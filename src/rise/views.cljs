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
  [ui/page "Info"
   [ui/row
    [ui/col
     "An info page placeholder"]]])

(defn countries
  []
  (let [country-id (get-in @(rf/subscribe [::subs/current-route])
                          [:path-params :id])
        all-countries (group-by :id (:items @(rf/subscribe [::subs/countries])))
        country (first (all-countries country-id))]
    (locals)
    [ui/page (country :title)
     [ui/row
      [ui/col {:md 3}
       [:> bs/Image {:src (str "/assets/" country-id ".png")
                     :width "100%"
                     :fluid true}]
       [:> bs/Image {:src "/assets/M4scale.png"
                     :width "100%"
                     :fluid true}]]]]))


(defn regions
  []
  (let [region-id (get-in @(rf/subscribe [::subs/current-route])
                          [:path-params :id])
        all-regions (group-by :id (:items @(rf/subscribe [::subs/regions])))
        region (first (all-regions region-id))]
    (locals)
    [ui/page (region :title)
     [ui/row
      [ui/col {:md 3}
       [:> bs/Image {:src (str "/assets/" region-id ".png")
                     :width "100%"
                     :fluid true}]
       [:> bs/Image {:src "/assets/M4scale.png"
                     :width "100%"
                     :fluid true}]]]]))


(defn hex
  []
  (let [location (get-in @(rf/subscribe [::subs/current-route])
                         [:path-params :id])]
    (locals)
    [ui/page
     [ui/row
      [ui/col {:md 3}
       [:> bs/Image {:src (str "/assets/" location " hex.png")
                     :width "100%"
                     :fluid true}]
       [:> bs/Image {:src "/assets/M4scale.png"
                     :width "100%"
                     :fluid true}]]
      [ui/col {:md 9}]]]))



