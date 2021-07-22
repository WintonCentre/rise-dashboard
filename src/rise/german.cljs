(ns rise.german)


(def german
   "
   Translate by replacing the english phrases inside double quotes with the italian
   
   Phrases may be simple strings inside double quotes, or they may have markup for formatting.
   
   If it's a simple string, it may contain placeholders %1, %2, %3 etc.... The site code replaces these with
   something else - usually another simple string or number retrieved from the database. You can move the placeholders
   to wherever they make sense.
   
   If you need to change the markup in square brackets ask gmp26@cam.ac.uk how. The syntax is documented 
   at https://github.com/weavejester/hiccup/wiki/Syntax.
   "
  #:db{:Dashboard "Erdbeben Dashboard"
       :Countries "Länder"
       :country "Land"
       :Communities "Gemeinden"
       :Regions "Regionen"
       :Navigate "Gehen Sie zu Ihrem Standort."
       :Italy "Italien"
       :Home "Home"
       :Settings "Einstellungen"
       :History "Historie"
       :Data-source "Datenquelle: SED, n. +xx xx xxx xx xx"
       :Responsibility "Verantwortlichkeit: Schweizerischer Erdbebendienst +xx xx xxx xx xx"
       :Ambulance [:span [:b "Ambulanz:"] " Call 118"]
       :Emergency [:span [:b "Notrufnummer:"] " Call 112"]
       :Country-regions {"italy" "Italien Regionen" ; "italy", "iceland" "switzerland" are lookup keys - do not translate them
                         "iceland" "Island Regionen"
                         "switzerland" "Schweiz Regionen"}
       :Regional-communities "Regionale Gemeinden"
       :Local-history "Lokale Erdbebenhistorie"
       :Not-included "nicht berücksichtigt im Dashboard"
       :Mag "Mag"
       :Last-updated "Letztes Update"
       :Next-update-due "Nächstes Update fällig"
       :from-date "00:00 6. Juli 2021"
       :to-date "00:00 7. Juli 2021"
       :Local-history-p1 "Wie viele Erdbeben mit einer Magnitude von 4 oder höher haben sich"
       :Local-history-p2 "in der Vergangenheit ereignet?"
       :Whats-happening "Was geschieht momentan hier?"
       :local-message "Aufgrund erhöhter seismischer Aktivität besteht im Bereich der Mount-Vittore-Verwerfung eine höhere Wahrscheinlichkeit als normal."
       :How-does-location-compare "Wie steht %1 im Vergleich zur Welt?" ; the location replaces %1
       :How-chance-compares "Wie steht die aktuelle Wahrscheinlichkeit eines Erdbebens der Magnitude von 4 oder höher in %1 im Vergleich zu einer durchschnittlichen Woche an anderen Orten der Welt?"
       :compare-average "im Vergleich zu einer durchschnittlichen Woche an anderen Orten der Welt?"
       :How-likely-is [:span "Wie wahrscheinlich ist ein " [:i "Magnituden 4 oder höher"] " Erdbeben" [:br] "in den nächsten 7 Tagen?"]
       :the-chance-within [:<> "Die Wahrscheinlichkeit eines Erdbebens" [:br] [:nobr "zwischen 6. Juli ⟷ 13. Juli ist"]]
       :Every-second "Every second of simulation represents %1 of real time in which each week has a %2 chance." ;%1 is a unit of time, %2 is weekly chance
       :whereas "im Gegensatz ist die Wahrscheinlichkeit in einer durchschnittlichen Woche "
       :current-chance-is "Die aktuelle Wahrscheinlichkeit ist %1 %2 wie durchschnittlich" ; %1 is the relative risk"
       :higher-than "mal höher als" ; may replace %2 above
       :about "in etwas gleich" ; may replace %2 above
       :smaller-than "mal tiefer als" ; may replace %2 above      
       :odds-against "Die Odds gegen ein Erdbeben sind"
       :Mag4-over-time "Mag 4+ Erdbeben in %1 über die Zeit"
       :How-many-bar-chart "Wie viele Erdbeben mit einer Magnitude 4 oder höher ereignen sich in %1 in einer Zeitspanne von 50 Jahren?" ; %1 is location
       :compare-world-cities "Wie steht %1 im Vergleich zur Welt?"
       :compare-cities-2 "mehr in den nächsten 7 Tagen ist"
       :compared-to-these-cities "verglichen mit einer durchschnittlichen Woche in diesen Städten"})
