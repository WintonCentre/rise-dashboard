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

(def time-factors {:1 "1 week"
                   :2 "2 weeks"
                   :3 "3 weeks"
                   :4 "1 month"
                   :8 "2 months"
                   :12 "3 months"
                   :16 "4 months"
                   :26 "6 months"
                   :52 "1 year"
                   :104 "2 years"
                   :156 "3 years"
                   :208 "4 years"
                   :260 "5 years"
                   :312 "6 years"
                   :364 "7 years"
                   :416 "8 years"
                   :520 "10 years"})

(defn closest 
  "returns the closest value to x in coll"
  [x coll]
  (first (sort-by #(js/Math.abs (- x %)) coll)))

(defn pretty-factor
  "returns the closest factor to x from time factors"
  [x]
  (let [coll (mapv #(js/parseInt (name %)) (keys time-factors))]
    (closest x coll)))

(defn time-of-next-quake
  [p]
  (- (/ (js/Math.log (rand)) p)))

(defn time-acceleration-factor
  "Scaling factor to get an average time of m between earthquakes of probability p"
  [p m]
  (pretty-factor (/ 1000 (* m p))))



(defn average
  "keep calling f to find its average value"
  [f]
  (/ (apply + (map f (range 100))) 100))


  (comment
    (time-acceleration-factor 0.022 15000)
    (time-of-next-quake 0.5)
    (average #(time-of-next-quake 0.5))
    0
    )

;; set boolean for :quake? and time for :next-quake-t in db
(rf/reg-event-db
 ::quake?
 (fn
   [db [_ quake? time]]
   (cond
     time (assoc db :quake? quake?
                 :next-quake-t time)
     :else (assoc db :quake? quake?))))

;; set quake? probability
#_(rf/reg-event-db
   ::quake?
   (fn
     [db [_ quake?]]
     (assoc db :quake? quake?)))

;; set animate? or not. If animate? is false, then quake should be true
(rf/reg-event-db
 ::animate?
 (fn
   [db [_ animate?]]
   (assoc db 
          :animate? animate?
          :quake? (when animate? (db :quake?)))))

;; set animate? or not. If animate? is false.
(rf/reg-event-db
 ::odds?
 (fn
   [db [_ odds?]]
   (assoc db
          :odds? odds?
          )))

(rf/reg-event-db
 ::with-vis?
 (fn
   [db [_ with-vis?]]
   (assoc db
          :with-vis? with-vis?)))

(rf/reg-event-db
 ::with-context?
 (fn
   [db [_ with-context?]]
   (assoc db
          :with-context? with-context?)))

(rf/reg-event-db
 ::annular?
 (fn
   [db [_ annular?]]
   (assoc db
          :annular? annular?)))

(rf/reg-event-db
 ::set-language
 (fn
   [db [_ language]]
   (assoc db
          :lang language)))

(def reset-settings
  {:animate? false
    :with-context? false
    :with-vis? false
    :annular? false
    :odds? false})

(def presets
  "Anything listed inside the set #{} is selected.
   Everything else is off"
  {:preset1 #{:odds?
              :with-vis?
              :annular? ;speedo
              :animate?}
   :preset2 #{} ; RR, no vis, no animation
   :preset3 #{:odds?
              :with-vis? ;bar
              :animate?}
   :preset4 #{:with-vis?
              :annular? ;speedo
              }
   :preset5 #{:with-context?}})

(comment
  (merge reset-settings
         (map (fn [k]
                [k true])
              (get presets :preset5)))
  ;; => {:animate? false, :with-context? true, :with-vis? false, :annular? false, :odds? false}
  
    (merge reset-settings
           (map (fn [k]
                  [k true])
                nil))
    ;; => {:animate? false, :with-context? false, :with-vis? false, :annular? false, :odds? false}

  0)
;; => nil

;; Presets for testing. These choose certain selections
(rf/reg-event-db
 ::set-preset
 (fn
   [db [_ preset-id]]
   (merge db 
          (merge reset-settings
                 (map (fn [k] [k true])
                      (get presets preset-id))))))

