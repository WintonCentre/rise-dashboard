(ns svg.space
  (:require
            [svg.scales :refer [nice-linear]]
            ))

(defn space [{:keys [outer margin padding
                      x-domain x-ticks
                      y-domain y-ticks] :as params}]
  (let [inner {:width  (- (:width outer) (:left margin) (:right margin))
               :height (- (:height outer) (:top margin) (:bottom margin))}
        width (- (:width inner) (:left padding) (:right padding))
        height (- (:height inner) (:top padding) (:bottom padding))]
    ;(println "x-domain" [0 width] x-ticks)
    ;(println "y-domain" [height 0] y-ticks)
    (assoc params
      :inner inner
      :width (- (:width inner) (:left padding) (:right padding))
      :height (- (:height inner) (:top padding) (:bottom padding))
      :x (nice-linear x-domain [0 width] x-ticks)
      :y (nice-linear y-domain [height 0] y-ticks)
      ))
  )

(comment
  (sort (keys (space {:outer    {:width 600 :height 300}
                       :margin   {:top 20 :right 20 :bottom 20 :left 20}
                       :padding  {:top 30 :right 30 :bottom 30 :left 30}
                       :x-domain [0 2000]
                       :x-ticks  10
                       :y-domain [0 100]
                       :y-ticks  5})))
  ;=> {:y #svg.scales.Linear{:in [0 100], :out [200 0], :tick-count 5},
  ; :y-domain [0 100],
  ; :x-domain [0 2000],
  ; :width 500,
  ; :x-ticks 10,
  ; :inner {:width 560, :height 260},
  ; :padding {:top 30, :right 30, :bottom 30, :left 30},
  ; :x #svg.scales.Linear{:in [0 2000], :out [0 500],
  ; :tick-count 10},
  ; :outer {:width 600, :height 300},
  ; :y-ticks 5, :height 200,
  ; :margin {:top 20, :right 20, :bottom 20, :left 20}}
  )