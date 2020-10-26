(defproject yetibot/github "_"
  :description "A yetibot plugin for https://github.com"
  :url "https://github.com/yetibot/yetibot-github"
  :plugins [[lein-parent "0.3.8"]]
  :parent-project {:coords [yetibot/parent "20201026.012801.efa80f2"]
                   :inherit [[:profiles :dev]
                             :license
                             :repl-options
                             :plugins
                             :deploy-repositories
                             :dependencies
                             :managed-dependencies]}
  :dependencies [[district0x/graphql-query "1.0.6"]]
  :profiles {:provided {:dependencies [[irresponsible/tentacles "0.6.6"]]}})
