(ns rise.views
  (:require
   [clojure.string :as string]
   [re-frame.core :as rf]
   ["react-bootstrap" :as bs]
   [rise.subs :as subs]
   [rise.events :as events]
   [rise.ui :as ui]
   [rise.widgets :as widg]
   [shadow.debug :refer [locals ?> ?-> ?->>]]))

(comment
  (rf/dispatch [::events/initialize-db]))

;;; Views ;;;
(defn home-page
  "Display a generic home page. 
   Minimally, navigation from here to an organ home page."
  []
  [ui/page "RISE Dashboard demo"
   [ui/row
    [ui/col
     "content here"]]])

(defn info
  []
  [:h1 "Info"])

(defn hex
  []
  (let [location (get-in @(rf/subscribe [::subs/current-route])
                        [:path-params :id])]
    (locals)
    [ui/page
     [ui/row
      [ui/col {:md 3}
       [:> bs/Image {:src (str "/assets/" location " hex.png")
                     :fluid true}]
       [:> bs/Image {:src "/assets/M4scale.png"
                   :fluid true}]]
      [ui/col {:md 9}]]]))



