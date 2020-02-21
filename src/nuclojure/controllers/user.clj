(ns nuclojure.controllers.user
  (:require [liberator.core :refer [resource defresource]]
            [nuclojure.models.user :as user]
            [nuclojure.utils.validation :as val]
            [nuclojure.config :refer [resource-auth resource-defaults resource-post]]
            [nuclojure.models.protocol :as Model]))

(defn- parse-user
  "Create a new User object based on parsed body"
  [parsed-body]
  (try
    (let [user (user/make-user parsed-body)]
      [true {:user user}])
    (catch Exception e
      [false {:forbidden-body (val/parse-spec-from-exception e)}])))


(defresource save-resource
  [body]
  (merge resource-defaults resource-post)
  :allowed-methods [:post]
  :allowed? (fn [_] (parse-user body))
  :post! (fn [ctx] (assoc ctx :entity (Model/save! (:user ctx))))
  :handle-created :entity)


(defresource list-resource
  []
  (merge resource-defaults resource-auth)
  :allowed-methods [:get]
  :handle-ok (fn [_] (user/find-all)))