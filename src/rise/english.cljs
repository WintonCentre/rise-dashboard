(ns rise.english)

(def english
  "
   Translate by replacing the english phrases inside double quotes with the italian
   
   Phrases may be simple strings inside double quotes, or they may have markup for formatting.
   
   If it's a simple string, it may contain placeholders %1, %2, %3 etc.... The site code replaces these with
   something else - usually another simple string or number retrieved from the database. You can move the placeholders
   to wherever they make sense.
   
   If you need to change the markup in square brackets ask gmp26@cam.ac.uk how. The syntax is documented 
   at https://github.com/weavejester/hiccup/wiki/Syntax.
   "
  #:db{:Dashboard "Earthquake forecast"
       :Countries "Countries"
       :country "country"
       :Communities "Communities"
       :Regions "Regions"
       :Navigate "Navigate to your location:"
       :Italy "Italy"
       :Switzerland "Switzerland"
       :Iceland "Iceland"
       :Zurich-Canton "Zurich Canton"
       :Home "Home"
       :Settings "Settings"
       :History "History"
       :Data-source "Data source: INGV, n. 3456783567"
       :Responsibility "Responsibilty: Civil Protection n.327 347684"
       :Ambulance [:span [:b "Ambulance:"] " Call 118"]
       :Emergency [:span [:b "Emergency number:"] " Call 112"]
       :Country-regions {"italy" "Italian Regions" ; 'italy', 'iceland' 'switzerland' are lookup keys - do not translate them
                         "iceland" "Icelandic Regions"
                         "switzerland" "Swiss Regions"}
       :Regional-communities "Regional Communities"
       :Local-history "Local earthquake history"
       :how-to-survive "How to survive an earthquake"
       :useful-tips "Click here for useful tips on what to do before, during, and after an earthquake."
       :Not-included "Not included in forecast"
       :Mag "Mag"
       :Magnitude "Magnitude"
       :Last-updated "Last updated"
       :Next-update-due "Next update due"
       :from-date "00:00 6th July 2021"
       :to-date "00:00 7th July 2021"
       :Local-history-p1 "How many earthquakes of magnitude 4 or more have hit"
       :Local-history-p2 "in the past?"
       :Whats-happening "What's happening here and now?"
       :local-message "%1 is seeing higher chances than normal because of increased seismic activity around the Mount Vettore fault system."
       :local-quiet-message "There has been no significant seismic activity in %1. However, an earthquake can always occur with no warning."
       :How-does-location-compare "How does %1 compare to the world?" ; the location replaces %1
       :How-chance-compares "How does the current chance of a magnitude 4 or more quake in %1 compare to an average week in other places worldwide?"
       :compare-average "compare to an average week in other places worldwide?"
       :How-likely-is [:span "How likely is a" " " [:i "magnitude 4 or above"] " earthquake" #_[:br] " within the next 7 days?"]
       :current-forecast [:span "Current forecast for a" " " [:i "magnitude 4 or above"] " earthquake in the area you have selected:"]
       :the-chance-within [:<> "The chance of an earthquake" [:br] [:nobr "within 6th July ⟷ 13th July is"]]
       :Every-second "Every second of simulation represents %1 of real time in which each week has a %2 chance." ;%1 is a unit of time, %2 is weekly chance
       :whereas "whereas the chance in an average week is "
       :current-chance-is "The current chance is %1 %2 average." ; %1 is the relative risk
       :higher-than "times higher than" ; may replace %2 above
       :about "about the same as" ; may replace %2 above
       :smaller-than "times lower than" ; may replace %2 above 
       :odds-against "The odds against an earthquake are"
       :Mag4-over-time "Mag 4+ earthquakes in %1 over time"
       :How-many-bar-chart "How many earthquakes of magnitude 4 or morehit %1 in each 50 year period?" ; %1 is location
       :compare-world-cities "How does %1 compare to the world?"
       :compare-cities-1 "The chance of a magnitude 4 or"
       :compare-cities-2 "more within the next 7 days is"
       :compared-to-these-cities "compared to an average week in these cities"
       :Athens "Athens"
       :LosAngeles "Los Angeles"
       :Tokyo "Tokyo"
       :Rome "Rome"
       :Zurich "Zurich"
       :Sossi "Sossi"
       :use-the-map "Use the map to select the area you want a forecast for:"
       :what-might-like "What might it be like?"
       :past-examples "Past examples of magnitude 4 and above earthquakes:"
       :click-here "Click here to find out more..."
       :what-can-I-do "What can I do with this information?"
       :much-less-certain "Earthquake forecasts are much less certain than weather forecasts as we cannot see what is happening underground, but they can give useful information to those making decisions."
       :explanation "Explanation for this forecast:"
       :show-me "Show me this number in context"
       :back-to "Back to explanation of the percentage"
       
       :current-levels "With current levels of seismic activity the chance of 
                        an earthquake of magnitude 4 or more happening in this 
                        area between"
       :imagine-100000 [:<> "Imagine " [:b "100,000"] " areas with exactly the same 
                        chance of an earthquake as this one."]
       :within-week "Within the week of %1 <-> %2 with a %3 chance we would expect:"
       :is "%1 <-> %2 is:"
       :happen "An earthquake of magnitude 4+ to happen in"})
