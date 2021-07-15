(ns rise.views
  (:require
   [clojure.string :as string]
   [re-frame.core :as rf]
   ["react-bootstrap" :as bs]
   [reagent.core :as r]
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
   (mag-button mag color ""))
  ([mag color text]
   ;(mag-button mag color text)
   [:div {:style {:display "flex"
                  :align-items "center"}}
    [:div {:style {:color (if (= color "#ACACAC") color "black")
                   :font-size (if (= color "#ACACAC") "1.2em" "1em")}} text]
    [:div {:style {:background-color color
                   :border "1px solid white"
                   :border-top "none"
                   :color "white"
                   :margin-left 0
                   :width "80px"
                   :height "6ex"
                   :display "flex"
                   :align-items "center"
                   :justify-content "center"}} "Mag " mag]]))

(defn mag-scale
  []
  [:<>
   #_[ui/row
      [ui/col {:style {:font-size "1.4em"}}
       "Showing chances of earthquakes magnitude 4 or above like these:"]]
   [ui/row {:class "d-flex flex-row justify-content-end"}
    [:div {:style {:position "absolute"
                   ;:border "1px solid green"
                   :width 100
                   }}
     [:svg {:viewbox "0 0 100 200" 
            :style {:position "relative" 
                    :right 0
                    :width "10" :height "20"}}
      [:rect {:x 0 :y 10 :height 400 :width 100 :fill "red" :line-width 10 }]]]
    
    [:div {:class "d-flex flex-column align-items-end justify-content-end"}
     [:div {:style {:width  0
                    :height 0
                    :border-style "solid" ;
                    :border-width "0 40px 15px 40px" ;
                    :border-color " transparent transparent #6b5967 transparent"}};
      ]
     (mag-button 9 "#6B5967")
     (mag-button 8 "#80647C")
     (mag-button 7 "#946E8C")
     (mag-button 6 "#B58283" #_"L'Aquila 2009, M=6.1")
     (mag-button 5 "#D2937A" #_"Emilia 2012, M=5.8")
     (mag-button 4 "#E7A174" #_"Amatrice (RI) 2017 M=4.3")
     [:div {:style {:margin-top 20}}]
     #_(mag-button "1-3" "#ACACAC" "Not included
                                  in dashboard")]]])


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
     [ui/three-columns
      {:col1 [mag-scale]
       :col2 [:<> [:h2 "Italian Regions"]
              (links-to country-regions)]
       :col3 [:> bs/Image {:src (str "/assets/" country-id ".png")
                           :width "100%"
                           :fluid true}]}]]))


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
     [ui/three-columns
      {:col1 [mag-scale]
       :col2 [:div {:style {:margin-left 15}} [:h2 "Regional Communities"]
              (links-to regional-communities)]
       :col3 [:> bs/Image {:src (str "/assets/" region-id ".png")
                           :width "100%"
                           :fluid true}]}]]))

(defn large
  [& n]
  (into [:span {:style {:font-size "1.3em"}}]
        (map str n)))

(def p 0.22)

(defn bar
  [p]
  [:div
   [:div {:style {:border "1px inset #CCC"
                  :background-color "white"
                  :height 16
                  :position "relative"
                  :width "100%"}}
    [:div {:style {:background-color #_"#2177FF" "#444466"
                   :position "absolute"
                   :padding "0px 0px"
                   :height 8
                   :left 0 :top 3
                   :width (str (* p 100) "%")}}]]
   [:div {:style {:display "flex" :justify-content "space-between" :width "100%" :font-size "0.8em" :color "#BCBCCC"}}
    [:span "0%"] [:span "100%"]]])

