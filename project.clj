(defproject yetibot/github "_"
  :description "A yetibot plugin for https://github.com"
  :url "https://github.com/yetibot/yetibot-github"
  :plugins [[lein-parent "0.3.9"]
            [lein-inferv "20201020.015531.74d2ced"]]
  :parent-project {:coords [yetibot/parent "20230419.003943.a089c31"]
                   :inherit [:license
                             :repl-options
                             :plugins
                             :deploy-repositories
                             :dependencies
                             :managed-dependencies]}
  :dependencies [[district0x/graphql-query "1.0.6"]]
  :profiles {:profiles/dev {}
             :dev [:profiles/dev]
             :provided {:dependencies [[irresponsible/tentacles "0.6.9"]]}})
