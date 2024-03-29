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
  #:db{:Dashboard "Erdbebenvorhersage"
       :Countries "Länder"
       :country "Land"
       :Communities "Gemeinden"
       :Regions "Regionen"
       :Navigate "Wählen Sie Ihren Standort:"
       :Italy "Italien"
       :Switzerland "Schweiz"
       :Iceland "Iceland"
       :Zurich-Canton "Kanton Zurich"
       :Home "Home"
       :Settings "Einstellungen"
       :History "Geschichte"
       :Data-source "Datenquelle: SED, n. +xx xx xxx xx xx"
       :Responsibility "Verantwortlich: Schweizerischer Erdbebendienst an der ETH Zürich +xx xx xxx xx xx"
       :Ambulance [:span [:b "Ambulanz:"] " Call 118"]
       :Emergency [:span [:b "Notrufnummer:"] " Call 112"]
       :Country-regions {"italy" "Italien Regionen" ; "italy", "iceland" "switzerland" are lookup keys - do not translate them
                         "iceland" "Island Regionen"
                         "switzerland" "Schweiz Regionen"}
       :Regional-communities "Gemeinden"
       :Local-history "Lokale Erdbebengeschichte"
       :how-to-survive "Wie man ein Erdbeben überlebt"
       :useful-tips "Klicken Sie hier für nützliche Tipps, was Sie vor, während und nach einem Erdbeben tun können."
       :Not-included "In dieser Prognose nicht berücksichtigt"
       :Mag "Mag"
       :Magnitude "Erdbebenstärke"
       :Last-updated "Letztes Update"
       :Next-update-due "Nächstes Update fällig"
       :from-date "00:00 6. Juli 2021"
       :to-date "00:00 7. Juli 2021"
       :Local-history-p1 "Wie viele Erdbeben mit einer Magnitude von 4 oder höher haben sich"
       :Local-history-p2 "in der Vergangenheit ereignet?"
       :Whats-happening "Was passiert jetzt und hier?"
       :local-message "Aufgrund erhöhter seismischer Aktivität im Bereich des lokalen Verwerfungssystems ist die Wahrscheinlichkeit für ein Erdbeben höher als sonst."
       :local-quiet-message "In %1 hat es bisher keine nennenswerten seismischen Aktivitäten gegeben. Ein Erdbeben kann jedoch jederzeit ohne Vorwarnung auftreten."
       :How-does-location-compare "Wie hoch ist die Wahrscheinlichkeit in %1 im Vergleich zur Welt?" ; the location replaces %1
       :How-chance-compares "Wie hoch ist die aktuelle Wahrscheinlichkeit eines Erdbebens der Magnitude von 4 oder höher in %1 im Vergleich zu einer durchschnittlichen Woche an anderen Orten der Welt?"
       :compare-average "im Vergleich zu einer durchschnittlichen Woche an anderen Orten der Welt?"
       :How-likely-is [:span "Wie wahrscheinlich ist ein " [:i "Magnituden 4 oder höher"] " Erdbeben in den nächsten 7 Tagen?"]
       :current-forecast [:span "Aktuelle Vorhersage für ein" " " [:i "Magnituden 4 oder höher"] " in dem von Ihnen ausgewählten Gebiet:"]
       :the-chance-within [:<> "Die Wahrscheinlichkeit eines Erdbebens" [:br] [:nobr "zwischen 6. Juli ⟷ 13. Juli ist"]]
       :Every-second "Jede Sekunde der Simulation entspricht %1 der realen Zeit, in der jede Woche eine Wahrscheinlichkeit von %2 hat." ;%1 is a unit of time, %2 is weekly chance
       :whereas "Im Gegensatz dazu beträgt die Wahrscheinlichkeit in einer durchschnittlichen Woche "
       :current-chance-is "Die aktuelle Wahrscheinlichkeit ist %1 %2 wie durchschnittlich" ; %1 is the relative risk"
       :higher-than "mal höher als" ; may replace %2 above
       :about "in etwas gleich" ; may replace %2 above
       :smaller-than "mal tiefer als" ; may replace %2 above      
       :odds-against "Die Chance, dass sich kein Erdebeben ereignet, liegt bei"
       :Mag4-over-time "Mag 4+ Erdbeben in %1 über die Zeit"
       :How-many-bar-chart "Wie viele Erdbeben mit einer Magnitude 4 oder höher ereignen sich in %1 in einer Zeitspanne von 50 Jahren?" ; %1 is location
       :compare-world-cities "Wie hoch ist die Wahrscheinlichkeit in %1 im Vergleich zur Welt?"
       :compare-cities-1 "Die Wahrscheinlichkeit eines Erdbebens der"
       :compare-cities-2 "Magnitude 4 oder mehr in den nächsten 7 Tagen ist"
       :compared-to-these-cities "verglichen mit einer durchschnittlichen Woche"
       :compared-to-these-cities-2 "in diesen Städten"
       :use-the-map "Wählen Sie auf der Karte das Gebiet aus, für das Sie eine Vorhersage wünschen:"
       :what-might-like "Wie könnte sie aussehen?"
       :past-examples "Frühere Beispiele für Erdbeben der Stärke 4 und darüber:"
       :click-here "Klicken Sie hier, um mehr zu erfahren..."
       :what-can-I-do "Was kann ich mit diesen Informationen anfangen?"
       :much-less-certain "Erdbebenvorhersagen sind viel unsicherer als Wettervorhersagen, da wir nicht sehen können, was unter der Erde passiert, aber sie können den Entscheidungsträgern nützliche Informationen liefern."
       :explanation "Erläuterung zu dieser Prognose:"
       :show-me "Zeigen Sie mir diese Zahl im Kontext"
       :back-to "Zurück zur Erklärung des Prozentsatzes"

       :current-levels "Angesichts der derzeitigen seismischen Aktivität ist 
                        die Wahrscheinlichkeit, dass sich in diesem Gebiet ein 
                        Erdbeben der Stärke 4 oder mehr zwischen"
       :imagine-100000 [:<> "Stellen Sie sich " [:b "100,000"] " Gebiete vor, in 
                           denen die Wahrscheinlichkeit eines Erdbebens genau so 
                           groß ist wie in diesem."]
       :within-week "Innerhalb der Woche von %1 <-> %2 mit einer Wahrscheinlichkeit von %3 würden wir erwarten:"
       :is "%1 <-> %2 ist:"
       :happen "Ein Erdbeben der Stärke 4+, das sich in"
       :not-happen "Kein Erdbeben der Stärke 4+ in"
       :of-them "von ihnen"
       :put-in-context "Um dies in einen Zusammenhang zu bringen:"
       :Jan "Jan"
       :Feb "Feb"
       :Mar "Mär"
       :Apr "Apr"
       :May "Mai"
       :Jun "Jun"
       :Jul "Jul"
       :Aug "Aug"
       :Sep "Sep"
       :Oct "Okt"
       :Nov "Nov"
       :Dec "Dez"
       :presets "Voreinstellungen"
       :language "Sprache"
       :intro-1 "Dies ist eine Demonstrations-Website, die vom Winton Centre 
                 for Risk & Evidence Communication an der Universität Cambridge 
                 als Teil des RISE-Programms entwickelt wurde. Der Code ist frei 
                 verfügbar für die Verwendung unter:"
       :intro-2 "Er fasst einige der bewährten Leitlinien für die 
                Kommunikation von operationellen Erdbebenvorhersagen, die im Rahmen des 
                im Rahmen des Projekts durch Interviews und Fokusgruppen mit über 100 
                Personen in Italien, Island und der Schweiz (darunter Seismologen, 
                Journalisten, Infrastrukturmanager, Zivilschutz und Mitglieder der 
                Öffentlichkeit) sowie groß angelegte Online-Umfragen in Italien, der 
                Schweiz und Kalifornien."
       :intro-3 "Die Erdbeben, die ausgewählt wurden, um Beispiele für verschiedene Magnituden zu geben, und die 
                Städte, die als Beispiele für unterschiedliche Gefährdungsstufen ausgewählt wurden, wurden für 
                Tests in Italien ausgewählt und sollten ersetzt werden."
       :intro-4 "Die geografische Navigation und die Daten sowie die Links/Worte für weitere Informationen 
                dienen natürlich nur zu Demonstrationszwecken."
       :intro-5 "Bitte wählen Sie zunächst eine Sprache und ein Land, dann eine Region und eine 
                Gemeinschaft, um die Demonstration zu sehen."})
