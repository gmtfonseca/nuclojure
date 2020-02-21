(ns nuclojure.models.user
  (:require [clojure.spec.alpha :as s]
            [nuclojure.utils.db :as db]
            [nuclojure.utils.validation :as val]
            [nuclojure.utils.encryption :as encryption]
            [nuclojure.spec-types :as specs]
            [nuclojure.models.protocol :refer [Model]]))

;; SPECS
(s/def ::email ::specs/email-type)
(s/def ::password #(>= (count %) 5))
(s/def ::user (s/keys :req [::email ::password] :opt []))


(defrecord User [email password]
  Model
  (save!
    [this]
    (let [user [{:user/email (:email this)
                 :user/password (encryption/encrypt (:password this))}]]
      (db/extract-eid-from-tx (db/transact user)))))


(defn make-user
  "Take map and creates new valid User object. Throws exception if
  key values dont match schema"
  [{:keys [email password]}]
  (let [user-body {::email email
                   ::password password}]
    (if-let [errors (val/conform-spec ::user user-body)]
      (throw (ex-info "Invalid body" errors))
      (->User email password))))


;; SCHEMA BASED METHODS
(defn find-all
  "Query all users from db"
  []
  (let [query '[:find (pull ?e [*])
                :where [?e :user/email ?email]]
        users (db/exec-query query)]
    (map #(first %) users)))