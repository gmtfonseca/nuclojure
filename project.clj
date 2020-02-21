(defproject nuclojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [liberator "0.15.3"]
                 [buddy/buddy-hashers "1.4.0"]
                 [buddy/buddy-auth "2.2.0"]
                 [com.datomic/client-pro "0.9.41" :exclusions [org.eclipse.jetty/jetty-client
                                                               org.eclipse.jetty/jetty-http
                                                               org.eclipse.jetty/jetty-util]]
                 ;solves dependency problem with compojure + datomic
                 [info.sunng/ring-jetty9-adapter "0.10.0"]
                 ]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler nuclojure.app/handler :init nuclojure.app/init}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
