(ns nuclojure.utils.datetime
  (:import (java.text SimpleDateFormat)))

(defonce date-format "dd/MM/yyyy")

(defn to-string
  [date]
  (.format (SimpleDateFormat. date-format) date))


(defn to-date
  [string]
  (.parse (SimpleDateFormat. date-format) string))