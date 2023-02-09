(ns rise.core
  (:require
   [reagent.dom :as rd]
   [re-frame.core :as rf]
   [rise.routes :as routes]
   [rise.events :as events]
   [rise.subs :as subs]
   [rise.ui :as ui]
   [error-boundary.error-boundary :refer [err-boundary]]
   [shadow.debug :refer [locals ?> ?-> ?->>]]
   ))

;(enable-console-print!)

;;; Setup ;;;
(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)))

(defn ^:dev/after-load mount-root
  "Mount components and start the reitit router. The :dev/after-load meta-data causes
   shadow-cljs to call mount-root after a hot-reload and clear the subsciption cache so
   everything gets updated nicely."
  []
;  (?-> "mount-root" ::mount-root)
  (rf/clear-subscription-cache!)
  (routes/init-routes!) ;; Reset routes on figwheel reload
  (rd/render [err-boundary
              [ui/root-component {:router routes/router
                                  :subscribe-current-route #(rf/subscribe [::subs/current-route])}]]
             (.getElementById js/document "app")))

(defn on-window-resize
  "Handle window-size change by dispatching new width to db"
  [evt]
  (rf/dispatch [::events/update-window-width (.-innerWidth js/window)]))

(comment
  (mount-root)
  )

(defn ^:export init
  "Initialise the database, sense window-width, and mount root of component tree. 
   
   We're breaking the rules slightly as this is an :afer-load function, which shadow-cljs docs 
   say must be synchronous. However the parts that need to be synchronous here are, and we 
   let the UI display a 'loading' state till the async dispatches complete. 
   
   If we coded this up as an :after-load-async function we'd need to provide a `done` function 
   to call on completion. Meanwhile, the UI would be dead.
   "
  []
  (rf/dispatch-sync [::events/initialize-db])

  ;;; Removing the following two lines as they hard-coded lung and kidney organs into the tool
  ;;; Instead, we obtain the organ list by reading in the metadata file above.
  ;(rf/dispatch [::events/load-and-transpose [(paths/centres-path :lung) [:organ-centres :lung]]])
  ;(rf/dispatch [::events/load-and-transpose [(paths/centres-path :kidney) [:organ-centres :kidney]]])

  (.addEventListener js/window "resize" on-window-resize)
  (dev-setup)

  (mount-root)) 

(defonce start-up (do (init) true))

#_(comment
  (defn mount-app-element []
    (when-let [el (get-app-element)]
      (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
  (mount-app-element)

;; specify reload hook with ^;after-load metadata
  (defn ^:after-load on-reload []
    (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
    ))