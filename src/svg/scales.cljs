(ns svg.scales)

;;;
;; Objects conforming to IScale represent a mapping between an inner coordinate system (in) to
;; an outer screen space out. This is often linear but could be non-linear e.g. if the coordinate system is
;; polar. The mapping functions are i->o and o->i. Each IScales maps just one dimension.
;; 
;; It is useful to colocate tick related information with the scale, but this does not have to be
;; used in every case.
;; 
;; Ticks is a preferred tick count. This is usually transformed to a 'nice' round number.
;; Tick-format-specifiers specifier how ticks should be labelled.
;;;
(defprotocol IScale
  (i->o [_])
  (o->i [_])
  (in [_])
  (out [_])
  (ticks [_])
  (tick-format-specifier [_])
  )

(def e10 (js/Math.sqrt 50))
(def e5 (js/Math.sqrt 10))
(def e2 (js/Math.sqrt 2))

(defn tick-step [start stop preferred-count]
  (let [step0 (/ (js/Math.abs (- stop start)) (max 0 preferred-count))
        step1 (js/Math.pow 10 (js/Math.floor (/ (js/Math.log step0) js/Math.LN10)))
        error (/ step0 step1)
        step (cond
               (>= error e10) (* 10 step1)
               (>= error e5) (* 5 step1)
               (>= error e2) (* 2 step1)
               :else step1)]
    (if (< stop start) (- step) step)))

(defn preferred-ticks [start stop preferred-count]

  (let [step (tick-step start stop preferred-count)]
    (range
      (* (js/Math.ceil (/ start step)) step)
      (+ (* (js/Math.floor (/ stop step)) step) (/ step 2))
      step)))

(defn numeric-format-specifier 
  "Provide a default format specifier for numeric scales"
  [scale]
  (let [abs-in (map js/Math.abs (:in scale))
        abs-step (js/Math.abs (apply tick-step (conj (:in scale) (:tick-count scale))))]
    (cond
      (< abs-step 0.00001)
      "~(~3,1e~)"
      (> (apply max abs-in) 99999)
      "~(~,1e~)"
      (>= abs-step 1)
      "~d"
      (>= abs-step 0.1)
      "~1$"
      (>= abs-step 0.01)
      "~$"
      (>= abs-step 0.001)
      "~3$"
      (>= abs-step 0.0001)
      "~0,4f"
      :else
      "~0,5f")))

(defn unit-format-specifier [unit scale]
  "Provide a default format specifier for numeric scales with a unit"
  (str (numeric-format-specifier scale) unit))

(defn percent-format-specifier [scale]
  "Provide a default format specifier for percentage scales"
  (unit-format-specifier "%" scale))

(defn- scale-ticks [a-scale tick-count]
  (apply preferred-ticks (conj (:in a-scale) tick-count)))

(defrecord Identity [in tick-count]
  IScale
  (i->o [this] identity)
  (o->i [this] identity)
  (in [this] (:in this))
  (out [this] (:in this))
  (ticks [this] (scale-ticks this tick-count))
  (tick-format-specifier [this] (numeric-format-specifier this)))

(defn- linear [[x1 x2] [y1 y2]] (fn [x] (+ y1 (* (/ (- x x1) (- x2 x1)) (- y2 y1)))))

(defn- linear-nice 
  "Return a nice domain given a range and preferred interval count "
  [[start stop :as input] & [p-count]]
  (let [n (if (nil? p-count) 10 p-count)
        step (tick-step start stop n)]
    (if (not (or (js/isNaN step) (nil? step)))

      (let [step (tick-step (* (js/Math.floor (/ start step)) step)
                            (* (js/Math.ceil (/ stop step)) step)
                            n)]
        [(* (js/Math.floor (/ start step)) step)
         (* (js/Math.ceil (/ stop step)) step)])

      input)))

(defn nice-identity [in tick-count]
  (->Identity (linear-nice in tick-count) tick-count))

(defrecord Linear [in out tick-count]
  IScale
  (i->o [this] (linear (:in this) (:out this)))
  (o->i [this] (linear (:out this) (:in this)))
  (in [this] (:in this))
  (out [this] (:out this))
  (ticks [this] (scale-ticks this tick-count))
  (tick-format-specifier [this] (numeric-format-specifier this)))

(defn nice-linear [in out tick-count]
  (->Linear (linear-nice in tick-count) out tick-count))

(comment

  (def lin (->Linear [0 100] [0 10] 10))
  ((i->o lin) 50)
  ((i->o lin) 200)
  (in lin)
  (out lin)
  ((o->i lin) 8)
  )