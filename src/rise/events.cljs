(ns rise.events
  (:require
   [re-frame.core :as rf]
   [day8.re-frame.http-fx]
   [rise.fx :as fx]
   [rise.db :as init-db]
   [shadow.debug :refer [locals ?> ?-> ?->>]]))

;; Need to clean out some transplants related events here...

;;; Events ;;;
(rf/reg-event-db
 ::initialize-db
 (fn
   [_ _]
   (merge init-db/default-db
          {})))

(rf/reg-event-db
 ::update-window-width
 (fn [db [_ new-width]]
   (assoc db :window-width new-width)))

(rf/reg-event-fx
 ::navigate
 (fn
   [{:keys [db]} [_ route params query]]
   ;; See `navigate` effect in routes.cljs
   {::fx/navigate! [route params query]}))

(rf/reg-event-db
 ::navigated
 (fn 
   [db [_ new-match]]
   (assoc db :current-route new-match)))

;;;
;; earthquake timer
;;;

(defn time-of-next-quake
  [p]
  (- (/ (js/Math.log (rand)) p)))

(defn average
  "keep calling f to find its average value"
  [f]
  (/ (apply + (map f (range 100))) 100))


  (comment
    (time-of-next-quake 0.5)
    (average #(time-of-next-quake 0.5))
    0)


;; set animate? or not. If animate? is false, then quake should be true
(rf/reg-event-db
 ::animate?
 (fn
   [db [_ animate?]]
   (assoc db 
          :animate? animate?
          :quake? (when animate? (db :quake?)))))

(rf/reg-sub ::animate? (fn [db] (:animate? db)))

;; set quake? probability
#_(rf/reg-event-db
 ::quake?
 (fn
   [db [_ quake?]]
   (assoc db :quake? quake?)))




