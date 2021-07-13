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

;; Utils ;;

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

(defn mag-button
  ([mag color]
   (mag-button mag color "" false))
  ([mag color text]
   (mag-button mag color text false))
  ([mag color text bottom]
   [:div {:style {:display "flex"
                  :align-items "center"}}
    [:div {:style {:color color
                   :font-size (if (= color "#ACACAC") "1.2em" "1em")}} text]
    [:div {:style {:background-color color
                   :border "1px solid white"
                   :color "white"
                   :margin-left 10
                   :min-width "80px"
                   :height "5ex"
                   :display "flex"
                   :align-items "center"
                   :justify-content "center"}} "Mag " mag]]))

(defn mag-scale
  []
  [:<>
   [ui/row
    [ui/col {:style {:font-size "1.4em" }}
     "Showing chances of earthquakes magnitude 4 or above like these:"]]
   [ui/row {:class "d-flex flex-row justify-content-around"}
    [ui/col {:class "d-flex flex-column align-items-end justify-content-end"}
     (mag-button 9 "#6B5967")
     (mag-button 8 "#80647C")
     (mag-button 7 "#946E8C")
     (mag-button 6 "#B58283" "Emilia 2012, M=6.9")
     (mag-button 5 "#D2937A" "L'Aquila 2009, M=5.9")
     (mag-button 4 "#E7A174" "Ischia 2017, M=4")
     ;(mag-button 3 "#ACACAC" "")
     (mag-button "1-3" "#ACACAC" "Not included
                                  in dashboard")
     #_(mag-button 1 "#ACACAC" "" true)
     ]]])


;;; Views ;;;

(defn home-page
  "Display a generic home page. Minimally, navigation from here to countries."
  []
  (let [all-countries (:items @(rf/subscribe [::subs/countries]))]
  [ui/page "RISE Dashboard demo"
   [ui/row
    [ui/col {:md 5}
     [mag-scale]]
    [ui/col {:md {:span 5 :offset 1} :style {:font-size "1.4em"}}
     "Navigate to your local area."
     [links-to all-countries]]]]))

(defn info
  []
  [ui/page "Info"
   [ui/row
    [ui/col
     "An info page placeholder"]]])


(defn countries
  "A country overview with links to regions"
  []
  (let [country-id (get-in @(rf/subscribe [::subs/current-route])
                           [:path-params :id])
        all-countries (group-by :id (:items @(rf/subscribe [::subs/countries])))
        country (first (all-countries country-id))
        all-regions (:items @(rf/subscribe [::subs/regions]))
        country-regions (filter #(= (:country %) country-id) all-regions)]
    (locals)
    [ui/page (country :title)
     [ui/row
      [ui/col {:lg 3}
       [:> bs/Image {:src (str "/assets/" country-id ".png")
                     :width "100%"
                     :fluid true}]
       [mag-scale]]
      [ui/col {:lg 9}
       (links-to country-regions)]]]))


(defn regions
  "A regional page with map, links back to the country and down to communities"
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
      [ui/col {:lg 3}
       [:> bs/Image {:src (str "/assets/" region-id ".png")
                     :width "100%"
                     :fluid true}]
       [mag-scale]
       #_[:> bs/Image {:src "/assets/M4scale.png"
                     :width "100%"
                     :fluid true}]]
      [ui/col {:lg 9}
       (links-to regional-communities)]]]))

(defn large
  [& n]
  (into [:span {:style {:font-size "1.4em"}}]
        (map str n)))

