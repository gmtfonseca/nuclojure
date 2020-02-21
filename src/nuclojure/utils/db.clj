(ns nuclojure.utils.db
  (:require [datomic.client.api :as d]
            [ring.util.response :refer [response]]
            [nuclojure.config :refer [db]]))


(defonce conn (atom nil))


(defn connect
  "Connect to default db config."
  []
  (let [cfg {:server-type :peer-server
            :access-key (:access-key db)
            :secret (:secret db)
            :endpoint (:endpoint db)
            :validate-hostnames false}
        client (d/client cfg)]
    (reset! conn (d/connect client {:db-name (:name db)}))
    (println "Connected to db" @conn)))


(defn exec-query
  "Exec query against latest db."
  ([query]
    (let [db (d/db @conn)]
      (d/q query db)))
  ([query args]
    (let [db (d/db @conn)]
      (let [statement {:query query :args (conj args db)}]
        (d/q statement)))))


(defn transact
  "Perform a transaction with given data."
  [data]
  (d/transact @conn {:tx-data data}))


(defn retract-by-eid
  "Perform a full retraction of a given entity id."
  [eid]
  (let [entity (read-string eid)]
    (transact [[:db/retractEntity entity]])))


(defn extract-eid-from-tx
  "Pull entity id out of a transaction result."
  [tx]
  (let [entity (when (> (count (:tx-data tx)) 1)
                   (:e (last (:tx-data tx))))]
    {:entity entity}))


(defn create-schema []
  "Utility method used to create the application schema."
  (let [movement-schema [{:db/ident :movement/date
                        :db/valueType :db.type/instant
                        :db/cardinality :db.cardinality/one
                        :db/doc "The date of the movement"}

                       {:db/ident :movement/amount
                        :db/valueType :db.type/bigdec
                        :db/cardinality :db.cardinality/one
                        :db/doc "The amount of money involved"}

                       {:db/ident :movement/description
                        :db/valueType :db.type/string
                        :db/cardinality :db.cardinality/one
                        :db/doc "The description of the movement"}

                       {:db/ident :movement/type
                        :db/valueType :db.type/keyword
                        :db/cardinality :db.cardinality/one
                        :db/doc "The type of the movement {:in :out}"}]

        user-schema [{:db/ident :user/email
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "The user email"}

                     {:db/ident :user/password
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "The user encrypted password"}
                     ]]
  (transact movement-schema)
  (transact user-schema)))



