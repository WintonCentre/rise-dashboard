(ns rise.configure
  "A placeholder for any preparattory tool"
  
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.stacktrace :as stack]
   [clojure.core.memoize :as memo]
   ))

(defn create-configuration
  []
  "We currently have no confguration so this is an empty action.")

;-------- MAIN -----------
(defn main
  "Main entry point. This function reads config.edn and the spreadsheets and writes out edn files.
When processing a new version of the xlsx spreadsheets, run `lein check` first to validate them."
  [& args]
  (try
    (create-configuration)
    (catch Exception e
      (println (str ">>-configuration error - see stack trace:" (.getMessage e) " -<<"))
      (stack/print-stack-trace e)
      (System/exit 1))))