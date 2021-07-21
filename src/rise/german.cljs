(ns rise.german)


(def german
  "Translate the english by replacing 'nil' with the italian
   
   Phrases may be simple strings inside double quotes, or they may have hiccup syntax to include formatting.
   
   If it's a simple string, it may contain placeholders %1, %2, %3 etc.... The site code will replace these with
   something else - usually another simple string retrieved from the database. The replacements cannot contain hiccup markup.
   
   Hiccup syntax (which encloses everything in [square brackets]) cannot contain placeholders."
  #:db{:Dashboard "Tableau de bord des tremblements de terre"
       :Countries "Countries"
       :country "country"
       :Communities "Communities"
       :Regions "Regions"
       :Italy "Italy"
       :Home "Home"
       :Settings "Settings"
       :History "History"
       :Data-source "Data source: INGV, n. 3456783567"
       :Responsibility "Responsibilty: Civil Protection n.327 347684"
       :Ambulance [:span [:b "Ambulance:"] " Call 118"]
       :Emergency [:span [:b "Emergency number:"] " Call 112"]
       :nowhere "nowhere"
       :Country-regions "Italian Regions"
       :Regional-communities "Regional Communities"
       :Local-history "Local earthquake history"
       :Not-included "Not included in dashboard"
       :Mag "Mag"
       :Last-updated "Last updated"
       :Next-update-due "Next update due"
       :from-date "00:00 6th July 2021"
       :to-date "00:00 7th July 2021"
       :Local-history-p1 "How many earthquakes of magnitude 4 or more have hit"
       :Local-history-p2 "in the past?"
       :Whats-happening "What's happening here and now?"
       :local-message "is seeing higher chances than normal because of increased seismic activity around the Mount Vittore fault system."
       :How-does "How does"
       :compare-to-world "compare to the world?"
       :How-chance-compares "How does the current chance of a magnitude 4+ quake in"
       :compare-average "compare to an average week in other places worldwide?"
       :How-likely-is [:span "How likely is a" " " [:i "magnitude 4 or above"] " earthquake" [:br] " within the next 7 days?"]
       :the-chance-within [:<> "The chance of an earthquake" [:br] [:nobr "within 6th July ⟷ 13th July is"]]
       :Every-second "Every second of simulation represents %1 of real time in which each week has a %2 chance."
       :whereas "whereas the chance in an average week is"
       :current-chance-is "The current chance is"
       :times-average "times %1 average."
       :odds-against "The odds against an earthquake are"
       :Mag4-over-time "Mag 4+ earthquakes in %1 over time"
       :How-many-bar-chart "How many earthquakes of magnitude 4 or morehit %1 in each 50 year period?"
       :compare-world-cities "How does %1 compare to the world?"
       :compare-cities-1 "The chance of a magnitude 4 or"
       :compare-cities-2 "more within the next 7 days is"})
