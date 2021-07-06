(ns rise.views
  (:require
   [clojure.string :as string]
   [re-frame.core :as rf]
   ["react-bootstrap" :as bs]
   [rise.subs :as subs]
   [rise.events :as events]
   [rise.ui :as ui]
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

(defn links-to
  [items]
  (into [:<>]
        (map
         (fn [item]
           (?-> item ::item)
           [:div  {:key (item :id)}
            (if (item :href)
              [:a {:href (ui/href (item :href) {:id (item :id)})}
               (item :title)]
              (item :title))])
         items)))
(comment
  (def items {:neighbours {:NE "Spoleto-N", :SE "Spoleto", :S "Spoleto-SW"}
              :p-7day 0.014
              :longitude 12.7376
              :osm-href "https://www.openstreetmap.org/relation/42105"
              :title "Spoleto-NW"
              :region "umbria"
              :mean-7day 0.00011
              :id "spoleto-NW"
              :latitude 42.739
              :country "italy"
              :map "spoleto-NW hex.png"
              :href :rise.views/hex})
  (def items [{:title "Umbria"
               :id "umbria"
               :country "italy"
               :href :rise.views/countries}])
  (links-to items)
  0)


(defn regions
  []
  (let [region-id (get-in @(rf/subscribe [::subs/current-route])
                          [:path-params :id])

        all-regions (group-by :id (:items @(rf/subscribe [::subs/regions])))
        region (first (all-regions region-id))
        all-countries (group-by :id (:items @(rf/subscribe [::subs/countries])))
        country (first (all-countries (region :country)))
        all-communities (:items @(rf/subscribe [::subs/communities]))
        regional-communities (filter #(= (:region %) region-id) all-communities)]
    (locals)
    [ui/page [:span (region :title) " (" [:a {:href (ui/href (country :href) {:id (country :id)})} (country :title)] ")"]
     [ui/row
      [ui/col {:md 3}
       [:> bs/Image {:src (str "/assets/" region-id ".png")
                     :width "100%"
                     :fluid true}]
       [:> bs/Image {:src "/assets/M4scale.png"
                     :width "100%"
                     :fluid true}]]
      [ui/col {:md 9}

       (links-to regional-communities)]]]))

(defn large
  [& n]
  (into [:span {:style {:font-size "1.8em"}}]
        (map str n)))

(defn area-status
  "show earthquake status of an area"
  [area p mean]
  (let [mag+ @(rf/subscribe [::subs/mag+])]
    [:div {:style {:font-size "14px"}}
     [:h2 "How likely is a magnitude " mag+ " or above earthquake"
      " in the " (area :title) " area within the 7 days from 6th July – 13th July?"]
    
     [ui/row
      [ui/col {:md 9 :style {:font-size 21
                             ;:border "1px solid #CCC"
                             :padding 15}}

       [:p "The likelihood is "
        [large (.toFixed (js/Number (* p 100)) 1) "%, "]
        " which is "
        [large (js/Math.round (/ p mean))] " times the likelihood in an average week."]
       ;[:br]
       [:p "The odds against are " (large (- (js/Math.round (/ 1 p)) 1) " - 1") "."]
       [:section {:style {:font-size 16}}
        [:br]
        [ui/row
         [ui/col {:md 4} "Last updated"]
         [ui/col {:md 4} "00:00 6th July 2021"]]
        [ui/row
         [ui/col {:md 4}  "Next update due"]
         [ui/col {:md 4} "00:00 7th July 2021"]]]]]]))

(def subs-key
  "subscription keys by administrative level key"
  {:country [::subs/countries]
   :region [::subs/regions]
   :community [::subs/communities]})

(defn find-location-by-id
  "level will be an entity id for this level like :country, :region, :community.
   Returns nil if not found."
  [level id]
  (first (-> (->> level
                  (subs-key)
                  (rf/subscribe)
                  (deref)
                  :items
                  (group-by :id))
             (get id))))

(comment
  (find-location-by-id :country "italy")
  ;; => {:href :rise.views/countries, :title "Italy", :id "italy", :map "italy.png"}
  (find-location-by-id :region "umbria")
  ;; => {:href :rise.views/regions, :title "Umbria", :id "umbria", :map "umbria.png", :country "italy"}
  (find-location-by-id :community "spoleto")
  ;; => {:neighbours {:N "Spoleto-N", :NE "Spoleto-NE", :NW "Spoleto-NW", :S "Spoleto-S", :SE "Spoleto-SE", :SW "Spoleto-SW"}, :p-7day 0.022, :longitude 12.7376, :osm-href "https://www.openstreetmap.org/relation/42105", :title "Spoleto", :region "umbria", :mean-7day 1.5E-4, :id "spoleto", :latitude 42.739, :country "italy", :map "spoleto hex.png", :href :rise.views/hex}
  (find-location-by-id :community "nowhere")
  ;; => nil
  0)

(defn link-location-by-id
  "Returns an anchor component to a location, wrapping the location title (2 arity) or the supplied hiccup component (3 arity)."
  ([level id]
   (let [location (find-location-by-id level id)]
     [:a {:href (ui/href (location :href) {:id (location :id)})} (location :title)]))
  ([level id component]
   (let [location (find-location-by-id level id)]
     (if location
       [:a {:href (ui/href (location :href) {:id (location :id)})} component]
       nil))))

(comment
  (link-location-by-id :country "italy")
  ;; => [:a {:href "#/countries/italy"} "Italy"]

  (link-location-by-id :country "italy" [:i "Italy"])
  ;; => [:a {:href "#/countries/italy"} [:i "Italy"]]

  (link-location-by-id :region "nowhere")
  ;; => nil
  0)


(def map-arrow
  "Lookup the arrow shape based on hex direction."
  {:N [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #00ffff" :border-left "22px solid transparent"
                     :border-right "20px solid transparent" :position "absolute" :top "3%" :left "46%"}}]
   :NE [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #00ffff" :border-left "22px solid transparent"
                      :border-right "20px solid transparent" :position "absolute" :top "25%" :right "6%" :transform "rotate(60deg)"}}]
   :NW [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #00ffff" :border-left "22px solid transparent"
                      :border-right "20px solid transparent" :position "absolute" :top "23.5%" :left "7%" :transform "rotate(-60deg)"}}]
   :SE [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #00ffff" :border-left "22px solid transparent"
                      :border-right "20px solid transparent" :position "absolute" :bottom "25%" :right "6%" :transform "rotate(120deg)"}}]
   :SW [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #00ffff" :border-left "22px solid transparent"
                      :border-right "20px solid transparent" :position "absolute" :bottom "25%" :left "6%" :transform "rotate(-120deg)"}}]
   :S [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #00ffff" :border-left "22px solid transparent"
                     :border-right "20px solid transparent" :position "absolute" :bottom "3%" :left "46%" :transform "rotate(180deg)"}}]})

