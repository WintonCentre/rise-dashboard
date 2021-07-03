(ns svg.time-series
  (:require
            [clojure.string :as str]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.scales :refer [->Identity nice-linear i->o o->i in out ticks tick-format-specifier]]))


#_(defn as-point-series [plot-layers]
    "Convert a vector of data-layers of time-series y-values to a vector of time-series of [x y] points."
    (into [] (for [time-series plot-layers]
               (into [] (map-indexed (fn [i v] [i v]) time-series)))))


(defn line-plot
  "X and Y are the x-axis and y-axis scale functions.
   Data should be a series of coordinate pairs
   [[0 100]  [1 98.89556593176486]  ... [9 64.83779488900586]  [10 60.8297996952587]]"
  [{:keys [x y point-series class-name]}]
  (let [X (i->o x)
        Y (i->o y)]

    ;(println "line point series" point-series)
    (when (seq point-series)
      (let [point (fn [x y] (str (X x) " " (Y y)))]
        [:g {:class-name class-name}
         [:polyline {:points (map #(apply point %) point-series)}]]))))

(defn area-plot
  "scale contains the x-axis and y-axis scale functions.
  Point series should look something like this:
  [[0 100]  [1 98.89556593176486]  ... [9 64.83779488900586]  [10 60.8297996952587]]
  An area plot is similar to a line plot, but the polyline is closed into a polygon by attaching the baseline"
  ([{:keys [x y point-series class-name y-baseline]}]
   (let [X (i->o x)
         Y (i->o y)
         y-baseline (if y-baseline y-baseline 0)]
     ;(println "area series " point-series)
     ;(println "class-name: " class-name)
     (when (seq point-series)
       (let [point (fn [x y] (str (X x) " " (Y y)))]
         [:g {:class-name class-name}
          [:polygon {:points (str/join ", " [(str/join ", " (map #(apply point %) point-series))
                                             (str/join ", " [(point (first (last point-series)) y-baseline)
                                                             (point (first (first point-series)) y-baseline)])])}]])))))
