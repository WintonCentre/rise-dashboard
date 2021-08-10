(ns rise.db
  (:require [rise.dictionaries :as dict]
            [clojure.string :as string]
            [re-frame.core :as rf]
            [rise.subs :as subs]
            ))

(defn ttt*
  "Look up a the keyword in the dictionary.
   If found then return in an undecorated span.
   If not found return english in a pink span.
   
   Use this inside HTML"
  [country-code field-key english]
  (let [translation (get-in dict/dictionary [country-code field-key])]
    (if translation
      translation
      ;[:span translation]
      [:span.pink english])))

(defn svg-ttt*
  "Look up a the keyword in the dictionary.
   If found then return in an undecorated text.
   If found return english inside braces.
   
   This is useful inside SVG"
  [country-code field-key english]
  (let [translation (get-in dict/dictionary [country-code field-key])]
    (if translation
      translation
      [:tspan {:fill "#F727FE"} english])))

(defn ttt
  "1 arity looks up field-key in chosen dictionary
  2+ arity looks up cc, but then does parameter interpolation on %1 %2 ...
   args will replace %1 %2,... stopping when either args or %s run out"
  [field-key s & args]
  (let [cc @(rf/subscribe [::subs/lang])
        ts (ttt* cc field-key s)]
    (if (or (vector? ts) (zero? (count args))) ; ts may be a [:span.pink] 
      ts
      #_#_let [ts (ttt* :it cc s)]
      (first (reduce (fn [[result i] arg]
                       [(string/replace result (str "%" (inc i)) arg) (inc i)]) [ts 0] args)))))

(comment
  @(rf/subscribe [::subs/lang])
  (apply ttt [:db/Countries "Boo"])
  0)

(defn maybe-translatable
  "Countries, Regions, and Communities are listed as 'items' in their parent map in the db.
   If the item title needs to be translated it should appear as a [key english] value in the db.
   If not, it may appear as a string.
   These kinds of 'maybe-translatable' things must use this function to decode the db value in the correct language."
  [title]
  (if (string? title)
    title
    (apply ttt title)))


#_(defn ttt
  "2 arity looks up cc in chosen dictionary
  3+ arity looks up cc, but then does parameter interpolation on %1 %2 ...
   args will replace %1 %2,... stopping when either args or %s run out"
  [cc s & args]
  (let [ts (ttt* :it cc s)]
    (if (or (vector? ts) (zero? (count args))) ; ts may be a [:span.pink] 
      ts
      (let [ts (ttt* :it cc s)]
        (first (reduce (fn [[result i] arg]
                         [(string/replace result (str "%" (inc i)) arg) (inc i)]) [ts 0] args))))))


;;;
;; 
;; Certain words need to be translated in the db database below 
;; -- search for "; TRANSLATE"
;;
;; DO NOT translate lower-case country-names like "italy" as these are internal 
;; identifiers only.
;;
;;;

