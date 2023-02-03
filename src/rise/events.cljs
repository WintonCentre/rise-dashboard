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


;;;
;; This is OK for the UI test rig, but very high and very low probabilities are
;; clamped to this range. Very low can be extended in the table. 
;; But very high probability giving less than a week intervals would require the
;; displayed phrase to switch away from using weeks.
;;;


;;
;; Needs reorganising so we don't need to key by language and so the texts 
;; can be edited in the language files english.cljs, german.cljs etc.
;;
;; Replace texts here with dictionary keywords? 
;; And then return time-factors to being language agnostic
;;
(def time-factors {:en {:1 "1 week"
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
                        :520 "10 years"
                        :5200 "100 years"
                        :52000 "1000 years"}
                   :de {:1 "1 Woche"
                        :2 "2 Wochen"
                        :3 "3 Wochen"
                        :4 "1 Monat"
                        :8 "2 Monate"
                        :12 "3 Monate"
                        :16 "4 Monate"
                        :26 "6 Monate"
                        :52 "1 Jahr"
                        :104 "2 Jahren"
                        :156 "3 Jahren"
                        :208 "4 Jahren"
                        :260 "5 Jahren"
                        :312 "6 Jahren"
                        :364 "7 Jahren"
                        :416 "8 Jahren"
                        :520 "10 Jahren"
                        :5200 "100 Jahren"
                        :52000 "1000 Jahren"}
                   :it {:1 "1 settimana"
                        :2 "2 settimane"
                        :3 "3 settimane"
                        :4 "1 mese"
                        :8 "2  mesi"
                        :12 "3 mesi"
                        :16 "4 mesi"
                        :26 "6 mesi"
                        :52 "1 anno"
                        :104 "2 anni"
                        :156 "3 anni"
                        :208 "4 anni"
                        :260 "5 anni"
                        :312 "6 anni"
                        :364 "7 anni"
                        :416 "8 anni"
                        :520 "10 anni"
                        :5200 "100 anni"
                        :52000 "1000 anni"}
                   })

(defn closest 
  "returns the closest value to x in coll"
  [x coll]
  (first (sort-by #(js/Math.abs (- x %)) coll)))

(defn pretty-factor
  "returns the closest factor to x from time factors"
  [lang x]
  (let [coll (mapv #(js/parseInt (name %)) (keys (time-factors lang)))]
    (closest x coll)))

(defn time-of-next-quake
  [p]
  (- (/ (js/Math.log (rand)) p)))

(defn time-acceleration-factor
  "Scaling factor to get an average time of m between earthquakes of probability p"
  [lang p m]
  (pretty-factor lang (/ 1000 (* m p))))



(defn average
  "keep calling f to find its average value"
  [f]
  (/ (apply + (map f (range 100))) 100))


  (comment
    (time-acceleration-factor :en 0.022 15000)
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
 ::in-percentage?
 (fn
   [db [_ bool]]
   (assoc db
          :in-percentage? bool)))


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

