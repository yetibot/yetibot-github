(defproject yetibot/github "_"
  :description "A yetibot plugin for https://github.com"
  :url "https://github.com/yetibot/yetibot-github"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :repl-options {:init-ns yetibot.core.repl
                 :timeout 120000
                 :prompt (fn [ns] (str "\u001B[35m[\u001B[34m" ns
                                       "\u001B[35m] \u001B[37mλ:\u001B[m "))}

  :plugins [[lein-inferv "20201020.015531.74d2ced"]
            [lein-environ "1.1.0"]
            [lein-midje "3.2.1"]]

  :deploy-repositories [["releases"  {:url "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [district0x/graphql-query "1.0.6"]]

  :profiles
  {:provided {:dependencies [[yetibot/core "LATEST"]
                             [irresponsible/tentacles "0.6.6"]]}
   :profiles/dev {}
   :dev [:profiles/dev
         {:dependencies [[lambdaisland/kaocha-midje "0.0-5"
                          :exclusions [midje/midje]]
                         [midje/midje "1.9.9"]]}]})
