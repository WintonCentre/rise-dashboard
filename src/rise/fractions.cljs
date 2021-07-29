(ns simple.fractions)

(defn c-fraction
  "Convert a real number p to a continued fraction"
  [p]
  (let [m (Math/floor p)
        f (- p m)]
    (if (< f 1e-10)
      (lazy-seq [m])
      (lazy-seq (cons m (c-fraction (/ 1 f)))))))

(defn real->f
  "Evaluate real as continued fraction truncated at n-terms and converted to a p/q fraction
   returning the result as [p q]."
  [d n-terms]
  {:pre [(pos? d)
         (pos-int? n-terms)]}
  (let [ns (reverse (take n-terms (c-fraction d)))
        f-0 [(first ns) 1]]
    (if (= n-terms 1)
      f-0
      (reduce
       (fn [[p q] n]
         [(+ (* n p) q) p])
       f-0
       (rest ns)))))

(comment
  (real->f Math/PI 1)
  (real->f Math/PI 2)
  (real->f Math/PI 3)
  (real->f Math/PI 4)
  (real->f Math/PI 5)
  )