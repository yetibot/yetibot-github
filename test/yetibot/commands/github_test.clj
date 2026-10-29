(ns yetibot.commands.github-test
  (:require
   [clj-http.client :as http]
   [clojure.data.json :as json]
   [midje.sweet :refer [fact => anything]]
   [yetibot.core.hooks :as hooks]
   [yetibot-github.commands.github :as github :refer [stats-cmd format-repo]]
   [yetibot-github.api.github :as gh]))

(def mock-poll-result {:a 1 :d 1 :c 1 :con 4})
(def mock-repo-info ["wilfred" "tv"])

(fact test-stats-cmd-polling-no-response
  (stats-cmd {:match (cons "dummy" mock-repo-info)}) =>
    {:result/error
     "Crunching the latest data for `wilfred/tv`, try again in a few moments 🐌"}
  (provided
    (gh/sum-stats anything anything) => nil))

(fact test-stats-cmd-polling-success-response
  (stats-cmd {:match (cons "dummy" mock-repo-info)}) =>
  {:result/data {:a 1 :c 1 :con 4 :d 1}
   :result/value "wilfred/tv: 1 commits, 1 additions, 1 deletions, 4 contributors"}
  (provided
    (gh/sum-stats anything anything) => mock-poll-result))

(def mock-comment {:id 123
                   :body "Thanks for the fix!"
                   :html_url "https://github.com/yetibot/yetibot/issues/899#issuecomment-123"})

(fact "posting a comment preserves the response as command data"
  (github/comment-on-issue-cmd
   {:match ["comment yetibot/yetibot#899 Thanks for the fix!"
            "yetibot" "yetibot" "899" "Thanks for the fix!"]}) =>
  {:result/data mock-comment
   :result/value "Comment added to issue #899"}
  (provided
    (gh/create-comment-on-issue "yetibot" "yetibot" "899" "Thanks for the fix!") => mock-comment))

(fact "a rejected comment is an error, not a success confirmation"
  (github/comment-on-issue-cmd
   {:match ["comment yetibot/yetibot#899 test"
            "yetibot" "yetibot" "899" "test"]}) =>
  {:result/error "403 - Resource not accessible by integration"}
  (provided
    (gh/create-comment-on-issue "yetibot" "yetibot" "899" "test") =>
    {:status 403 :body {:message "Resource not accessible by integration"}}))

(fact "the API posts the comment body using the configured endpoint and token"
  (let [request (atom nil)
        response (with-redefs [gh/endpoint "https://github.example.com/api/v3/"
                              gh/auth {:oauth-token "test-token"}
                              http/request (fn [req]
                                             (reset! request req)
                                             {:status 201
                                              :headers {"content-type" "application/json"}
                                              :body (json/write-str mock-comment)})]
                   (gh/create-comment-on-issue "yetibot" "yetibot" "899" "Thanks for the fix!"))]
    [response
     (select-keys @request [:method :url])
     (get-in @request [:headers "Authorization"])
     (json/read-str (:body @request) :key-fn keyword)]) =>
  [mock-comment
   {:method :post
    :url "https://github.example.com/api/v3/repos/yetibot/yetibot/issues/899/comments"}
   "token test-token"
   {:body "Thanks for the fix!"}])

(defn registered-subcommands []
  (let [commands (atom nil)]
    (with-redefs [gh/configured? (constantly true)
                  hooks/cmd-hook-resolved (fn [_ & cmds] (reset! commands cmds))]
      (require 'yetibot-github.commands.github :reload))
    @commands))

(fact "the registered command routes its captures to the comment handler"
  (let [[match handler] (hooks/match-sub-cmds
                        "comment yetibot/my-repo.name#899 Thanks for the fix!"
                        (registered-subcommands))]
    [match handler]) =>
  [["comment yetibot/my-repo.name#899 Thanks for the fix!"
    "yetibot" "my-repo.name" "899" "Thanks for the fix!"]
   #'github/comment-on-issue-cmd])

(defn command-requests [commands command]
  (let [requests (atom [])
        [_ args] (hooks/split-command-and-args command)
        [match handler] (hooks/match-sub-cmds args commands)]
    (with-redefs [http/request (fn [request]
                                (swap! requests conj request)
                                {:status 201
                                 :headers {"content-type" "application/json"}
                                 :body (json/write-str mock-comment)})]
      (when handler
        (handler {:match match})))
    @requests))

(fact "other subcommands containing comment never post to GitHub"
  (let [commands (registered-subcommands)]
    (mapv #(command-requests commands %)
          ["gh uncomment yetibot/yetibot#899 do not post this"
           "gh please comment yetibot/yetibot#899 do not post this"])) =>
  [[] []])

(fact "multiline comments reach GitHub without truncation"
  (let [body "First line\n\n```clojure\n(+ 1 2)\n```\nLast line\n"
        requests (command-requests
                  (registered-subcommands)
                  (str "github comment yetibot/yetibot#899 " body))]
    [(count requests)
     (:method (first requests))
     (json/read-str (:body (first requests)) :key-fn keyword)]) =>
  [1 :post {:body "First line\n\n```clojure\n(+ 1 2)\n```\nLast line\n"}])
