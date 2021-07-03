(ns rise.events
  (:require
   (winton-utils.data-frame :refer [map-of-vs->v-of-maps])
   [re-frame.core :as rf]
   [day8.re-frame.http-fx]
   [rise.fx :as fx]
   [rise.db :as init-db]
   [ajax.core :as ajax]
   [cljs.reader :as  edn]
   [clojure.string :as string]
   [clojure.set :as rel]
   [shadow.debug :refer [locals ?> ?-> ?->>]]))

;;; Events ;;;

(rf/reg-event-db
 ::initialize-db
 (fn ;-traced 
   [_ _]
   (merge init-db/default-db
          {:current-route nil
          :inputs {}
           :selected-vis "bars"})))

(rf/reg-event-db
 ::update-window-width
 (fn [db [_ new-width]]
   (assoc db :window-width new-width)))

(rf/reg-event-fx
 ::navigate
 (fn ;-traced 
   [{:keys [db]} [_ route params query]]
   ;; See `navigate` effect in routes.cljs
   {::fx/navigate! [route params query]}))

(rf/reg-event-db
 ::navigated
 (fn ;-traced 
   [db [_ new-match]]
   (assoc db :current-route new-match)))

(rf/reg-event-db
 ; active page
 ::page
 (fn ;-traced 
   [db [_ page]]
   (assoc db :organ page)))


(rf/reg-event-db
 ; reset inputs
 ::reset-inputs
 (fn ;-traced 
   [db [_ _]]
   (assoc db :inputs {})))

(rf/reg-event-db
 ; background-info
 ::background-info
 (fn ;-traced 
   [db [_ b-info]]
   (assoc db :background-info b-info)))

(rf/reg-event-db
 ; randomise-icons
 ::randomise-icons
 (fn ;-traced 
   [db [_ _]]
;   (?-> (:randomise-icons db) ::randomise-icons)
   (update db :randomise-icons not)))


(comment
  (enable-console-print!)
  )



;;;
;; Process raw tool bundles into db. 
;; 
;; It may be more efficient to do this processing at configuration time
;;;


(rf/reg-event-db
 ::selected-vis
 (fn ;-traced
   [db [_ selection]]
   (assoc db :selected-vis selection)))

(rf/reg-event-db
 ::store-response
 (fn
  [db [_ data-path response]]
  (-> db
      (assoc-in data-path (edn/read-string response)))))

(rf/reg-event-db
 ::transpose-response
 (fn
  [db [_ data-path response]]
  (-> db
      (assoc-in data-path (map-of-vs->v-of-maps (edn/read-string response))))))

(rf/reg-event-db
 ::bad-response
 (fn
  [db [_ data-path response]]
  #_(when (or data-path response)
      (js/alert (str "bad-response while loading " data-path "response = " response)))
  db))