(defn area-status
  "show earthquake status of an area"
  [area p mean]
  (let [mag+ @(rf/subscribe [::subs/mag+])
        animate? @(rf/subscribe [::subs/animate?])]
    [ui/row {:style {:font-size "21px"}}
     [ui/col 
      [:div {:style {:border "1px solid #CCC"
                     :border-radius 20
                     :padding "15px 30px"
                     :box-shadow "1px 1px 1px 1px #CCC"
                     :background-color "#444466" #_"#80647D"
                     :color "white"}}
       [ui/row
        [ui/col {:md 9}
         [:div  "The chance of an earthquake" [:br] [:nobr "within 6th July <———> 13th July"]]]
        [ui/col {:md 3}
         [:div [large (.toFixed (js/Number (* p 100)) 1) "%"]]]]
       [ui/row {:style {:margin-top 5}}
        [ui/col
         [bar p]]]
       [ui/row {:style {:margin-top 5}}
        [ui/col
                  [:div {:style {:margin-bottom 6}} "compared to"]
         [bar mean]]]
       ;[:br]
       ;[:hr]

       [ui/row {:style {:display "flex" :align-items "center" :padding-bottom 15}}
        [ui/col {:md 9}
         [:span "the chance in an average week"]]
        [:br]
        [ui/col {:md 3 }
         [:div [large (.toFixed (js/Number (* mean 100)) 3) "%"]]]]
       
       [:br]
       ;[:hr]

       [ui/row {:style {:display "flex" :align-items "center" :padding-bottom 15}}
        [ui/col {:md 9}
         [:span "The odds against an earthquake"]]
        [ui/col {:md 3}
         [:nobr [:div (large (- (js/Math.round (/ 1 p)) 1) " - 1")]]]]]
      
      [:div {:style {:font-size 16
                     :margin-left 30
                     :color "#888"}}
       [ui/row {:style {:width 370}}
        ;[:span  "Last updated"]
        ;[:span "00:00 6th July 2021"]
        [ui/col {:xs 5} "Last updated"]
        [ui/col {:xs 6} "00:00 6th July 2021"]
        ]
       [ui/row {:style {:width 370}}
        [ui/col {:xs 5} "Next update due"]
        [ui/col {:xs 6} "00:00 7th July 2021"]]]

      [ui/row 
       [ui/col {:md 12 :style {:font-size 16
                               :display "flex"
                               :flex-direction "column"
                               :padding 45
                               :align-items "flex-start"
                               :justify-content "space-around"
                               }}
        [:h4 "What's happening?"]
        [:p [:i "The area is seeing higher chances than normal because of increased 
             seismic activity around the Mount Vittore fault system."]]]]]]))

(defn arrow-template
  "a parameterised direction arrow"
  [{:keys [top right bottom left deg]}]
  [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #444466" #_"#AA647D" :border-left "22px solid transparent"
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
  "Points around a hexagon standing on its base. 
   Order will be retained up to 32 points only"
  (keys map-arrow)
  )



(defn timer-component []
  (let [seconds-elapsed (r/atom 0)]
    (fn []
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [:div
       "Seconds Elapsed: " @seconds-elapsed])))

(defn hex
  "A community-level page featuring a mapped hexagon."
  []
  (let [location (get-in @(rf/subscribe [::subs/current-route])
                         [:path-params :id])
        all-communities (group-by :id (:items @(rf/subscribe [::subs/communities])))
        community (first (all-communities location))
        all-regions (group-by :id (:items @(rf/subscribe [::subs/regions])))
        region-id (community :region)
        region (first (all-regions region-id))
        animate? @(rf/subscribe [::subs/animate?])
        quake? true
        ]
    (locals)
    [ui/page
     [:<>
      [ui/row {:style {:margin-bottom 20 :display "flex" :align-items "flex-end"}}
       [ui/col {:md 3 :style {:display "inline-block" :font-size "2.2em" :font-weight  "500"}}
        [:nobr (community :title)] " (" [:a {:href (ui/href (region :href) {:id (region :id)})} (region :title)] ")"]
       [ui/col {:md 8 :style {:display "inline-block" :font-size "2em" :font-weight  "500"}}
        "How likely is a " [:i "magnitude 4 or above"] " earthquake" [:br] " within the next 7 days?"]]]
     [ui/three-columns
      {:col1 [:div {:style {:margin-top 20}} [mag-scale]]
       :col2 [:div {:style {:margin-left 15}} (area-status community (community :p-7day) (community :mean-7day))]
       :col3 [:<> [:div {:style {:position "relative" :display "flex"}
                         :class-name (when quake? "shake")}
                   [:> bs/Image {:src (str "/assets/" location " hex.png")
                                 :width "100%"
                                 :fluid false}]
               ; decorate map with map arrow links
                   [:div {:style {:position "absolute" :top 0 :left 0 :bottom 0 :right 0}}
                    (into [:<>]
                          (map
                           #(link-neighbour :community (community :id) %)
                           hex-compass-points))]]
              (when animate?
                [:div {:style {:display "flex" :align-items "start" :margin-top 15}}
                 [:div {:style {:width 20
                                :height 20
                                :display "inline-block"
                                :border "3px solid #ACACAC"
                                :border-radius 10}}]
                 [:div {:style {:padding-left 15 :display "inline-block"}}
                  "Every beat is a possible future 7 days"]])]}

      #_[ui/row
       [ui/col {:lg 3 :style {:max-width 500 :margin-bottom 30}}
        [mag-scale]]
       [ui/col {:lg 6 :style {:max-width 600}}
        (area-status community (community :p-7day) (community :mean-7day))]
       [ui/col {:lg 3 :style {:max-width 500}}
        [:<>
         [:div {:style {:position "relative" :display "flex"}
                :class-name (when quake? "shake")}
          [:> bs/Image {:src (str "/assets/" location " hex.png")
                        :width "100%"
                        :fluid false}]
        ; decorate map with map arrow links
          [:div {:style {:position "absolute" :top 0 :left 0 :bottom 0 :right 0}}
           (into [:<>]
                 (map
                  #(link-neighbour :community (community :id) %)
                  hex-compass-points))]]
         (when animate?
           [:div {:style {:display "flex" :align-items "start" :margin-top 15}}
            [:div {:style {:width 20
                           :height 20
                           :display "inline-block"
                       ;:background-color "#ACACAC"
                           :border "3px solid #ACACAC"
                           :border-radius 10}}]
            [:div {:style {:padding-left 15 :display "inline-block"}}
             "Every beat is a possible future 7 days"]])]]]]])

)




