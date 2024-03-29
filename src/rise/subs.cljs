(ns rise.subs
  (:require
   [re-frame.core :as rf]))

;;; 
;; Note that subscription keys are fully qualified in rise.subs ns, but...
;; db keys are global and named similarly
;; 
;; subscripton keys are used in views (usually) to READ db values
;; db keys are used in event handlers (always) to WRITE to the db
;;; 

(rf/reg-sub ::lang (fn [db] (:lang db)))

(rf/reg-sub ::current-route (fn [db] (:current-route db)))

(rf/reg-sub ::communities (fn [db] (:communities db)))

(rf/reg-sub ::regions (fn [db] (:regions db)))

(rf/reg-sub ::countries (fn [db] (:countries db)))

(rf/reg-sub ::mag+ (fn [db] (:mag+ db)))

(rf/reg-sub ::animate? (fn [db] (:animate? db)))

(rf/reg-sub ::with-context? (fn [db] (:with-context? db)))

(rf/reg-sub ::with-vis? (fn [db] (:with-vis? db)))

(rf/reg-sub ::annular? (fn [db] (:annular? db)))

(rf/reg-sub ::odds? (fn [db] (:odds? db)))

(rf/reg-sub ::in-percentage? (fn [db] (:in-percentage? db)))

(rf/reg-sub ::average-cities (fn [db] (:average-cities db)))

(rf/reg-sub ::quake? (fn [db] (:quake? db)))

(rf/reg-sub ::next-quake-t (fn [db] (:next-quake-t db)))

(rf/reg-sub ::average-time-to-quake (fn [db] (:average-time-to-quake db)))
