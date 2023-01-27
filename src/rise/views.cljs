(ns rise.views
  (:require
   [clojure.string :as string]
   [clojure.edn :as edn]
   [re-frame.core :as rf]
   ["react-bootstrap" :as bs]
   [reagent.core :as r]
   [reagent.format :as ref]
   [rise.subs :as subs]
   [rise.events :as events]
   [rise.ui :as ui]
   [rise.bsio :as bsio]
   [rise.db :as db]
   [shadow.debug :refer [locals ?> ?-> ?->>]]
   [goog.string :as gstr]
   [goog.string.format :as gsf])
  )


(comment
  @(rf/subscribe [::subs/lang])
  (db/ttt :db/Dashboard "Earthquake dashboard")
  (gstr/format "Cost: %4.2f" 100234)
  (ref/format  "Cost: %05d" 100)
  ()
  0
  )


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
           (let [title (db/maybe-translatable (item :title))]
             [:div  {:key (item :id)}
              (if (item :href)
                [:a {:href (ui/href (item :href) {:id (item :id)})}
                 title #_(item :title)]
                title #_(item :title))]))
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
    [:div {:style {:background-color color
                   :border "1px solid white"
                   :border-top "none"
                   :color "white"
                   :margin-left 0
                   :min-width "80px"
                   :height "6ex"
                   :display "flex"
                   :align-items "center"
                   :justify-content "center"}} [:span mag]]
    [:div {:style {:color (if (= color "#ACACAC") color "black")
                   :padding-left 15
                   :padding-right 25
                   :font-size (if (= color "#ACACAC") "1.2em" "1em")}} text]]))

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
   [:line {:x1 "79px" :x2 "99px" :y1 (mag-y mag) :y2 (mag-y slot) :stroke color :stroke-width 2}]
   [:circle {:cx "79px" :cy (mag-y mag) :r 4 :fill color}]
   [:rect {:x 90 :width "calc(100% -  95px)" :y (- (mag-y slot) 12) :height 22 :rx 10 :fill "#fff" :stroke-width 1 :stroke color}]
   [:text {:x 95 :y (+ 4 (mag-y slot)) :fill color} text]])

