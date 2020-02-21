(ns nuclojure.utils.encryption
  (:require [buddy.hashers :as hashers]))

(defn encrypt
  [decrypted]
  (hashers/derive decrypted))


(defn check
  [decrypted encrypted]
  (hashers/check decrypted encrypted))