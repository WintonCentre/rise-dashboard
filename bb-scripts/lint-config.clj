#!/usr/bin/env bb

(require '[babashka.deps :as deps])
(deps/add-deps '{:deps {borkdude/rewrite-edn {:mvn/version "0.0.2"}}})

(require '[borkdude.rewrite-edn :as r]
         '[clojure.edn :as edn])

(def public "../resources/public/")
(def kidney (str public "kidney/"))

(def src-path (str kidney))
(def bristol-edns (str "edn/Bristol/"))
(def files ["survival.txt"])

(defn lint
  [f]
  (slurp f)
  )

(->> files
     (map #(str src-path bristol-edns %))
     (map lint))