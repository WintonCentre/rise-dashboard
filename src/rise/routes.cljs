(ns rise.routes
  (:require
   [re-frame.core :as rf]
   [reitit.core :as r]
   [reitit.coercion.spec :as rss]
   [reitit.frontend :as rfr]
   [reitit.frontend.controllers :as rfc]
   [reitit.frontend.easy :as rfe]
   #_[rise.ui :as ui]
   [rise.events :as events]
   [rise.views :as views]
   [rise.subs :as subs]
   [shadow.debug :refer [locals ?> ?-> ?->>]]
   #_["react-bootstrap" :as bs :refer [Navbar Navbar.Brand Navbar.Toggle Navbar.Collapse Navbar.Text
                                       Nav Nav.Link]]))
(comment
  (paths/centres-path :lung))

(def routes
  "Reitit nested route syntax can be tricky. Only the leaves are valid.
   This example is helpful:
   (def route
     (r/router
      [\"/api\"
       [\"\" ::api] ; <-- necessary to make \"/api\" a valid leaf route
       [\"/ping\" ::ping]
       [\"/user/:id\" ::user]]))
   as it defines valid routes for /api, /ping, and /user/fred"
  ["/"
   [""
    {:name      ::views/home
     :view      views/home-page
     :link-text "Home"
     :controllers [{;; Do whatever initialization needed for home page
                    :start (fn [& params] (println ::routes "Entering Home " params))
       ;; Teardown can be done here.
                    :stop   (fn [& params] (println ::routes "Leaving Home " params))}]}]

   ["info" {:name      ::views/info
            :view      views/info
            :link-text "Info"
            :controllers [{:start (fn [& params] (js/console.log "Entering info: " params))
                           :stop  (fn [& params] (js/console.log (str "Leaving Info" params)))}]}
    [""] ; required to make ["åinfo"] a leaf route
    ]
   ["hex/:id" {:name      ::views/hex
               :view      views/hex
               :link-text "Location"
               :controllers [{:start (fn [& params] (js/console.log "Entering hex: " params))
                              :stop  (fn [& params] (js/console.log (str "Leaving hex" params)))}]}
    [""] ; required to make ["hex"] a leaf route
    ]
   ["regions/:id" {:name      ::views/regions
                   :view      views/regions
                   :link-text "Region"
                   :controllers [{:start (fn [& params] (js/console.log "Entering regions: " params))
                                  :stop  (fn [& params] (js/console.log (str "Leaving regions" params)))}]}
    [""] ; required to make ["regions"] a leaf route
    ]
   ["countries/:id" {:name      ::views/countries
                   :view      views/countries
                   :link-text "Country"
                   :controllers [{:start (fn [& params] (js/console.log "Entering countries: " params))
                                  :stop  (fn [& params] (js/console.log (str "Leaving countries" params)))}]}
    [""] ; required to make ["regions"] a leaf route
    ]])

(comment
  (def rts (r/router routes))
  (r/match-by-path rts "/")
  (r/match-by-path rts "/info/1")
  (:data (r/match-by-path rts "/regions/lazio"))
  0)

(defn on-navigate [new-match]
  (let [old-match (rf/subscribe [::subs/current-route])]
    (when new-match
      (let [cs (rfc/apply-controllers (:controllers @old-match) new-match)
            m  (assoc new-match :controllers cs)]
        (rf/dispatch [::events/navigated m])))))

(def router
  (rfr/router
   routes
   {:data {:coercion rss/coercion}}))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start!
   router
   on-navigate
   {:use-fragment true}))

(comment
  (routes [:home :info]))