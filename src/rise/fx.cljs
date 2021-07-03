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

(comment
  ; from predict code
  #_(defn get-dictionary
      "read dictionary ops from a url.
  USED IN PRODUCTION DICTIONARY LOAD"
      ([url {:keys [on-error handler]}]
       (GET url {:error-handler file-error                      ;on-error
                 :handler       handler
                 :format        :transit                        ;:transit
                 }))))
  
(defn reg-factor
  "Register simple db subscription and event on a factor. Duplicate registrations are possible and will cause a console warning
   on startup. The final registration overwrites any previous ones. This function can be used to register db keys at run-time.
   Both subscription and event are registered on the organ-namespaced factor.
   Events happen when an input changes the value of a factor.
   Subscriptions access input values by the factor key."
  [organ-k factor-k]
  (let [ref-k (keyword (name organ-k) (name factor-k))]
    (rf/reg-sub ref-k (fn [db] (get-in db [:inputs organ-k factor-k])))
    (rf/reg-event-db ref-k (fn [db [_ v]] (assoc-in db [:inputs organ-k factor-k] v)))))

(defn reg-factors
  "Function which registers all organ factors given in a seq of factor maps"
  [[organ fmaps]]
  (doseq [fmap fmaps]
    (reg-factor organ (:factor fmap))))

(rf/reg-fx :reg-factors reg-factors)
  

;;
;; load translation state; Call this to set up the translation system
;;
#_(defn load-translations*
  "load the initial dictionary into the rtdb t-state.
  local dictionary-url is the relative url to a txt file containing :upsert and :switch commands.
  At runtime, the callback handler is usually a call to handle-dictionary with ref bound to the t-state-change cursor.
  At test time this may be different.

  USED IN PRODUCTION DICTIONARY LOAD"
  ([local-dictionary-url handler]
   (get-dictionary
    local-dictionary-url {:headers {"Cache-Control" "no-store"}
                          :on-error file-error
                          :handler  handler})))



  

