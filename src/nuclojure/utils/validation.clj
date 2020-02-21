(ns nuclojure.utils.validation
  (:require [clojure.spec.alpha :as s]))

;; Former validation function. Good use case of recursion.
(comment (defn check-errors
  [body validations]
  (loop [errors {}
         vals (seq validations)]
    (let [attribute (ffirst vals)
          handler (second (first vals))]
      (if (nil? attribute)
        errors
        (recur
          (cond
            (and
              (true? (:required handler))
              (or (not (contains? body attribute)) (nil? (attribute body))))
                (assoc errors attribute "Value is required")
            (and
              (contains? body attribute)
              ((:validator handler) (attribute body)))
                (assoc errors attribute (:message handler))
            :else errors)
          (next vals)))))))


(defn conform-spec
  "Return map of specification problems, otherwise returns nil"
  [spec body]
  (let [parsed (s/conform spec body)]
    (when (= parsed ::s/invalid)
      (s/explain-data spec body))))


(defn parse-spec-from-exception
  "Take a spec exception and produce a map with parsed values"
  [e]
  (let [problems (first (:clojure.spec.alpha/problems (.getData e)))]
    {:msg (.getMessage e)
     :spec (first (:path problems))
     :val (:val problems)
     :predicate (str (:pred problems))}))