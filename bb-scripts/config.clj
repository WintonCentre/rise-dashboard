#!/usr/bin/env bb
(ns config)
  (require '[clojure.java.shell :as shell])

(shell/sh "clojure" 
          "-A:config" 
          "-X" "rise.configure/main")

