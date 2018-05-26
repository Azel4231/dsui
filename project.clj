(defproject org.clojars.azel4231/dsui "0.1.0"
  :description "Generate a readonly form-based UI for arbitrary data"
  :url "https://github.com/Azel4231/dsui"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/test.check "0.9.0" :scope "test"]]
  
  :deploy-repositories [["clojars"  {:sign-releases false
                                      :url "https://clojars.org/repo"}]]

  :plugins []
  :main dsui.examples
  :source-paths ["src"]
  :test-paths ["test"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
