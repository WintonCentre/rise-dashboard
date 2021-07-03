(ns rise.subs
  (:require
   [re-frame.core :as rf]
   ))

;;; 
;; Note that subscription keys are fully qualified in rise.subs ns, but...
;; db keys are global and named similarly
;; 
;; subscripton keys are used in views (usually) to READ db values
;; db keys are used in event handlers (always) to WRITE to the db
;;; 
(rf/reg-sub ::window-width (fn [db] (:window-width db)))

(rf/reg-sub ::current-route (fn [db] (:current-route db)))

(rf/reg-sub ::mdata (fn [db] (:mdata db)))

(rf/reg-sub ::organ (fn [db] (:organ db)))

(rf/reg-sub ::pages (fn [db] (:pages db)))

(rf/reg-sub ::organ-centres (fn [db] (get-in db [:organ-centres])))

(rf/reg-sub ::background-info (fn [db] (get-in db [:background-info])))

(rf/reg-sub ::guidance-percent (fn [db] (get-in db [:guidance-percent])))

(rf/reg-sub ::randomise-icons (fn [db] (get-in db [:randomise-icons])))

(rf/reg-sub ::tools (fn [db] (get-in db [:tools])))

(rf/reg-sub ::tool (fn [db] (get-in db [:tool])))

(rf/reg-sub ::centre (fn [db] (get-in db [:centre])))

(rf/reg-sub ::bundles (fn [db] (get-in db [:bundles])))

(rf/reg-sub ::numerics (fn [db] (get-in db [:numerics])))

(rf/reg-sub ::inputs (fn [db] (get-in db [:inputs])))

(rf/reg-sub ::test-day (fn [db] (get-in db [:test-day])))

(rf/reg-sub ::cohort-dates (fn [db] (get-in db [:cohort-dates])))

(rf/reg-sub ::selected-vis (fn [db] (get-in db [:selected-vis])))

(rf/reg-sub ::mdata (fn [db] (get-in db [:mdata])))


