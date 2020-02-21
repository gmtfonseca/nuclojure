(ns nuclojure.models.movement
  (:require [clojure.spec.alpha :as s]
            [nuclojure.utils.db :as db]
            [nuclojure.utils.validation :as val]
            [nuclojure.models.protocol :refer [Model]]))

;; SPECS
(s/def ::date inst?)
(s/def ::amount #(and (not (nil? %)) (pos? %)))
(s/def ::description (s/nilable string?))
(s/def ::type  #(or (= % :in) (= % :out)))
(s/def ::movement (s/keys :req [::date ::amount ::type]
                          :opt [::description]))

(defrecord
  Movement [date amount description type]
  Model
  (save!
    [this]
    (let [movement [{:movement/date (:date this)
                     :movement/amount (:amount this)
                     :movement/description (or (:description this) "")
                     :movement/type (:type this)}]]
      (db/extract-eid-from-tx (db/transact movement)))))


(defn make-movement
  "Take map and create new valid Movement object. Throws exception if
  key values dont match schema"
  [{:keys [date amount description type]}]
  (let [movement-body {::date date
                       ::amount amount
                       ::description description
                       ::type type}]
    (if-let [errors (val/conform-spec ::movement movement-body)]
      (throw (ex-info "Invalid body" errors))
      (->Movement date amount description type))))


;; SCHEMA BASED FUNCTIONS
(defn delete!
  "Remove entity from db based on eid"
  [eid]
  (db/extract-eid-from-tx (db/retract-by-eid eid)))


(defn find-all
  "Query all movements from db"
  []
  (let [query '[:find (pull ?e [*])
                :where [?e :movement/date ?date]]
        query-result (db/exec-query query)]
    (map #(first %) query-result)))


