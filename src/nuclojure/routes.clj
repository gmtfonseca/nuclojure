(ns nuclojure.routes
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [nuclojure.controllers.movement :as movement-ctrl]
            [nuclojure.controllers.user :as user-ctrl]
            [nuclojure.controllers.auth :as auth-ctrl]))


(defroutes routes-handler
  ;; MOVEMENT
  (GET "/movement" [] (movement-ctrl/list-resource))
  (POST "/movement" {:keys [body]} (movement-ctrl/save-resource body))
  (DELETE "/movement/:eid" [eid] (movement-ctrl/delete-resource eid))
  ;; USER
  (GET "/user" [] (user-ctrl/list-resource))
  (POST "/user" {:keys [body]} (user-ctrl/save-resource body))
  ;; AUTH
  (POST "/auth/signin" {:keys [body]} (auth-ctrl/signin-resource body))
  (route/not-found (response {:error "Not found"})))