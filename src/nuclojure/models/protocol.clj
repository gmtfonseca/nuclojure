(ns nuclojure.models.protocol)

(defprotocol Model
  (save! [this] "Save entity into DB"))