(ns svg.chart-area
  (:require 
            [svg.space :refer [space]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.axis :refer [axisBottom axisTop axisLeft axisRight]]
            [svg.scales :refer [->Identity nice-linear i->o o->i in out ticks tick-format-specifier]]
            [svg.grid :refer [grid-horizontal grid-vertical]]
   ))


(defstyle default-styles
  [".outer" {:fill   "none"
             :stroke "#CCC"}]
  [".inner" {:fill             "#FFDDDD"
             :stroke           "none"
             :stroke-opacity   100
             :stroke-width     2
             :stroke-dasharray "3, 4"}]
  [".annotation" {
                  :font-size "24px"
                  }]
  [".grid" {
            :stroke         "#000"
            :stroke-width   1
            :stroke-opacity 0.5
            }]
  [".arrow" {
             :stroke       "#000"
             :stroke-width "1.5px"
             }])

(defn x-title-svg
  [{:keys [x y x-title padding styles]}]
  (let [[X0 X1] (out x)
        [Y0 y1] (out y)
        x-mid (- (/ (- X1 X0) 2) (* 4 (count x-title)))]

    [:g {:key "x-title" :transform (str "translate(" x-mid " " (* 0.7 (:bottom padding)) ")")}
     [:text {:key        "note"
             :class-name (:annotation styles)
             :x          X0
             :y          Y0}
      x-title]]))

(defn y-title-svg
  [{:keys [x y y-title padding styles]}]
  (let [[X0 X1] (out x)
        [Y0 Y1] (out y)
        y-mid (/ (- Y1 Y0) 2)
        ydelta (* 4 (count y-title))]
    [:g {:key       "y-title"
         :transform (str "translate(" (- X0 (* 0.7 (:left padding))) " " (+ ydelta y-mid) ") " " rotate(-90 " X0 " " Y0 ") ")}
     [:text {:key        "note"
             :class-name (:annotation styles)
             :x          X0
             :y          Y0}
      y-title]]))

(defn chart-area [{:keys [width height
                              inner outer
                              margin padding
                              x y
                              styles
                              plots
                              placed-labels] :as params}]
  "Plots data in `plots` - which is a series of point vectors or a function returning same, and then overlays `placed-labels`."
  (let [X (i->o x)
        Y (i->o y)
        [X0 X1] (out x)
        [Y0 Y1] (out y)
        styles (if styles styles default-styles)]
    
    ; The outer rectangle uses padding to preserve aspect ratio while remaining responsive
    [:.row [:.col-sm-offset-0.col-sm-12
            [:div {:style {:margin      "0 auto"
                           :width       "auto"
                           :height      "auto"
                           :padding-top (str (* 100 (/ (:height outer) (:width outer))) "%")
                           :position    "relative"
                           }}
             [:svg {:style    {:position "absolute"
                               :top      0
                               :left     0}
                    :view-box (str " 0 0 " (:width outer) " " (:height outer))}

              [:defs
               [:filter#shadow
                [:feDropShadow {:dx 0.8 :dy 0.8 :stdDeviation 1 :flood-opacity 0.5}]]]

              [:g {:key       0
                   :transform (str "translate(" (:left margin) ", " (:top margin) ")")}

               [:rect {:key        1
                       :class-name (:outer styles)
                       :width      (:width inner)
                       :height     (:height inner)
                       }]

               ;;
               ;; define the coordinate system
               ;;
               [:g {:key       2
                    :transform (str "translate(" (:left padding) " " (:top padding) ")")}
                [:rect {:key        1
                        :class-name (:inner styles)
                        :width      width
                        :height     height}]

                ;; axes on all edges
                [:g {:key       "bottom"
                     ;:class-name ".xaxis"
                     :transform (str "translate(0," (+ (first (out y)) 10) ")")}
                 (axisBottom {:scale            x
                              :ticks            (ticks x)
                              :format-specifier (str (tick-format-specifier x) "")})

                 ]

                [:g {:key       "left"
                     :transform (str "translate(" (- (first (out x)) 10) ",0)")}
                 (axisLeft {:scale y :ticks (ticks y) :format-specifier (str (tick-format-specifier y))})]

                (x-title-svg (assoc params :x x :y y :padding padding :styles styles))
                (y-title-svg (assoc params :x x :y y :padding padding :styles styles))

                ; Add plots
                (when plots (if (fn? plots) (plots) plots))

                ; Add grid overlay
                (grid-horizontal {:x x :y y :step 10 :styles styles})
                (grid-vertical {:x x :y y :step 0.5 :styles styles})

                ; Add labels
                (when placed-labels (placed-labels))

                ]]]]]]))

(comment
  (space {:outer    {:width 1000, :height 800},
          :margin   {:top 30, :right 30, :bottom 30, :left 30},
          :padding  {:top 100, :right 100, :bottom 100, :left 100},
          :x-domain [0 2000] :x-title "Days", :x-ticks 10,
          :y-domain [0 100] :y-title "Percentage survival", :y-ticks 10,
          :data     []})

  )