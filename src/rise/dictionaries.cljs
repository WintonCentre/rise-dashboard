(ns rise.dictionaries
  [:require
   [rise.english :as en]
   [rise.italian :as it]
   [rise.icelandic :as is]
   [rise.german :as de]
   [rise.french :as fr]])
;;
;; Pulling in the language as separate files so we can round trip them individually
;; to translators.
;;
(def dictionary
  {:en en/english
   :it it/italian
   :is is/icelandic
   :de de/german
   :fr fr/french})
