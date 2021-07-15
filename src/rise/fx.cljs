(ns rise.fx
  (:require [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]))

;;; Effects ;;;

;; Triggering navigation from events.
;; k is the route :name as defined in routes/routes
;; params are any url parameters
;; query is any query parameters
(rf/reg-fx
 ::navigate!
 (fn [[k params query]]
   ;(println "NAVIGATE!" k params query)
   (rfe/push-state k params query)))


;; Loading edn files from the database
(rf/reg-fx
::dispatch
(fn [[event-key event-params]]
  (rf/dispatch [event-key event-params])))

