(ns rise.views
  (:require
   [clojure.string :as string]
   [clojure.edn :as edn]
   [re-frame.core :as rf]
   ["react-bootstrap" :as bs]
   [reagent.core :as r]
   [rise.subs :as subs]
   [rise.events :as events]
   [rise.ui :as ui]
   [rise.bsio :as bsio]
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
                   :padding-left 15
                   :padding-right 25
                   :font-size (if (= color "#ACACAC") "1.2em" "1em")}} text]
    [:div {:style {:background-color color
                   :border "1px solid white"
                   :border-top "none"
                   :color "white"
                   :margin-left 0
                   :min-width "80px"
                   :height "6ex"
                   :display "flex"
                   :align-items "center"
                   :justify-content "center"}} "Mag " mag]]))

(defn mag-y
  "Linear map so mag-y 10 -> 0 and mag-y 4 -> 302 (the height of the mag scale)"
  [mag] (/ (* (- 10 mag) 302) 6))
(comment
  (mag-y 4)
  ;; => 302

  (mag-y 5)
  ;; => 251.66666666666666

  (mag-y 10)
  ;; => 0
  )

(defn example-quake
  "example quake text and arrow"
  [text slot mag color]

  [:g {:style {:width "100%"}}
   [:line {:x1 "85%" :x2 "100%" :y1 (mag-y slot) :y2 (mag-y mag) :stroke color :stroke-width 2}]
   [:circle {:cx "100%" :cy (mag-y mag) :r 4 :fill color}]
   [:rect {:x 5 :width "calc(100% -  30px)" :y (- (mag-y slot) 12) :height 22 :rx 10 :fill "#fff" :stroke-width 1 :stroke color}]
   [:text {:x 10 :y (+ 4 (mag-y slot)) :fill color} text]]
  )

