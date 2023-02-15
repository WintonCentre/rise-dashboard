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
       :Navigate "Naviga verso la tua municipalità:"
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
       :how-to-survive "Come sopravvivere a un terremoto"
       :useful-tips "Cliccate qui per avere consigli utili su cosa fare prima, durante e dopo un terremoto."
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
       :How-likely-is [:span "Quanto è probabile un" " " [:i "terremoto di magnitudo 4 o superiore"] " entro i prossimi 7 giorni?"]
       :current-forecast [:span "Previsione attuale di un" " " [:i "terremoto di magnitudo 4 o superiore"] " nell'area selezionata:"]
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
       :compared-to-these-cities "rispetto a una settimana media in queste città."
       :use-the-map "Utilizzare la mappa per selezionare l'area per la quale si desidera una previsione:"
       :what-might-like "Come potrebbe essere?"
       :past-examples "Esempi passati di terremoti di magnitudo 4 e superiore:"
       :click-here "Clicca qui per saperne di più..."
       :what-can-I-do "Cosa posso fare con queste informazioni?"
       :much-less-certain "Le previsioni dei terremoti sono molto meno certe di quelle meteorologiche, perché non possiamo vedere cosa succede nel sottosuolo, ma possono fornire informazioni utili a chi deve prendere decisioni."
       :explanation "Spiegazione di questa previsione:"
       :show-me "Mostrami questo numero nel contesto"
       :back-to "Torna alla spiegazione della percentuale"

       :current-levels "Con gli attuali livelli di attività sismica, 
                        la possibilità che un terremoto di magnitudo 
                        4 o più si verifichi in questa zona tra"
       :imagine-100000 [:<> "Immaginate " [:b "100,000"] " aree con esattamente 
                            la stessa probabilità di un terremoto come questo."]
       :within-week "Entro la settimana del %1 <-> %2 con una probabilità del %3 ci aspetteremmo:"
       :is "%1 <-> %2 è:"
       :happen "Un terremoto di magnitudo 4+ si verificherà in"
       :not-happen "Nessun terremoto di magnitudo 4+ si è verificato in"
       :of-them "di loro"
       :put-in-context "Per contestualizzare il tutto:"
       :Jan "Gen"
       :Feb "Feb"
       :Mar "Mar"
       :Apr "Apr"
       :May "Mag"
       :Jun "Giu"
       :Jul "Lug"
       :Aug "Ago"
       :Sep "Set"
       :Oct "Ott"
       :Nov "Nov"
       :Dec "Dic"
       :presets "Preimpostazioni"
       :language "Lingua"
       :intro-1 "Questo è un sito web dimostrativo sviluppato dal Winton Centre 
                per la comunicazione del rischio e dell'evidenza dell'Università di Cambridge 
                nell'ambito del programma RISE. Il codice è liberamente disponibile per 
                l'uso a:"
       :intro-2 "Raccoglie alcune delle linee guida sulle migliori pratiche per 
                comunicazione delle previsioni sismiche operative, sviluppate nel corso del 
                progetto attraverso interviste e focus group con oltre 100 persone in Italia, Islanda e Svizzera. 
                persone in Italia, Islanda e Svizzera (tra cui sismologi, 
                sismologi, giornalisti, gestori di infrastrutture, protezione civile e 
                pubblico in generale), oltre a sondaggi online su larga scala in Italia, Svizzera e California, 
                Svizzera e California."
       :intro-3 "I terremoti scelti per fornire esempi di diversa magnitudo e le città scelte per fornire esempi di diversi livelli di pericolosità sono stati scelti per 
                città scelte per fornire esempi di diversi livelli di pericolosità, sono stati scelti per 
                in Italia e dovrebbero essere sostituiti."
       :intro-4 "La navigazione e i dati geografici, nonché i link/le parole per ulteriori 
                sono ovviamente solo a scopo dimostrativo."
       :intro-5 "Iniziate scegliendo una lingua e un Paese, poi una regione e una comunità, per vedere la dimostrazione. 
                comunità, per vedere la dimostrazione."})
