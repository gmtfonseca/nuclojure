(ns nuclojure.controllers.movement
  (:require [liberator.core :refer [resource defresource]]
            [nuclojure.models.movement :as movement]
            [nuclojure.config :refer [resource-auth resource-defaults resource-post]]
            [nuclojure.utils.datetime :as datetime]
            [nuclojure.models.protocol :as Model]
            [nuclojure.utils.validation :as val]))


(defn- parse-body
  "Parse request body based on entity datatypes."
  [{:keys [date amount description type]}]
  (try
    (let [parsed-body {:date (datetime/to-date date)
                       :amount (bigdec amount)
                       :description description
                       :type (keyword type)}]
      [false {:parsed-body parsed-body}])
    (catch Exception _
      [true {:msg "Malformed body"}])))


(defn- parse-movement
  "Create a new Movement object based on parsed body"
  [parsed-body]
  (try
    (let [movement (movement/make-movement parsed-body)]
      [true {:movement movement}])
    (catch Exception e
        [false {:forbidden-body (val/parse-spec-from-exception e)}])))


(defresource save-resource
  [body]
  (merge resource-defaults resource-auth resource-post)
  :allowed-methods [:post]
  :malformed? (fn [_] (parse-body body))
  :allowed? (fn [ctx] (parse-movement (:parsed-body ctx)))
  :post! (fn [ctx] (assoc ctx :entity (Model/save! (:movement ctx))))
  :handle-created :entity)


(defn- format-dates
  "Return collection of movements with JSON friendly dates"
  [movements]
  (map (fn[t]
         (update t
           :movement/date
           datetime/to-string))
    movements))


(defresource list-resource
  []
  (merge resource-defaults resource-auth)
  :allowed-methods [:get]
  :handle-ok (fn [_] (format-dates (movement/find-all))))


(defresource delete-resource
  [eid]
  (merge resource-defaults resource-auth)
  :allowed-methods [:delete]
  :delete! (fn [ctx] (assoc ctx :entity (movement/delete! eid)))
  :respond-with-entity? true
  :handle-ok :entity)