(defn mag-scale
  "Draw magnitude scale with example quakes on it"
  []
  #_(let [w @(rf/subscribe [::subs/window-width])])
  [:<>

   [ui/row {:class "d-flex flex-row justify-content-end"}
    [:div {:style {:position "absolute"
                   ;:border "1px solid green"
                   :width #_(/ w 4) "100%"}}
     [:svg {;:viewbox "0 0 100 200"
            :width "calc(100% - 80px)"
            :style {:position "relative"
                    :right 0
                    :margin-top 15

                    :height "302"
                    #_#_:border "1px solid red"}}

      (example-quake "Norcia 2016 M=6.5" 6.7 6.5 "#666")
      (example-quake "L'Aquila 2009 M=6.1" 6.1 6.1 "#666")
      (example-quake "Emilia 2012 M=5.8" 5.5 5.8 "#666")
      (example-quake "Amatrice 2017 M=4.3" 4.5 4.3 "#666")]]


    [:div {;:style {:margin-right 15}
           :class "d-flex flex-column align-items-end justify-content-end"}
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
     (mag-button 4 "#E7A174" #_"Amatrice (RI) 2017 M=4.3")]]
   [ui/row {:class "d-flex flex-row justify-content-end"}
    [:div {:class "d-flex flex-column align-items-end justify-content-end"
           :style {:margin-top 20}}
     (mag-button "1-3" "#ACACAC" "Not included
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

(defn option-radio-group
  [id key-name on-text off-text]
  [ui/row
   [ui/col {:md 6}
    [bsio/radio-button-group {:id id
                              :value-k (keyword key-name)
                              :value-f #(if @(rf/subscribe [(keyword :rise.subs key-name)]) "on" "off")
                              :on-change #(rf/dispatch [(keyword :rise.events key-name) (= % "on")])
                              :buttons-f (fn [] [{:key (keyword key-name) :level "on" :level-name on-text}
                                                 {:key (str "not" (keyword key-name)) :level "off" :level-name off-text}])}]]])

(defn settings
  "Page to set options"
  []
  [ui/page [:h1 "Set Options"]
   [:div {:style {:height 500 :width 200 :display "flex" :flex-direction "column" :justify-content "space-between"}}
    [option-radio-group "odds?" "odds?" "Show Odds" "Show relative risk" ]
    [option-radio-group "setcontext" "with-context?" "With contextual pages" "Without contextual pages"]
    [option-radio-group "setvis" "with-vis?" "With visualisation" "Without visualisation"]
    [option-radio-group "annular" "annular?" "Show speedo?" "Show bar?"]

    [ui/row
     [ui/col {:md 3}
      [:> bs/Button {:href (ui/href :rise.views/hex {:id "spoleto"})}
       "Start"]]]]])

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
    [ui/page [:h1 (country :title)]
     [ui/three-columns
      {:col1 [mag-scale]
       :col2 [:div {:style {:margin-left 30}}
              [:h2 "Italian Regions"]
              (links-to country-regions)]
       :col3 [:> bs/Image {:src (str "/assets/" country-id ".png")
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
    [ui/page [:div {:style {:margin-left 30}}
              [:h1 [:span (region :title) " (" [:a {:href (ui/href (country :href) {:id (country :id)})} (country :title)] ")"]]]
     [ui/three-columns
      {:col1 [mag-scale]
       :col2 [:div {:style {:margin-left 30}} [:h2 "Regional Communities"]
              (links-to regional-communities)]
       :col3 [:> bs/Image {:src (str "/assets/" region-id ".png")
                           :fluid true}]}]]))

(defn large
  [& n]
  (into [:span {:style {:font-size "1.3em"}}]
        (map str n)))

;(def p 0.22)

(defn bar
  [{:keys [p r w text]}]

  [:div {:style {:width 100 :height 100 :display "flex" :flex-direction "column" :justify-content "space-between" :align-items "center"}}
   (when text  [:div {:style {:margin "auto auto" :font-size "1.4em"}} text])
   (when @(rf/subscribe [::subs/with-vis?])
     (when-not @(rf/subscribe [::subs/annular?])
       [:<>
        [:div {:id "bar-vis"
               :style {:background-color "#ffffff88"
                       :height 16
                       :position "relative"
                       :width "100px"}}
         [:div {:style {:background-color "#fff"
                        :position "absolute"
                        :padding "0px 0px"
                        :height "100%"
                        :left 0 :top 0
                        :width (str (* p 100) "%")}}]]
        [:div {:style {:display "flex" :justify-content "space-between" :width "100%" :font-size "0.8em" :color "#BCBCCC"}}
         [:span "0%"] [:span "100%"]]]))])

  (defn arc
    "Return an arc. p is a fraction of a turn, r is the radius at the centre of the arc, w is the width of the arc
   To get a pie chart the whole circle and not just the annulus, we need (= w r).
   "
    [{:keys [p r w text]}]
    (let [cr (- r (/ w 2))
          d (* 2 r)
          dash (* js/Math.PI cr p)
          gap (- (* 2 js/Math.PI cr) (* 2 dash))]

      [:div {:style {:width d :height d}}
       [:div {:style {:display "flex" :direction "row" :justify-content "center" :align-items "center"}}
        (when @(rf/subscribe [::subs/with-vis?])
          [:div {:style {:width 0}}
           [:svg {:width d :height d}
            [:g
             [:circle {:cx r :cy r :r cr :fill "none" :stroke "#ffffff88" :stroke-width w}]
             [:circle {:cx r :cy r :r cr :fill "none" :stroke "#ffffff" #_"#327bff" :stroke-width w
                       :stroke-dasharray (str dash " " gap " " dash)
                       :style {:transform "rotate(-90deg)"
                               :transform-origin "50% 50%"}}]]]])
        (when text  [:div {:style {:margin "auto auto"}} text])]]))

(defn trim-s
  "Given s as a string representation of an integer or simple decimal (not E notation!), 
   trim whitespace and remove trailing zeroes. If a simple integer trim and drop leading zeroes."
  [s]
  (let [small-decimal (< (edn/read-string s) 1)
        zero-s? #(= "0" %)
        drop-zeroes #(drop-while zero-s? %)]
    (if small-decimal
      (->> s
           string/trim
           reverse
           drop-zeroes
           reverse
           string/join)
      (->> s string/trim drop-zeroes string/join))))

(comment
  (trim-s "0.01500")
  ;; => "0.015"
  (trim-s "01500")
  ;; => "1500"
  (trim-s "01500.30")
  ;; => "1500.30"
  0)

(trim-s "0.01500")

(defn nice% 
  "format a percentage nicely"
  [p]
  (str (trim-s (.toPrecision (js/Number. (* p 100)) 2)) "%")
  )

(defn vis%
  "An arc with p rendered as a percentage at the centre"
  [p]
  (let [annular? @(rf/subscribe [::subs/annular?])
        with-vis? @(rf/subscribe [::subs/with-vis?])
        params {:p p
                :r 55
                :w 12
                :b 3
                :text (nice% p)}]
    (if (and annular? with-vis?)
      [arc params]
      [bar params])))

(defn content-base
  "The bottom of the page, occupying cols 1, 2 and 3"
  [community]
  (let [with-context? @(rf/subscribe [::subs/with-context?])
        region (find-location-by-id :region (community :region))
        base-style (fn [md] {:md md :style {:font-size 16
                                            :display "flex"
                                            :flex-direction "column"
                                            :padding 45
                                            :align-items "flex-start"
                                            :justify-content "space-between"}})]
    [ui/row
     [ui/col (base-style 3)
      (when with-context?
        [:<>
         [:h4 [:a {:href (str "/#/history/" (community :id))} "Local earthquake history"]]
         [:p "How many earthquakes of magnitude 4 or more have hit " (community :title) " in the past?"]])]
     [ui/col (base-style 5)
      [:h4 [:a {:href (str "/#/hex/" (community :id))} "What's happening here and now?"]]
      [:p (community :title) " is seeing higher chances than normal because of increased 
             seismic activity around the Mount Vittore fault system."]]
     [ui/col (base-style {:span 4})
      (when with-context?
        [:<>
         [:h4 [:a {:href (str "/#/world/" (community :id))} "How does " (community :title) " compare to the world?"]]
         [:p "How does the current chance of a magnitude 4+ quake in " (community :title)
          " compare to an average week in other places worldwide?"]])]]))

(defn update-status
  []
  [:div {:style {:font-size 16
                 :margin-left 30
                 :color "#888"}}

   [ui/row {:style {:width 370}}
        ;[:span  "Last updated"]
        ;[:span "00:00 6th July 2021"]
    [ui/col {:xs 5} "Last updated"]
    [ui/col {:xs 6} "00:00 6th July 2021"]]

   [ui/row {:style {:width 370}}
    [ui/col {:xs 5} "Next update due"]
    [ui/col {:xs 6} "00:00 7th July 2021"]]])

(defn area-status
  "show earthquake status of an area"
  [community]
  (let [p (community :p-7day)
        mean (community :mean-7day)
        mag+ @(rf/subscribe [::subs/mag+])
        animate? @(rf/subscribe [::subs/animate?])
        with-vis? @(rf/subscribe [::subs/with-vis?])
        odds? @(rf/subscribe [::subs/odds?])]
    [ui/row {:style {:font-size "21px"}}
     [ui/col
      [:div {:style {:border "1px solid #CCC"
                     :border-radius 20
                     :padding (str (if with-vis? 15 30) "px 30px")
                     :box-shadow "1px 1px 1px 1px #CCC"
                     :background-color "#444466" #_"#80647D"
                     :color "white"}}
       [ui/row {:style {:display "flex" :align-items "center" :justify-content "space-between" 
                        :padding-bottom (when with-vis? 25)}}
        [:<> ;ui/col {:md 9}
         [:div  "The chance of an earthquake" [:br] [:nobr "within 6th July ⟷ 13th July is"]]]
        [:<> ;ui/col {:md 3}
         [vis% p]
         #_[:div [large (.toFixed (js/Number (* p 100)) 1) "%"]]]]
       #_[ui/row {:style {:margin-top 5}}
          [ui/col

           #_[bar p]]]
       #_(if with-vis?
           [ui/row {:style {:margin-top 5}}
            [ui/col
             #_[:div {:style {:margin-bottom 6}} "compared to"]
             #_[bar mean]]]
           [:br])
       ;[:br]
       ;[:hr]

       [ui/row {:style {:display "flex" :align-items "center" :justify-content "space-between" :padding-bottom 35}}
        [:<> ;ui/col {:md 9}
         [:span "whereas the chance in an average week is"]]
        ;[:br]
        [:<> ;ui/col {:md 3}
         [vis% mean]
         #_[:div [large (.toFixed (js/Number (* mean 100)) 3) "%"]]]]

       ;[:br]
       ;[:hr]

       [ui/row {:style {:display "flex" :align-items "center" :justify-content "space-between" :padding-bottom 25}}
        (if odds?
          [:<>
           [:span "The odds against an earthquake are"]
           [:nobr [:div {:style {:width 85}} (large (- (js/Math.round (/ 1 p)) 1) " - 1")]]]
          [:<>
           [:span "The current chance is " (large (let [rr (/ p mean)]
                                            (str (if (> rr 1)
                                                   (js/Math.round rr) ;(* 10 (js/Math.round (/ rr 10)))
                                                   (trim-s (.toPrecision (js/Number. (* rr 100)) 2)))))) " times " 
            (condp = (compare p mean)
              -1 "smaller than"
              0 "equal to"
              1 "higher than")
            " average."]
           ]
          )]]

      [update-status]]]))

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
  (keys map-arrow))

(defn timer-component []
  (let [seconds-elapsed (r/atom 0)]
    (fn []
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [:div
       "Seconds Elapsed: " @seconds-elapsed])))



(defn main-content-template
  "Three standard columns with injected title and main central panel
   page-title is a page-title function which takes the community as a parameter
   content is a content component taking the community as parameter"
  [page-title animate? content]
  (let [location (get-in @(rf/subscribe [::subs/current-route])
                         [:path-params :id])
        all-communities (group-by :id (:items @(rf/subscribe [::subs/communities])))
        community (first (all-communities location))
        all-regions (group-by :id (:items @(rf/subscribe [::subs/regions])))
        region-id (community :region)
        region (first (all-regions region-id))
        quake? false]
    (locals)
    [ui/page
     [:<>
      [ui/row {:style {:margin-bottom 20 :display "flex" :align-items "flex-end"}}
       [ui/col {:md 3 :style {:display "inline-block" :font-size "2.2em" :font-weight  "500"}}
        [:nobr (community :title)] " (" [:a {:href (ui/href (region :href) {:id (region :id)})} (region :title)] ")"]
       [ui/col {:md 8 :style {:display "inline-block" :font-size "2em" :font-weight  "500"}}
        [page-title community]]]
      [ui/three-columns
       {:col1 [:div {:style {:margin-top 20}} [mag-scale]]
        :col2 [:div {:style {:margin-left 15}} (content community)]
        :col3 [:<> [:div {:style {:position "relative" #_#_:display "flex"}
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
                 [:div {:style {:display "flex" :align-items "start" :margin-top 15}}])]}]
      [content-base community]]]))

(defn hex
  "A community-level page featuring a mapped hexagon."
  []
  (main-content-template
   (fn [community] [:<> "How likely is a " [:i "magnitude 4 or above"] " earthquake" [:br] " within the next 7 days?"])
   true
   area-status))

;;;
;;
;;;
(defn histogram
  [community]
  [ui/row {:style {:font-size "21px"}}
   [ui/col
    [:div {:style {:border "1px solid #CCC"
                   :border-radius 20
                   :display "flex"
                   :flex-direction "column" 
                   :padding 20
                   :justify-content "space-between"
                   :align-items "center"
                   :box-shadow "1px 1px 1px 1px #CCC"
                   :background-color "#444466" #_"#80647D"
                   :color "white"
                   :height 355}}
     [:div {:style {:text-align "center"}}
      "How many earthquakes of magnitude 4 or morehit " (community :title) " in each 50 year period?"]
     [:div {:style {:max-width 450}}
      [:> bs/Image {:src "/assets/history.png"
                    :alt "History of quakes in the area"
                    :fluid true}]]]]])

(defn history
  "Showing the earthquake history for an area"
  []
  (main-content-template
   (fn [community]
     (let [region (find-location-by-id :region (community :region))]
       [:span  "Mag 4+ earthquakes in " (community :title) " over time"]))
   true
   histogram))


;;;
;;
;;;
(defn world-averages
  [community]
  (let [X #(+ 10 (* % 2))
        dx -4
        p (community :p-7day)
        pc (fn [x] (str x "%"))
        community-x (X (* 100 p))]
    [ui/row {:style {:font-size "21px"}}
     [ui/col
      [:div {:style {:display "relative"
                     :border "1px solid #CCC"
                     :border-radius 20
                     :padding "0px 0px"
                     :box-shadow "1px 1px 1px 1px #CCC"
                     :background-color "#444466" #_"#80647D"
                     :color "white"
                     :height 360}}
       [:svg {:width "100%" :height "100%"}
        [:g
         [:text {:x "8%" :y "12%" :fill "#fff"} "Chance of a magnitude 4 or "]
         [:text {:x "8%" :y "19%" :fill "#fff"} "more within the next 7 days"]
         [:text {:style {:font-size "1.5em"} :x (pc (+ community-x dx)) :y "33%" :fill "#fff"} (nice% p) " in " (community :title)]
         [:line {:x1 (pc community-x) :x2 (pc community-x) :y1 "36%" :y2 "50%" :stroke "#fff" :stroke-width 2}]
         [:circle {:cx (pc community-x) :cy "50%" :r 5 :fill "#fff"}]
         (into [:g
                [:line {:x1 "10%" :x2 "90%" :y1 "50%" :y2 "50%" :stroke "#fff" :stroke-width 3}]]
               (map (fn [tick]
                      (let [x* (X tick)
                            x (str x* "%")
                            dx (str (- x* 2) "%")]
                        [:g
                         [:line {:x1 x :x2 x
                                 :y1 "47%" :y2 "53%"
                                 :stroke "#fff" :stroke-width 3}]
                         [:text {:x dx :y "46%" :fill "#fff8"} (str tick "%")]]))
                    (range 0 50 10)))
         [:text {:x "8%" :y "95%" :fill "#fff8"} "Tokyo"]]]

       #_[:> bs/Image {:src "/assets/thermometer.png"
                     :alt "Map showing forecast area"
                     :fluid true}]]]]))

(defn world
  "Showing community relative to world averages"
  []
  (main-content-template
   (fn [community] [:span "How does " (community :title) " compare to the world?"])
   true
   world-averages))






