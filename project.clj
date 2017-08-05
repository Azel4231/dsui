(defproject dsui "0.2.0-SNAPSHOT"
  :description "DataStructure UI"
  :url "https://github.com/Azel4231/dsui"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/test.check "0.9.0" :scope "test"]
                 [halgari/fn-fx "0.3.0-SNAPSHOT"]
                 ]

  :plugins []
  :main dsui.examples
  :source-paths ["src"]
  :test-paths ["test"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