(def default-db
  {:lang :en
   :mag+ 4 ; display this magnitude and greater
   :animate? false ; whether to run the animation
   :quake? true
   :next-quake-t 15000 ; time to next quake. None if next-quake < clock
   :average-time-to-quake 15000 ; in ms

   :with-context? true ; include contextual pages (local history and compare with the world)
   :with-vis? false ; use visuals for the percentage
   :annular? true ; use an annular visual / use a bar chart
   :odds? false ; show odds or relative risk

   :average-cities [{:p 0.05 ; probability
                     :dx 0 ; label offset
                     :y 47  ; vertical position as %
                     :fill "#fff"
                     :city "Athens"} ; TRANSLATE
                    {:p 0.04
                     :dx -5
                     :y 62
                     :fill "#fff"
                     :city "Los Angeles"} ; TRANSLATE
                    {:p 0.2
                     :dx -3
                     :y 52
                     :fill "#fff"
                     :city "Tokyo"}] ; TRANSLATE
   :countries {:title [:db/Countries "Countries"]
               :items [{:href :rise.views/countries
                        :title [:db/Italy "Italy"]
                        :id "italy"
                        :map "italy.png"}
                       #_{:href :rise.views/countries
                          :title "New Zealand" ; TRANSLATE
                          :id "nz"
                          :map "nz.png"}
                       {:href :rise.views/countries
                        :title [:db/Switzerland "Switzerland"] ; TRANSLATE
                        :id "switzerland"
                        :map "switzerland.png"}
                       #_{:href :rise.views/countries
                          :title "Iceland" ; TRANSLATE
                          :id "iceland"
                          :map "iceland.png"}]}
   :regions {:title [:db/Regions "Regions"]
             :items [{:href :rise.views/regions
                      :title "Umbria"
                      :id "umbria"
                      :map "umbria.png"
                      :country "italy"}
                     {:href :rise.views/regions
                      :title [:db/Zurich-Canton "Canton of Zurich"]
                      :id "zurich-canton"
                      :map "zurich-canton.png"
                      :country "switzerland"}
                     {:href :rise.views/regions
                      :title "Valais"
                      :id "valais"
                      :map "valais.png"
                      :country "switzerland"}
                     #_{:href :rise.views/regions
                        :title "Lazio"
                        :id "lazio"
                        :map "lazio.png"
                        :country "italy"}
                     #_{:href :rise.views/regions
                        :title "Abruzzo"
                        :id "abruzzo"
                        :map "abruzzo.png"
                        :country "italy"}]}
   :communities {:title [:db/Communities "Communities"] ; TRANSLATE
                 :items [#_{:title "Foligno"
                            :region "umbria"
                            :id "foligno"}
                         #_{:title "Orvieto"
                            :region "umbria"
                            :id "orvieto"}
                         #_{:title "Perugia"
                            :region "umbria"
                            :id "perugia"}
                         ;;
                         ;; Norcia
                         ;;
                         {:href :rise.views/hex
                          :title "Norcia"
                          :id "norcia"
                          :map "norcia hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.30
                          :mean-7day 0.03
                          :neighbours {:N "norcia-N"
                                       :NE "norcia-NE"
                                       :NW "norcia-NW"
                                       :S "norcia-S"
                                       :SE "norcia-SE"
                                       :SW "norcia-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Norcia–N"
                          :id "norcia-N"
                          :map "norcia-N hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.26
                          :mean-7day 0.01
                          :neighbours {:SE "norcia-NE"
                                       :SW "norcia-NW"
                                       :S "norcia"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Norcia-NE"
                          :id "norcia-NE"
                          :map "norcia-NE.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.16
                          :mean-7day 0.00012
                          :neighbours {:NW "norcia-N"
                                       :SW "norcia"
                                       :S "norcia-SE"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Norcia-SE"
                          :id "norcia-SE"
                          :map "norcia-SE.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.023
                          :mean-7day 0.00015
                          :neighbours {:N "norcia-NE"
                                       :NW "norcia"
                                       :SW "norcia-S"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Norcia-S"
                          :id "norcia-S"
                          :map "norcia-S.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.001
                          :mean-7day 0.00005
                          :neighbours {:NE "norcia-SE"
                                       :N "norcia"
                                       :NW "norcia-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Norcia-SW"
                          :id "norcia-SW"
                          :map "norcia-SW.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.00012
                          :mean-7day 0.00044
                          :neighbours {:NE "norcia"
                                       :SE "norcia-S"
                                       :N "norcia-NW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Norcia-NW"
                          :id "norcia-NW"
                          :map "norcia-NW.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.014
                          :mean-7day 0.00011
                          :neighbours {:NE "norcia-N"
                                       :SE "norcia"
                                       :S "norcia-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         ;;
                         ;; Spoleto
                         ;; 
                         {:href :rise.views/hex
                          :title "Spoleto"
                          :id "spoleto"
                          :map "spoleto hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.022
                          :mean-7day 0.00015
                          :neighbours {:N "spoleto-N"
                                       :NE "spoleto-NE"
                                       :NW "spoleto-NW"
                                       :S "spoleto-S"
                                       :SE "spoleto-SE"
                                       :SW "spoleto-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto–N"
                          :id "spoleto-N"
                          :map "spoleto-N hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.012
                          :mean-7day 0.00005
                          :neighbours {:SE "spoleto-NE"
                                       :SW "spoleto-NW"
                                       :S "spoleto"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-NE"
                          :id "spoleto-NE"
                          :map "spoleto-NE hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.16
                          :mean-7day 0.00012
                          :neighbours {:NW "spoleto-N"
                                       :SW "spoleto"
                                       :S "spoleto-SE"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-SE"
                          :id "spoleto-SE"
                          :map "spoleto-SE hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.023
                          :mean-7day 0.00015
                          :neighbours {:N "spoleto-NE"
                                       :NW "spoleto"
                                       :SW "spoleto-S"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-S"
                          :id "spoleto-S"
                          :map "spoleto-S hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.001
                          :mean-7day 0.00005
                          :neighbours {:NE "spoleto-SE"
                                       :N "spoleto"
                                       :NW "spoleto-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-SW"
                          :id "spoleto-SW"
                          :map "spoleto-SW hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.00012
                          :mean-7day 0.00044
                          :neighbours {:NE "spoleto"
                                       :SE "spoleto-S"
                                       :N "spoleto-NW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-NW"
                          :id "spoleto-NW"
                          :map "spoleto-NW hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "umbria"
                          :country "italy"
                          :p-7day 0.014
                          :mean-7day 0.00011
                          :neighbours {:NE "spoleto-N"
                                       :SE "spoleto"
                                       :S "spoleto-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         ;;
                         ;; Zurich
                         ;;
                         {:href :rise.views/hex
                          :title "Zurich"
                          :id "zurich"
                          :map "zurich hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "zurich-canton"
                          :country "switzerland"
                          :p-7day 0.0095
                          :mean-7day 0.00001
                          :neighbours {:N "zurich-N"
                                       :NE "zurich-NE"
                                       :NW "zurich-NW"
                                       :S "zurich-S"
                                       :SE "zurich-SE"
                                       :SW "zurich-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Zurich–N"
                          :id "zurich-N"
                          :map "zurich-N hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "zurich-canton"
                          :country "switzerland"
                          :p-7day 0.0299
                          :mean-7day 0.00003
                          :neighbours {:SE "zurich-NE"
                                       :SW "zurich-NW"
                                       :S "zurich"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Zurich-NE"
                          :id "zurich-NE"
                          :map "zurich-NE.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "zurich-canton"
                          :country "switzerland"
                          :p-7day 0.16
                          :mean-7day 0.00012
                          :neighbours {:NW "zurich-N"
                                       :SW "zurich"
                                       :S "zurich-SE"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Zurich-SE"
                          :id "zurich-SE"
                          :map "zurich-SE.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "zurich-canton"
                          :country "switzerland"
                          :p-7day 0.023
                          :mean-7day 0.00015
                          :neighbours {:N "zurich-NE"
                                       :NW "zurich"
                                       :SW "zurich-S"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Zurich-S"
                          :id "zurich-S"
                          :map "zurich-S.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "zurich-canton"
                          :country "switzerland"
                          :p-7day 0.001
                          :mean-7day 0.00005
                          :neighbours {:NE "zurich-SE"
                                       :N "zurich"
                                       :NW "zurich-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Zurich-SW"
                          :id "zurich-SW"
                          :map "zurich-SW.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "zurich-canton"
                          :country "switzerland"
                          :p-7day 0.000012
                          :mean-7day 0.00001
                          :neighbours {:NE "zurich"
                                       :SE "zurich-S"
                                       :N "zurich-NW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Zurich-NW"
                          :id "zurich-NW"
                          :map "zurich-NW.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "zurich-canton"
                          :country "switzerland"
                          :p-7day 0.014
                          :mean-7day 0.00011
                          :neighbours {:NE "zurich-N"
                                       :SE "zurich"
                                       :S "zurich-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         ;;;
                         ;; Sion
                         ;;;
                         {:href :rise.views/hex
                          :title "Sion"
                          :id "sion"
                          :map "sion hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "valais"
                          :country "switzerland"
                          :p-7day 0.0099
                          :mean-7day 0.00032
                          :neighbours {:N "sion-N"
                                       :NE "sion-NE"
                                       :NW "sion-NW"
                                       :S "sion-S"
                                       :SE "sion-SE"
                                       :SW "sion-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Sion-N"
                          :id "sion-N"
                          :map "sion-N hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "valais"
                          :country "switzerland"
                          :p-7day 0.0303
                          :mean-7day 0.00049
                          :neighbours {:SE "sion-NE"
                                       :SW "sion-NW"
                                       :S "sion"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                        
                         {:href :rise.views/hex
                          :title "Sion-NE"
                          :id "sion-NE"
                          :map "sion-NE.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "valais"
                          :country "switzerland"
                          :p-7day 0.16
                          :mean-7day 0.00012
                          :neighbours {:NW "sion-N"
                                       :SW "sion"
                                       :S "sion-SE"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Sion-SE"
                          :id "sion-SE"
                          :map "sion-SE.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "valais"
                          :country "switzerland"
                          :p-7day 0.023
                          :mean-7day 0.00015
                          :neighbours {:N "sion-NE"
                                       :NW "sion"
                                       :SW "sion-S"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Sion-S"
                          :id "sion-S"
                          :map "sion-S.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "valais"
                          :country "switzerland"
                          :p-7day 0.001
                          :mean-7day 0.00005
                          :neighbours {:NE "sion-SE"
                                       :N "sion"
                                       :NW "sion-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Sion-SW"
                          :id "sion-SW"
                          :map "sion-SW.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "valais"
                          :country "switzerland"
                          :p-7day 0.00012
                          :mean-7day 0.00044
                          :neighbours {:NE "sion"
                                       :SE "sion-S"
                                       :N "sion-NW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Sion-NW"
                          :id "sion-NW"
                          :map "sion-NW.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :region "valais"
                          :country "switzerland"
                          :p-7day 0.014
                          :mean-7day 0.00011
                          :neighbours {:NE "sion-N"
                                       :SE "sion"
                                       :S "sion-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}]}})

;; For now, edit these definitions to change site language
;(def ttt (partial ttt* :it))
(def svg-ttt (partial svg-ttt* (default-db :lang)))

(defn db-ttt 
  [v-or-text]
  (if (vector? v-or-text)
    (do
      (println v-or-text ::db-ttt)
      (let [[_ db-key english] v-or-text]
        (ttt db-key english)))
    v-or-text
    )
  )
