(ns nuclojure.config)

(defonce db {:name "nuclojure"
             :access-key "admin"
             :secret "admin"
             :endpoint "localhost:8998"})


(defonce jws {:token-name "Bearer"
              :secret "nuclojure"
              ;; expiration time in seconds
              :exp 999999})


(defonce resource-defaults {:service-available? {:representation {:media-type "application/json"}}
                            :available-media-types ["application/json"]
                            :handle-exception (fn [ctx] {:error (.getMessage (:exception ctx))})})

(defonce resource-auth {:authorized? (fn [ctx] [(boolean (:identity (:request ctx)))])
                        :handle-unauthorized {:error "Unauthorized"}})

(defonce resource-post {:handle-malformed (fn [ctx] {:error (:msg ctx)})
                        :handle-forbidden (fn [ctx] {:error (:forbidden-body ctx)})})