(ns nuclojure.app
  (:require [compojure.core :refer :all]
            [liberator.core :refer [resource defresource]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [nuclojure.config :refer [jws]]
            [nuclojure.utils.db :as db]
            [nuclojure.routes :refer [routes-handler]]))

(def auth-backend (backends/jws {:secret (:secret jws) :token-name (:token-name jws)}))

(def handler
  (-> routes-handler
      ;; serialize the response body into JSON
      wrap-json-response
      ;; if the request body contains a JSON, parse it into a Clojure datastructure
      (wrap-json-body {:keywords? true :bigdecimals? true})
      ;; auth
      (wrap-authentication auth-backend)
      ;; mainly url-encoded support
      (wrap-defaults api-defaults)
      ))

(defn init
  []
  (println "Initializing server")
  (db/connect)
  (db/create-schema))


