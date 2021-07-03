(ns svg.grid
  (:require [svg.scales :refer [i->o o->i in out]]
            ))

(defn grid-lines
  "Takes an x scale, a y scale, a step interval (in output space), and an orientation from :horizontal | :vertical."
  [{:keys [orientation x y step styles]}]
  (let [[[_ y*] [x1 x2] [y1 y2] bolder?] (if (= orientation :horizontal)
                                   [[x y] (out x) [(i->o y) (i->o y)] #(zero? (mod % 5))]
                                   [[y x] [(i->o x) (i->o x)] (out y) #(zero? (mod % 2))])
        [y0* y1*] (in y*)]

    (map-indexed
      (fn [k y_k] [:line {:key              (str "y" y_k)
                          :class-name       (:grid styles)
                          :stroke-dasharray (if (bolder? k) "5 5" "2 10")
                          :x1               (if (fn? x1) (x1 y_k) x1)
                          :x2               (if (fn? x2) (x2 y_k) x2)
                          :y1               (if (fn? y1) (y1 y_k) y1)
                          :y2               (if (fn? y2) (y2 y_k) y2)}])
      (range y0* y1* step))))



(defn grid-horizontal
  "Takes an x scale, a y scale, a step interval (in graph space)"
  [{:keys [x y step] :as params}]
  (grid-lines (assoc params :orientation :horizontal)))

(defn grid-vertical
  "Takes an x scale, a y scale, a step interval (in graph space)"
  [{:keys [x y step] :as params}]
  (grid-lines (assoc params :orientation :vertical)))
