(ns nuclojure.models.auth
  (:require [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [nuclojure.utils.db :as db]
            [nuclojure.utils.encryption :as encryption]
            [nuclojure.config :refer [jws]]))


(defn signed-token
  "Signs payload with given user and default JWS config"
  [user]
  (jwt/sign {:user user
             :exp (time/plus (time/now) (time/seconds (:exp jws)))}
    (:secret jws)))


(defn signin
  "Check if user and password match. If they do, returns
  a map with user email and JWT."
  [email password]
  (let [query '[:find (pull ?e (*))
                :in $ ?email
                :where [?e :user/email ?email]
                [?e :user/password ?password]]
        user (ffirst (db/exec-query query (list email)))
        authorized? (encryption/check password (:user/password user))]

    (when authorized?
      {:email (:user/email user)
       :token (signed-token (:db/id user))})))

