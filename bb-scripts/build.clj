#!/usr/bin/env bb

(ns build)
  (require '[clojure.java.shell :as shell])

(println "==> npm install")
(let [{:keys [:exit :err :out]} (shell/sh "npm" "install")]
  (if (zero? exit) 
    (do (println out)
        (println "<== npm install done"))
    (do (println ">>- npm install ERROR: -<<" err)
        (System/exit 1))))

(println "==> Compile")
(let [{:keys [:exit :err :out]}
      (shell/sh "npm" "run" "build")]
  (if (zero? exit)
    (do (println out)
        (println "<== tool created"))
    (do (println ">>- tool compilation error: -<< " err)
        (System/exit 1))))
