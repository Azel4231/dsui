(defproject dsui "0.1.0-SNAPSHOT"
  :description "DataStructure UI"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha11"]
                 [org.clojure/test.check "0.9.0" :scope "test"]]

  :plugins []
  :main ds.ui
  :source-paths ["src"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
