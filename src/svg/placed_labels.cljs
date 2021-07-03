(ns svg.placed-labels
  (:require
            [clojure.string :as str]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [svg.scales :refer [->Identity nice-linear i->o o->i in out ticks tick-format-specifier]]))

(defn placed-label
  "Place a label using scales x and y to locate it at (u,v) in graph space containing text styled by class-name"
  [{:keys [k x y text u v class-name]}]
  (let [X (i->o x)
        Y (i->o y)]
    (when text
      [:g {:key k :class-name class-name}
       [:text {:x (X u) :y (Y v)} text]])))
