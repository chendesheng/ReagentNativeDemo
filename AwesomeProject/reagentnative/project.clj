(defproject reagentnative "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-beta2"]
                 [org.clojure/clojurescript "0.0-3269"]
                 [reagent "0.5.0" :exclusions [cljsjs/react]]]
  :main ^:skip-aot reagentnative.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[lein-cljsbuild "1.0.6"]]
  :cljsbuild {:builds
              {:min {:source-paths ["src"]
               :compiler {:output-to ".main.js"
                          :output-dir "out"
                          :optimizations :whitespace}}}})
