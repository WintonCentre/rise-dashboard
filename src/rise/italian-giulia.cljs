(ns rise.italian)

(def italian
  "
   
   
   Translate by replacing the english phrases inside double quotes with the italian
   
   Phrases may be simple strings inside double quotes, or they may have markup for formatting.
   
   If it's a simple string, it may contain placeholders %1, %2, %3 etc.... The site code replaces these with
   something else - usually another simple string or number retrieved from the database. 
   
   Try not to change the markup and don't change the %1/%2 placeholders.
   "
  {:db/Dashboard "Sito web"
   ;:db/Countries "Paesi"
   ;:db/country "paese"
   ;:db/Communities "municipalità"
   ;:db/Regions "Regioni"
   ;:db/Italy "Italia"
   ;:db/Home "Home" ;"Home"
   ;:db/Settings "Impostazioni"
   ;:db/History "Storia"
   ;:db/Data-source "Fonte dati: INGV, n. 3456783567"
   ;:db/Responsibility "Responsabilità: Protezione Civile n.327 347684"
   ;:db/Ambulance [:span [:b "Ambulanza:"] " Chiama 118"]
   ;:db/Emergency [:span [:b "Numero d'emergenza:"] " Chiama 112"]
   ;:db/nowhere "in nessun luogo"
   ;:db/Country-regions "Regioni italiane"
   ;:db/Regional-communities "Municipalità regionali"
   ;:db/Local-history "Storia del terremoto locale"
   ;:db/Not-included "Non incluso nel sito web"
   ;:db/Mag "Mag"
   ;:db/Last-updated "L'ultimo aggiornamento"
   ;:db/Next-update-due "Il prossimo aggiornamento sarà"
   ;:db/from-date "00:00 6 giugno 2021"
   ;:db/to-date "00:00 7 luglio 2021"
   ;:db/Local-history-p1 "Quanti terremoti di magnitudo 4 o superiore hanno colpito"
   ;:db/Local-history-p2 "in passato?"
   ;:db/Whats-happening "Cosa sta succedendo qui e adesso?"
   ;:db/local-message "Si sta assistendo a probabilità più alte del normale a causa di un aumento dell'attività sismica verso il sistema di faglie di Monte Vittore."
   ;:db/How-does "Cosa succede a %1"
   ;:db/compare-to-world "rispetto al resto del mondo?"
   ;:db/How-chance-compares "How does the current chance of a magnitude 4+ quake in"
   ;:db/compare-average "paragonata a una settimana media in altri posti in tutto il mondo?"
   ;:db/How-likely-is [:span "Quanto è probabile un" " " [:i "magnitude 4 or above"] " terremoto" [:br] "entro i prossimi 7 giorni?"]
   ;:db/the-chance-within [:<> "La probabilità di un terremoto" [:br] [:nobr "entro 6 luglio ⟷ 13 luglio é"]]
   ;:db/Every-second "Ogni secondo della simulazione rappresenta l' %1 del tempo reale nel quale ogni settimana ha una probabilità pari al 2%."
   ;:db/whereas "mentre la probabilità in una settimana media è"
   ;:db/current-chance-is "Attualmente la probabilità è"
   ;:db/times-average "%1 volte rispetto alla media."
   ;:db/odds-against "Le quote contro un terremoto sono"
   ;:db/Mag4-over-time "Terremoto di Mag 4+ a Spoleto nel corso degli anni"
   ;:db/How-many-bar-chart "Quante terremoti di magnitudo 4 o superiore hanno colpito %1 ogni 50 anni?"
   ;:db/compare-world-cities "Come va a %1 rispetto al resto del mondo?"
   ;:db/compare-cities-1 "La probabilità di un magnitudo 4 o"
   ;:db/compare-cities-2 "superiore entro i prossimi 7 giorni è"
   })