(defn mag-scale
  "Draw magnitude scale with example quakes on it"
  []
  #_(let [w @(rf/subscribe [::subs/window-width])])
  [:<>

   [ui/row {:class "d-flex flex-row justify-content-start"}
    [:div {:style {:position "absolute"
                   ;:border "1px solid green"
                   :width #_(/ w 4) "100%"}}
     [:svg {;:viewbox "0 0 100 200"
            :width "calc(100% - 80px)"
            :style {:position "relative"
                    #_#_:right 0
                    :left 0
                    :margin-top 15

                    :height "302"
                    #_#_:border "1px solid red"}} 
      (example-quake "Norcia 2016 M=6.5" 6.7 6.5 "#666")
      (example-quake "L'Aquila 2009 M=6.1" 6.1 6.1 "#666")
      (example-quake "Emilia 2012 M=5.8" 5.5 5.8 "#666")
      (example-quake "Amatrice 2017 M=4.3" 4.5 4.3 "#666")]]


    [:div {:class "d-flex flex-column align-items-start justify-content-center"}
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
   [ui/row {:class "d-flex flex-row justify-content-start"}
    [:div {:class "d-flex flex-column align-items-start justify-content-start"
           :style {:margin-top 20}}
     (mag-button "1-3" "#ACACAC" (db/ttt :db/Not-included "Not included in dashboard"))]]])


;;; Views ;;;

(defn home-page
  "Display a generic home page. Minimally, navigation from here to countries."
  []
  (let [all-countries (:items @(rf/subscribe [::subs/countries]))]
    [ui/page ""
     [ui/row 
      [ui/col {:md {:span 6 :offset 1} :style {:font-size "1.4em"}}
       (db/ttt :db/Navigate "Navigate to your local area.")
       [links-to all-countries]]
      [ui/col {:md 5} 
       [:p {:style {:text-align "center"}} [:b (db/ttt :db/Magnitude "Magnitude")]]
       [:div {:style {:margin-left 10}}
        [mag-scale]]]]]))

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
(defn language-choice
  [id]
  [ui/row
   [ui/col {:md 6}
    [bsio/radio-button-group {:id id
                              :value-k :lang
                              :value-f #(name @(rf/subscribe [::subs/lang]))
                              :on-change #(rf/dispatch [::events/set-language (keyword %)])
                              :buttons-f (fn [] [{:key :en :level "en" :level-name "English"}
                                                 {:key :it :level "it" :level-name "Italian"}
                                                 {:key :de :level "de" :level-name "German"}
                                                 {:key :fr :level "fr" :level-name "French"}
                                                 ])}]]])
(defn settings
  "Page to set options"
  []
  [ui/page [:h1 "Set Options"]
   [:div {:style {:height 550 :width 200 :display "flex" :flex-direction "column" :justify-content "space-between"}}
    [:span [:label "Language choice"]
     [language-choice "set-language"]]
    [:br]
    [option-radio-group "odds?" "odds?" "Show Odds" "Show relative risk"]
    [option-radio-group "setcontext" "with-context?" "With contextual pages" "Without contextual pages"]
    [option-radio-group "setvis" "with-vis?" "With visualisation" "Without visualisation"]
    [option-radio-group "annular" "annular?" "Show speedo" "Show bar"]
    [option-radio-group "animate" "animate?" "Show animation" "No animation"]

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
    [ui/page [:h1 (apply db/ttt (country :title))]
     [ui/three-columns
      {:col3 [:div {:style {:display "flex"
                            :flex-direction "column"
                            :justify-content "center"}}
              [:p {:style {:text-align "center"}} [:b (db/ttt :db/Magnitude "Magnitude")]]
              [:div {:style {:margin-left 15}}
               [mag-scale]]]
       :col2 [:div {:style {:margin-left 30}}
              [:h2 (get (db/ttt :db/Country-regions "English Regions") country-id)]
              (links-to country-regions)]
       :col1 [:> bs/Image {:src (str "/assets/" country-id ".png")
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
              [:h1 [:span (db/maybe-translatable (region :title)) " (" [:a {:href (ui/href (country :href) {:id (country :id)})} (db/maybe-translatable (country :title))] ")"]]]
     [ui/three-columns
      {:col3 [:div {:style {:display "flex"
                            :flex-direction "column"
                            :justify-content "center"}}
              [:p {:style {:text-align "center"}} [:b (db/ttt :db/Magnitude "Magnitude")]]
              [:div {:style {:margin-left 15}}
               [mag-scale]]]
       :col2 [:div {:style {:margin-left 30}} [:h2 (db/ttt :db/Regional-communities "Regional Communities")]
              (links-to regional-communities)]
       :col1 [:> bs/Image {:src (str "/assets/" region-id ".png")
                           :fluid true}]}]]))

(defn large
  [& n]
  (into [:span {:style {:font-size "1.3em"}}]
        (map str n)))

;(def p 0.22)

(defn bar
  [{:keys [p r w text]}]

  [:div {:style {:width 100 :height 100 :display "flex" :flex-direction "column" :justify-content "center" :align-items "flex-end"}}
   (when text  text [:div {:style {:margin "0" :font-size "1.4em"}} text])
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
                                            :justify-content "flex-start"}})]
    [ui/row
     [ui/col (base-style 3)
      (when with-context?
        [:<>
         [:h4 [:a {:href (str "/#/history/" (community :id))} (db/ttt :db/Local-history "Local earthquake history")]]
         [:p (db/ttt :db/Local-history-p1 "How many earthquakes of magnitude 4 or more have hit") " "
          (db/maybe-translatable (region :title)) " "
          (db/ttt :db/Local-history-p2 "in the past?")]])]
     [ui/col (base-style 5)
      [:h4 [:a {:href (str "/#/hex/" (community :id))} (db/ttt :db/Whats-happening "What's happening here and now?")]]
      (if (or (= (community :id) "norcia-S")
              (= (community :id) "zurich")
              (= (community :id) "sion")
              (< (community :p-7day) (* 1.2 (community :mean-7day))))
        [:p (db/ttt :db/local-quiet-message "%1 is currently in a quiet period." (community :title))]
        [:p  (db/ttt :db/local-message "%1 is seeing higher chances than normal because of increased 
             seismic activity around the Mount Vettore fault system." (community :title))]
        )]
     [ui/col (base-style {:span 4})
      (when with-context?
        [:<>
         [:h4 [:a {:href (str "/#/world/" (community :id))}
               (db/ttt :db/How-does-location-compare "How does %1 compare to the world" (community :title))]]
         [:p (db/ttt :db/How-chance-compares "How does the current chance of a magnitude 4+ quake in %1 compare to an average week in other places worldwide?"
                     (community :title))]])]]))
(comment
(db/ttt :db/How-does-location-compare "How does %1 compare to the world" "Spoleto"))


(defn update-status
  []
  [:div {:style {:font-size 16
                 :margin-left 30
                 :color "#888"}}

   [ui/row {:style {:width 570}}
        ;[:span  "Last updated"]
        ;[:span "00:00 6th July 2021"]
    [ui/col {:xs 6} (db/ttt :db/Last-updated "Last updated")]
    [ui/col {:xs 6} [db/ttt :db/from-date "00:00 6th July 2021"]]]

   [ui/row {:style {:width 570}}
    [ui/col {:xs 6} (db/ttt :db/Next-update-due "Next update due")]
    [ui/col {:xs 6} (db/ttt :db/to-date "00:00 7th July 2021")]]])

(comment
  (db/ttt :db/current-chance-is "The current chance is %1 %2 average"
          123
          "higher than")

)
;;;
;;
;; Continued fractions give us fractions with low denominators that are best fits to any given
;; real. Useful when trying to find low num representations of .
;;
;;; 
(defn c-fraction
  "Convert a real number p to a continued fraction held in a lazy-seq."
  [p]
  (lazy-seq
   (let [m (js/Math.floor p)
         f (- p m)]
     (if (< f 1e-8)
       [m]
       (cons m (c-fraction (/ 1 f)))))))

(defn real->real
  "Evaluate real as continued fraction truncated at n-terms and converted to p/q fraction,
   but then we're returning p/q as a real again."
  [d n-terms]
  {:pre [(pos-int? n-terms)]}
  (let [ns (reverse (take n-terms (c-fraction d)))]
    (condp = n-terms
      1 (first ns)
      (reduce
       (fn [acc n]
         (println [acc n] ::reaf->f)
         (if (zero? n)
           (reduced (/ 1 acc))
           (+ n (/ 1 acc))))
       (first ns)
       (rest ns)))))

(defn real->f
  "Evaluate real as continued fraction truncated at n-terms and converted to a p/q fraction
   returning the result as [p q].
   This should give a 'best' rational approximation."
  [d n-terms]
  {:pre [(pos? d)
         (pos-int? n-terms)]}
  (let [ns (reverse (take n-terms (c-fraction d)))
        f-0 [(first ns) 1]]
    (if (= n-terms 1)
      f-0
      (reduce (fn [[p q] n] 
                [(+ (* n p) q) p])
              f-0
              (rest ns)))))

(comment 
  (real->f 1.2 3)
  (real->f 1.2 4)
  (real->f 1.2 5)
0)

(defn int-sf
  "Provides a representation of an integer to given number of sig figs.
   Single arity defaults to 2 sf"
  ([n]
   (int-sf n 2))
  ([n sig-figs]
   (let [n (Math/round n)
         pow10 (Math/pow 10 (- (Math/floor (Math/log10 n)) (dec sig-figs)))]
     (* pow10 (Math/round (/ n pow10))))))

(comment
  (int-sf 8)
  ;; => 8

  (int-sf 12)
  ;; => 12

  (int-sf 123)
  ;; => 120

  (int-sf 1234)
  ;; => 1200

  (int-sf 12345678)
  ;; => 12000000

  (int-sf 12345678 5)
  ;; => 12346000


  )


(defn get-odds
  "Convert a probability to a 'nice' odds value returned as an [on against] vector.
   Interpret the result as odds on if p > 0.5. Odds against if p < 0.5. Evens if p == 0.5
   
   By default 4 continued fraction terms are used."
  ([prob]
   (get-odds prob 4))
  ([prob n]
   (let [[p q] (real->f prob n)]
     (map int-sf [p (- q p)]))))

(comment
  (get-odds 0.9999)
  (get-odds 0.0001)
  (get-odds 0.5)
  (get-odds 0.000012)

  0
  )


(defn good-odds
  "Same as get-odds, but we search for a continued fraction approximation that
   has numbers less than or equal to 3."
  [prob]
  (->> (for [n-terms [6 5 4 3 2]
             :let [odds (get-odds prob n-terms)]]
         odds)
       (filter #(<= (apply min %) 3))
       first))

(comment
  (filter #(<= (apply min %) 3) '([209 1004] [51 245] [5 24] [1 5] [1 4]))

  (get-odds 0.6)
  ;; => [3 2]

  (get-odds 0.6002)
  ;; => [2 1]

  (get-odds 0.6002 5) ; 5 term may be better here
  ;; => [3 2]

  (good-odds 0.6002)

  (good-odds 1.2)
  (get-odds 0.1723 2)
  (good-odds 0.1723)


  (good-odds 0.966)
  (good-odds 0.000123)
  ;; => [24 1]

  (get-odds 0.83)
  ;; => [5 1]

  (get-odds 0.83 5)
  ;; => [39 8]  ; 5 terms gives higher precision but nasty numbers

  (get-odds 0.83 6) ; convergence on 6 terms
  ;; => [83 17]

  (real->f 0.6 0)
  (real->f 0.6 1)
  (real->f 0.6 2)
  (real->f 0.6 3)
  (real->f 0.6 4)
  (real->f 0.6 5)
  (real->real 0.6 5)

  (real->f 0.6002 4)
  ;; => [2 3]

  (real->f 0.6002 5)
  ;; => [3 5]

  (real->f 0.6002 6)
  ;; => [599 998]

  (real->f 12.3452 1)

  (real->f 0.833333333 5)

  (c-fraction 2)

  (take 1 (c-fraction 0.6))
  ;; => (0)

  (take 2 (c-fraction 0.6))
  ;; => (0 1)

  (take 3 (c-fraction 0.6))
  ;; => (0 1 1)

  (take 4 (c-fraction 0.6))
  ;; => (0 1 1 2)

  (take 5 (c-fraction 0.6))
  ;; => (0 1 1 2)

  (take 12 (c-fraction 3.245))
  ;; => (3 4 12 3 1)

  (take 12 (c-fraction (/ 1 3.245)))
  ;; => (0 3 4 12 3 1)   ; reciprocal of a continued fraction induces shift

  (take 20 (c-fraction (js/Math.exp 1))) ; e has a nice expansion in continued fractions
  ;; => (2 1 2 1 1 4 1 1 6 1 1 8 1 1 10 1 1 12 1 1)

  (real->f js/Math.PI 2)
  )

(defn area-status
  "show earthquake status of an area"
  [community]
  (let [p (community :p-7day)
        mean (community :mean-7day)
        mag+ @(rf/subscribe [::subs/mag+])
        with-vis? @(rf/subscribe [::subs/with-vis?])
        odds? @(rf/subscribe [::subs/odds?])
        rr%2 (condp = (compare p mean)
               -1 (db/ttt :db/smaller-than "smaller than")
               0 (db/ttt :db/about "about")
               1 (db/ttt :db/higher-than "higher than"))]
    [ui/row {:style {:font-size "21px"}}
     [ui/col
      [:div {:style {:border "1px solid #CCC"
                     :border-radius 20
                     :min-height 370
                     :padding (str (if with-vis? 15 30) "px 30px")
                     :box-shadow "1px 1px 1px 1px #CCC"
                     :background-color "#444466" #_"#80647D"
                     :color "white"}}
       [ui/row {:style {:display "flex" :align-items "center" :justify-content "space-between"
                        :padding-bottom (when with-vis? 25)}}
        [:div (db/ttt :db/the-chance-within [:<> "The chance of an earthquake" [:br] [:nobr "within 6th July ⟷ 13th July is"]])]
        [:<>
         [vis% p]]]

       [ui/row 
        [:div {:style {:width "100%" :display "flex" :align-items "center" :justify-content "space-between" :padding-bottom 35}}
         [:div  ;ui/col {:md 9}  Using divs to avoid gutter between mag-scale and main box
          [:div (db/ttt :db/whereas "whereas the chance in an average week is")]]
         [:div  ;ui/col {:md 3}
          [vis% mean]]]]

       [ui/row {:style {:display "flex" :align-items "center" :justify-content "space-between" :padding-bottom 25}}
        (if odds?
          (let [odds (good-odds p)
                row-style {:style {:width "100%" :display "flex" :flex-direction "row" :justify-content "space-between"}}]
            
            (if (< p 0.5)
              [:div row-style
               (db/ttt :db/odds-against "The odds against an earthquake are")
               [:nobr (string/join " - " (reverse odds))]]
              [:div row-style
               (db/ttt :db/odds-on "The odds on an earthquake are")
               [:nobr (string/join " - " odds)]]))
          [:<>
           [:span (db/ttt :db/current-chance-is [:span "The current chance is %1 %2 average"]
                          (let [rr (/ p mean)]
                            (str (if (> rr 1)
                                   (js/Math.round rr) ;(* 10 (js/Math.round (/ rr 10)))
                                   (trim-s (.toPrecision (js/Number. (* rr 100)) 2)))))
                          rr%2)
            ]
           ])]]

      [update-status]]]))

(defn arrow-template
  "a parameterised direction arrow"
  [{:keys [top right bottom left deg]}]
  [:div {:style {:width "22px" :height "22px" :border-bottom "22px solid #444466" :border-left "22px solid transparent"
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
    (when-let [neighbour (get-in location [:neighbours direction])]
      (link-location-by-id level neighbour (map-arrow direction)))))

(comment
  (link-neighbour :community "spoleto-NE" :S)
  ;; => [:a {:href "#/hex/spoleto-SE"} [:div {:style {:border-right "20px solid transparent", :transform "rotate(180deg)", :bottom "3%", :top nil, :width "22px", :border-left "22px solid transparent", :right nil, :position "absolute", :height "22px", :border-bottom "22px solid #444466", :left "43%"}}]]

  0)

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
  [page-title content]
  (let [location (get-in @(rf/subscribe [::subs/current-route])
                         [:path-params :id])
        all-communities (group-by :id (:items @(rf/subscribe [::subs/communities])))
        community (first (all-communities location))
        all-regions (group-by :id (:items @(rf/subscribe [::subs/regions])))
        region-id (community :region)
        region (first (all-regions region-id))
        animate? @(rf/subscribe [::subs/animate?])
        quake? @(rf/subscribe [::subs/quake?])
        lambda (:p-7day community)
        time->next-q @(rf/subscribe [::subs/next-quake-t])
        lang @(rf/subscribe [::subs/lang])
        time-acc-f (events/time-acceleration-factor lang lambda @(rf/subscribe [::subs/average-time-to-quake]))
        ;time-acc-f-in-words ((keyword (str time-acc-f)) events/time-factors)
        time-acc-f-in-words (get-in events/time-factors [lang (keyword (str time-acc-f))])
        ]

    (when (and animate? quake?)
                 ;; Do this in effects and fx. 
                 ;; Also save setTimeout returns so they can be cleared before starting another timer
      (js/console.log (str "Next quake in: " time->next-q " ms."))
      (js/setTimeout #(rf/dispatch [::events/quake? true]) time->next-q)
      (js/setTimeout #(rf/dispatch [::events/quake? false (max 1000 (/ (* 1000 (events/time-of-next-quake lambda)) time-acc-f))]) 800))

    [ui/page
     [:<>
      [ui/row {:style {:margin-bottom 20 :display "flex" :align-items "flex-end"}}
       [ui/col {:md 3 :style {:display "inline-block" :font-size "2.2em" :font-weight  "500"}}
        [:nobr (community :title)] " (" [:a {:href (ui/href (region :href) {:id (region :id)})} (db/maybe-translatable (region :title))] ")"]
       [ui/col {:md 8 :style {:display "inline-block" :font-size "2em" :font-weight  "500"}}
        [page-title community]]]
      [ui/three-columns
       {:col3 #_[:div {:style {:margin-top 20}} [mag-scale]] [:div {:style {:display "flex"
                                                                            :flex-direction "column"
                                                                            :justify-content "center"}}
                                                              [:p {:style {:text-align "center"}} [:b (db/ttt :db/Magnitude "Magnitude")]]
                                                              [:div {:style {:margin-left 15}}
                                                               [mag-scale]]]
        :col2 [:div {:style {:margin-left 15}} (content community)]
        :col1 [:<> [:div {:style {:position "relative" #_#_:display "flex"} ;; Pre 14.3 Safaris have issues with images in flexbox
                          :class-name (when (and animate? quake?) "shake")}
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
                 (db/ttt :db/Every-second
                         "Every second of simulation represents %1 of real time in which each week has a %2 chance."
                         time-acc-f-in-words (nice% (community :p-7day)))])]}]
      [content-base community]]]))

(defn hex
  "A community-level page featuring a mapped hexagon."
  []
  (main-content-template
   (fn [community] [:<> (db/ttt :db/How-likely-is [:<> "How likely is a" " " [:i "magnitude 4 or above"] " earthquake" [:br] " within the next 7 days?"])])
   area-status))

;;;
;;
;;;
(defn histogram
  [community]
  (let [region (find-location-by-id :region (community :region))]
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
        (db/ttt :db/How-many-bar-chart "How many earthquakes of magnitude 4 or morehit %1 in each 50 year period?"
                (db/maybe-translatable (region :title)))]
       [:div {:style {:max-width 450}}
        [:> bs/Image {:src "/assets/history.png"
                      :alt "History of quakes in the area"
                      :fluid true}]]]]]))

(defn history
  "Showing the earthquake history for an area"
  []
  (main-content-template
   (fn [community]
     (let [region (find-location-by-id :region (community :region))]
       [:span  (db/ttt :db/Mag4-over-time "Mag 4+ earthquakes in %1 over time"
                       (db/maybe-translatable (region :title)))]))
   histogram))


;;;
;;
;;;
(defn pc [x] (str x "%"))


(defn average-city
  "mark an average city"
  [{:keys [p X dx y fill city]}]
  (let [p (* 100 p)]
    [:<>
     [:text {:style {:font-size "1.2em"} :fill fill
             :x (pc (X (+ p dx))) :y (pc (+ y 7))} (pc p)]

     [:text {:x (pc (X (+ p dx))) :y (pc (+ y 14)) :fill fill} city]
     [:line {:x1 (pc (X (+ p dx))) :x2 (pc (X p)) :y1 (pc y) :y2 "42%" :stroke fill :stroke-width 2}]
     [:circle {:cx (pc (X (+ p dx))) :cy "42%" :r 5 :fill fill}]]))

(defn world-averages
  [community]
  (let [X #(+ 10 (* % 2))
        dx -4
        p (community :p-7day)
        community-x (X (* 100 p))
        average-cities @(rf/subscribe [::subs/average-cities]) ]
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
       (cond
         (nil? community) "Missing community data"
         (nil? (community :p-7day)) "Missing community :p-7day"
         :else [:svg {:width "100%" :height "100%"}
                [:g {:transform "translate(-25 0)"}
                 [:rect {:x 0 :y "42%" :width "120%" :height "60%" :fill "#fff3"}]
                 [:text {:x "8%" :y "10%" :fill "#fff"} (db/ttt :db/compare-cities-1 "The chance of a magnitude 4 or")]
                 [:text {:x "8%" :y "17%" :fill "#fff"} (db/ttt :db/compare-cities-2 "more within the next 7 days is")]
                 [:text {:style {:font-size "1.5em"} :fill "#fff" :x (pc (+ community-x dx)) :y "30%"} (nice% p) " in " (community :title)]
                 [:line {:x1 (pc community-x) :x2 (pc community-x) :y1 "36%" :y2 "42%" :stroke "#fff" :stroke-width 2}]
                 [:circle {:cx (pc community-x) :cy "42%" :r 5 :fill "#fff"}]
                 (into [:g
                        [:line {:x1 "10%" :x2 "90%" :y1 "42%" :y2 "42%" :stroke "#fff" :stroke-width 3}]]
                       (map (fn [tick]
                              (let [x* (X tick)
                                    x (str x* "%")
                                    dx (str (- x* 2) "%")]
                                [:g
                                 [:line {:x1 x :x2 x
                                         :y1 "39%" :y2 "45%"
                                         :stroke "#fff8" :stroke-width 1}]
                                 [:text {:x dx :y "38%" :fill "#fff8" :font-size 14} (str tick "%")]]))
                            (range 0 50 10)))
                 (when average-cities
                   (into [:g] (map (fn [city] (average-city (assoc city :X X :dx 0))) average-cities)))

                 
                 [:text {:x "8%"
                         :y (if (db/ttt :db/compared-to-these-cities-2 "in these cities") 
                              "87%" "89%")
                         :fill "#fff"} (db/ttt :db/compared-to-these-cities "compared to an average week in these cities")]
                 [:text {:x "8%" :y "95%" :fill "#fff"} (db/ttt :db/compared-to-these-cities-2 "in these cities")]]])

       #_[:> bs/Image {:src "/assets/thermometer.png"
                     :alt "Map showing forecast area"
                     :fluid true}]]]]))

(defn world
  "Showing community relative to world averages"
  []
  (main-content-template
   (fn [community] [:span (db/ttt :db/compare-world-cities "How does %1 compare to the world?" (community :title))])
   world-averages))






