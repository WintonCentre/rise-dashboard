(ns rise.italian)

(def italian
  "
   Translate by replacing the english phrases inside double quotes with the italian
   
   Phrases may be simple strings inside double quotes, or they may have markup for formatting.
   
   If it's a simple string, it may contain placeholders %1, %2, %3 etc.... The site code replaces these with
   something else - usually another simple string or number retrieved from the database. You can move the placeholders
   to wherever they make sense.
   
   If you need to change the markup in square brackets ask gmp26@cam.ac.uk how. The syntax is documented 
   at https://github.com/weavejester/hiccup/wiki/Syntax.
   "
  #:db{:Dashboard "Previsioni di terremoti"
       :Countries "Paesi"
       :country "paese"
       :Communities "Municipalità"
       :Regions "Regioni"
       :Navigate "Naviga verso la tua municipalità."
       :Italy "Italia"
       :Switzerland "Svizzera"
       :Iceland "Islanda"
       :Zurich-Canton "Il cantone di Zurigo"
       :Home "Home"
       :Settings "Impostazioni"
       :History "Storia"
       :Data-source "Fonte dati: INGV, n. 3456783567"
       :Responsibility "Responsabilità: Protezione Civile n.327 347684"
       :Ambulance [:span [:b "Ambulanza:"] " Chiama 118"]
       :Emergency [:span [:b "Numero d'emergenza:"] " Chiama 112"]
       :Country-regions {"italy" "Regioni Italiane"        ; 'italy', 'iceland' 'switzerland' are lookup keys - do not translate them
                         "iceland" "Regioni Islandese"
                         "switzerland" "Regioni Svizzere"}
       :Regional-communities "Municipalità regionali"
       :Local-history "Storia del terremoto locale"
       :Not-included "Non incluso nella previsione"
       :Mag "Mag"
       :Magnitude "Magnitudo"
       :Last-updated "L'ultimo aggiornamento"
       :Next-update-due "Il prossimo aggiornamento sarà"
       :from-date "00:00 6 luglio 2021"
       :to-date "00:00 7 luglio 2021"
       :Local-history-p1 "Quanti terremoti di magnitudo 4 o superiore hanno colpito"
       :Local-history-p2 "in passato?"
       :Whats-happening "Cosa sta succedendo qui e ora?"
       :local-message "A %1 si assiste a probabilità più elevate del normale a causa di un aumento dell'attività sismica verso il sistema di faglie di Monte Vettore."
       :local-quiet-message "Non c'è stata alcuna attività sismica significativa a %1. Tuttavia, un terremoto può sempre verificarsi senza preavviso."
       :How-does-location-compare "Come si pone %1 rispetto al mondo?" ; the location replaces %1
       :How-chance-compares "Come si confronta l'attuale probabilità di un terremoto di magnitudo 4+ a %1 con una settimana media in altri posti nel mondo?"
       :compare-average "paragonata a una settimana media in altri posti nel mondo?"
       :How-likely-is [:span "Quanto è probabile un" " " [:i "terremoto di magnitudo 4" [:br] "o superiore"] " entro i prossimi 7 giorni?"]
       :the-chance-within [:<> "La probabilità di un terremoto" [:br] [:nobr "entro 6 luglio ⟷ 13 luglio é"]]
       :Every-second "Ogni secondo della simulazione rappresenta %1 del tempo reale nel quale ogni settimana ha una probabilità pari al 2%."
       :whereas "mentre la probabilità in una settimana media è"
       :current-chance-is "Attualmente la probabilità è %1 %2 media" ; %1 is the relative risk
       :higher-than "volte superiore alla" ; may replace %2 above
       :about "simile alla" ; may replace %2 above
       :smaller-than "volte inferiore della" ; may replace %2 above
       :odds-against "Le quote contro un terremoto sono"
       :Mag4-over-time "Terremoto di Mag 4+ in %1 nel corso degli anni"
       :How-many-bar-chart "Quanti terremoti di magnitudo 4 o superiore sono accaduti ogni 50 anni?"
       :compare-world-cities "Come si pone %1 rispetto al mondo?"
       :compare-cities-1 "La probabilità di un magnitudo 4 o superiore"
       :compare-cities-2 "entro i prossimi 7 giorni è"
       :compared-to-these-cities "rispetto a una settimana media in queste città."})
