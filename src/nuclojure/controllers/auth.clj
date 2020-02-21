(ns nuclojure.controllers.auth
  (:require [liberator.core :refer [resource defresource]]
            [nuclojure.models.auth :as auth]
            [nuclojure.config :refer [resource-defaults]]))


(defresource signin-resource
  [{:keys [email password]}]
  resource-defaults
  :allowed-methods [:post]
  :post! (fn [ctx]
           (assoc ctx
             :identity
             (auth/signin email password)))
  :new? false
  :respond-with-entity? true
  :handle-ok (fn [ctx]
               (if-let [identity (:identity ctx)]
                 identity
                 {:error "Invalid email or password"})))
