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
(defn weekly-factor
  "We want about 1 simulated quake every t seconds for a rate of k quakes per week, but we 
   also want the weekly factor to be an easily understood number."
  [k t]
  (let [w (/ 1 k t)]
    (condp > w
      100000 100000
      50000 50000
      20000 20000
      10000 10000
      5000 5000
      2000 2000
      1000 1000
      500 500
      200 200
      100 100))
  )

(comment
  ;; Example: Say real-k = 0.01, so earthquake mean interval is 100 real weeks.
  ;; sim-k should be 0.2 to generate 1 sim-quake every 20 seconds
  ;; That 20 seconds is about constant - call it T
  ;; And each simulated second represents 5 real weeks - the week-factor is 5 = (/ 1 real-k T)

  (weekly-factor 0.5 1)
  0)

(defn next-quake-interval
  "How many weeks to next scheduled simulated quake"
  [p]
  (- (/ (js/Math.log (rand)) p)))

(defn next-quake-time
  "system time in ms + (quake interval converted to ms) / simulated-weeks-per-interval
   p is the earthquake probability per week
   week-factor is how many weeks we simulate in one second"
  [p week-factor]
  (+ (.now js/Date.) (* (/ 1000 week-factor) (next-quake-interval p))))

(defn average
  "keep calling f to find its average value"
  [f]
  (/ (apply + (map f (range 1000))) 1000))


  (comment
    (average #(next-quake-interval 0.5))
    ;; => 2.1261752103174656
    ;; should be about 2

    ;; should schedule quakes roughly with a mean 20s interval. 
    (next-quake-time 0.5 0.1)
    ;; => 1.6263588112591138E12


    ;; Text under map will say something like 
    "Every 10 seconds we simulate whether or not a Mag 4+ event happens using the probabilty for next week"

    0
    )


;; set animate? or not. If animate? is false.
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