(defn link-neighbour
  "Link a location to "
  [level id direction]
  (when-let [location (find-location-by-id level id)]
    (js/console.log location ::location)
    (when-let [neighbour (get-in location [:neighbours direction])]
      (println neighbour ::neighbour)
      (link-location-by-id level neighbour (map-arrow direction)))))

(comment
  (enable-console-print!)
  (link-neighbour :community "spoleto" :N)
  0
  ;; => [:a {:href "#/hex/spoleto-N"} [:div {:style {:width "22px", :height "22px", :border-bottom "22px solid #ffffff88", :border-left "22px solid transparent", :border-right "20px solid transparent", :position "absolute", :top "3%", :left "46%"}}]]

  (link-neighbour :community "spoleto-NE" :S))

(def hex-compass-points
  "Points around a hexagon standing on its base"
  [:N :NE :NW :SE :SW :S])

(defn hex
  []
  (let [location (get-in @(rf/subscribe [::subs/current-route])
                         [:path-params :id])
        all-communities (group-by :id (:items @(rf/subscribe [::subs/communities])))
        community (first (all-communities location))
        all-regions (group-by :id (:items @(rf/subscribe [::subs/regions])))
        region-id (community :region)
        region (first (all-regions region-id))]
    (locals)
    [ui/page [:span (community :title) " (" [:a {:href (ui/href (region :href) {:id (region :id)})} (region :title)] ")"]
     [ui/row
      [ui/col {:md 3}
       [:div {:style {:position "relative" :display "flex"}}
        [:> bs/Image {:src (str "/assets/" location " hex.png")
                      :width "100%"
                      :fluid true}]
        ; decorate map with map arrow links
        [:div {:style {:position "absolute" :top 0 :left 0 :bottom 0 :right 0}}
         (into [:<>]
               (map
                #(link-neighbour :community (community :id) %)
                hex-compass-points))
         ]]
       
       [:> bs/Image {:src "/assets/M4scale.png"
                     :width "100%"
                     :fluid true}]]
      [ui/col {:md 7}
       (area-status community (community :p-7day) (community :mean-7day))]]]))