(defn area-status
  "show earthquake status of an area"
  [area p mean]
  (let [mag+ @(rf/subscribe [::subs/mag+])
        left-style {:style {}}]
    [ui/row {:style {:font-size "21px"}}
     [ui/col 
      [:h1 {:style {:font-size "32px"}}"How likely is an earthquake in the next 7 days?"]
      [:div {:style {:border "1px solid #CCC"
                     :border-radius 20
                     :padding "15px 30px"
                     :box-shadow "1px 1px 1px 1px #CCC"}}
       [ui/row
        [ui/col {:md 9}
         [:div "The chance of a magnitude " mag+ " or above" [:br] "between 6th July – 13th July"]]
        [ui/col {:md 3}
         [:div [large (.toFixed (js/Number (* p 100)) 1) "%"]]]]
       [:hr]
       #_[:div {:style {:display "flex"
                        :justify-content "space-around"
                        :align-items "center"}}
          [:div left-style
           [:p "The chance of a magnitude " mag+ " or above between" [:br] " 6th July – 13th July is:"]]
          [:div [large (.toFixed (js/Number (* p 100)) 1) "%"]]]

       [ui/row {:style {:display "flex" :align-items "center" :padding-bottom 15}}
        [ui/col {:md 9}
         [:span "The chance in an average week"]]
        [ui/col {:md 3}
         [:div [large (.toFixed (js/Number (* mean 100)) 3) "%"]]]]
       [:hr]
       #_[:div {:style {:display "flex"
                        :justify-content "space-around"
                        #_#_:align-items "center"}}
          [:p "This is 147 times the chance in an average week"]
          [:div [large (.toPrecision (js/Number (* (/ p 147) 100)) 2) "%"]]]
       [ui/row {:style {:display "flex" :align-items "center" :padding-bottom 15}}
        [ui/col {:md 9}
         [:span "The odds against are"]]
        [ui/col {:md 3}
         [:div (large (- (js/Math.round (/ 1 p)) 1) " - 1")]]]

       ]
      
      [:div {:style {:font-size 16
                     :margin-left 45
                     :color "#888"}}
       [ui/row
        [ui/col {:xs 6} "Last updated"]
        [ui/col {:xs 6} "00:00 6th July 2021"]]
       [ui/row
        [ui/col {:md 6}  "Next update due"]
        [ui/col {:md 6} "00:00 7th July 2021"]]]

      [ui/row 
       [ui/col {:md 12 :style {:font-size 16
                               :display "flex"
                               :padding 45
                               :justify-content "space-around"
                               :align-items "center"}}
        [:p [:i "The area is seeing higher chances than normal because of increased 
             seismic activity around the Mount Vittore fault system."]]]]]]))

(defn arrow-template
  "a parameterised direction arrow"
  [{:keys [top right bottom left deg]}]
  [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #00ffff" :border-left "22px solid transparent"
                 :border-right "20px solid transparent" :position "absolute"
                 :top (when top top) :right (when right right) :bottom (when bottom bottom) :left (when left left)
                 :transform (str "rotate(" deg "deg)")}}])

(def map-arrow
  "A map of arrows by hex direction."
  {:N (arrow-template {:top "3%" :left "43%" :deg 0})
   :NE (arrow-template {:top "25%" :right "6%" :deg 60})
   :NW (arrow-template {:top "23.5%" :left "6%" :deg -60})
   :SE (arrow-template {:bottom "25%" :right "6%" :deg 120})
   :SW (arrow-template {:bottom "25%" :left "6%" :deg -120})
   :S (arrow-template {:bottom "3%" :left "43%" :deg 180})})

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
  "A community-level page featuring a mapped hexagon."
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
      [ui/col {:lg 3}
       [mag-scale]]
      [ui/col {:lg 6}
       (area-status community (community :p-7day) (community :mean-7day))]
      [ui/col {:lg 3}
       [:div {:style {:position "relative" :display "flex"}}
        [:> bs/Image {:src (str "/assets/" location " hex.png")
                      :width "100%"
                      :fluid true}]
        ; decorate map with map arrow links
        [:div {:style {:position "absolute" :top 0 :left 0 :bottom 0 :right 0}}
         (into [:<>]
               (map
                #(link-neighbour :community (community :id) %)
                hex-compass-points))]]]]]))



