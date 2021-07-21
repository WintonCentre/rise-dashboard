(ns rise.italian)

(def italian
  "
   
   
   Translate by replacing the english phrases inside double quotes with the italian
   
   Phrases may be simple strings inside double quotes, or they may have markup for formatting.
   
   If it's a simple string, it may contain placeholders %1, %2, %3 etc.... The site code replaces these with
   something else - usually another simple string or number retrieved from the database. 
   
   Try not to change the markup and don't change the %1/%2 placeholders.
   "
  {:db/Dashboard "Cruscotto del terremoto"
   ;:db/Countries "Countries"
   ;:db/country "country"
   ;:db/Communities "municipalità"
   ;:db/Regions "Regions"
   ;:db/Italy "Italia"
   ;:db/Home "Home" ;"Home"
   ;:db/Settings "Settings"
   ;:db/History "History"
   ;:db/Data-source "Data source: INGV, n. 3456783567"
   ;:db/Responsibility "Responsibilty: Civil Protection n.327 347684"
   ;:db/Ambulance [:span [:b "Ambulance:"] " Call 118"]
   ;:db/Emergency [:span [:b "Emergency number:"] " Call 112"]
   ;:db/nowhere "nowhere"
   ;:db/Country-regions "Italian Regions"
   ;:db/Regional-communities "Regional Communities"
   ;:db/Local-history "Local earthquake history"
   ;:db/Not-included "Not included in dashboard"
   ;:db/Mag "Mag"
   ;:db/Last-updated "Last updated"
   ;:db/Next-update-due "Next update due"
   ;:db/from-date "00:00 6th July 2021"
   ;:db/to-date "00:00 7th July 2021"
   ;:db/Local-history-p1 "How many earthquakes of magnitude 4 or more have hit"
   ;:db/Local-history-p2 "in the past?"
   ;:db/Whats-happening "What's happening here and now?"
   ;:db/local-message "is seeing higher chances than normal because of increased seismic activity around the Mount Vittore fault system."
   ;:db/How-does "How does"
   ;:db/compare-to-world "compare to the world?"
   ;:db/How-chance-compares "How does the current chance of a magnitude 4+ quake in"
   ;:db/compare-average "compare to an average week in other places worldwide?"
   ;:db/How-likely-is [:span "How likely is a" " " [:i "magnitude 4 or above"] " earthquake" [:br] " within the next 7 days?"]
   ;:db/the-chance-within [:<> "The chance of an earthquake" [:br] [:nobr "within 6th July ⟷ 13th July is"]]
   ;:db/Every-second "Every second of simulation represents %1 of real time in which each week has a %2 chance."
   ;:db/whereas "whereas the chance in an average week is"
   ;:db/current-chance-is "The current chance is"
   ;:db/times-average "times %1 average."
   ;:db/odds-against "The odds against an earthquake are"
   ;:db/Mag4-over-time "Mag 4+ earthquakes in %1 over time"
   ;:db/How-many-bar-chart "How many earthquakes of magnitude 4 or morehit %1 in each 50 year period?"
   ;:db/compare-world-cities "How does %1 compare to the world?"
   ;:db/compare-cities-1 "The chance of a magnitude 4 or"
   ;:db/compare-cities-2 "more within the next 7 days is"
   })
