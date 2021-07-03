(ns rise.config-utils
  (:require  [clojure.string :as string]))

;--- Utilities
;
;

(def MEMO
  "switch this on to use faster memoised data access functions, off when debugging" 
  true) 

(defn maybe-key
  "Convert a possible string possibly starting with a colon to a keyword"
  [s] (if (and (string? s) (> (count s) 1) (string/starts-with? s ":"))
        (keyword (subs s 1))
        s))

(defn transpose
  "transpose rows of a matrix. If 2 arity, add headers"
  ([m]
   (apply map vector m))
  ([m headers]
   (zipmap headers (transpose m))))
