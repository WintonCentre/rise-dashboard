(ns rise.icelandic)

(def icelandic
  "
   Translate by replacing the english phrases inside double quotes with the icelandic, overwriting the German text.)
   
   Phrases may be simple strings inside double quotes, or they may have markup for formatting.
   
   If it's a simple string, it may contain placeholders %1, %2, %3 etc.... The site code replaces these with
   something else - usually another simple string or number retrieved from the database. You can move the placeholders
   to wherever they make sense.
   
   If you need to change the markup in square brackets ask gmp26@cam.ac.uk how. The syntax is documented 
   at https://github.com/weavejester/hiccup/wiki/Syntax.
   "
  #:db{:Dashboard "Jarðskjálftaspá"
       :Countries "Lönd"
       :Communities "Samfélög"
       :Regions "Svæði"
       :Italy "Ítalíu"
       :Switzerland "Sviss"
       :Iceland "Ísland"
       :Zurich-Canton "Zürich kantóna"
       :Navigate "Farðu að staðsetningu þinni:"
       :how-to-survive "Hvernig á að lifa af jarðskjálfta"
       :useful-tips "Smelltu hér til að fá gagnlegar ábendingar um hvað á að gera fyrir, á meðan og eftir jarðskjálfta." 
       :Home "Home"
       :Settings "Stillingar"
       :History "Saga"
       :Data-source "Uppruni gagna: SED, n. +xx xx xxx xx xx"
       :Responsibility "Ábyrgð: Almannavarnir n.327 347684"
       :Ambulance [:span [:b "Sjúkrabíll:"] " Hringdu í 118"]
       :Emergency [:span [:b "Neyðartilvik:"] " Hringdu í 112"]
       :Country-regions {"italy" "ítölsk héruð" ; "italy", "iceland" "switzerland" are lookup keys - do not translate them
                         "iceland" "íslensk svæði"
                         "switzerland" "svissnesk héruð"}
       :Regional-communities "Svæðissamfélög"
       :Local-history "Staðbundin jarðskjálftasaga"
       :Not-included "Ekki innifalið í spánni"
       :Mag "Stærð"
       :Magnitude "Stærð"
       :Last-updated "Síðast uppfært"
       :Next-update-due "Næsta uppfærsla væntanleg"
       :from-date "00:00 6. júlí 2021"
       :to-date "00:00 7. júlí 2021"
       :Local-history-p1 "Hversu margir skjálftar af stærðinni 4 eða fleiri hafa orðið"
       :Local-history-p2 "í fortíðinni?"
       :Whats-happening "Hvað er að gerast hér og nú?"
       :local-message "%1 sér meiri líkur en venjulega vegna aukinnar skjálftavirkni í kringum Vettore-breiðukerfið."
       :local-quiet-message "Engin marktæk skjálftavirkni hefur verið í %1. Hins vegar getur jarðskjálfti alltaf átt sér stað án viðvörunar."
       :How-does-location-compare "Hvernig er %1 samanborið við heiminn?" ; the location replaces %1
       :How-chance-compares "Hvernig eru núverandi líkur á skjálfta af stærðinni 4 eða meira í %1 samanborið við meðalviku á öðrum stöðum um allan heim?"
       :compare-average "bera saman við meðalviku á öðrum stöðum um allan heim?"
       :How-likely-is [:span "Hversu líklegt er " [:i "að jarðskjálfti af stærðinni 4 eða yfir verði"] " á næstu 7 dögum?"]
       :current-forecast [:span "Núverandi spá fyrir" " " [:i "jarðskjálfta af stærðinni 4 eða hærri"] " á svæðinu sem þú hefur valið:"]
       :Every-second "Hver sekúnda af uppgerð táknar %1 af rauntíma þar sem hver vika hefur %2 möguleika." ;%1 is a unit of time, %2 is weekly chance
       :whereas "en möguleikinn í meðalviku er "
       :current-chance-is "Núverandi möguleiki er %1 %2 meðaltal." ; %1 is the relative risk"
       :higher-than "sinnum hærri en" ; may replace %2 above
       :about "um það sama og" ; may replace %2 above
       :smaller-than "sinnum lægri en" ; may replace %2 above      
       :odds-against "Líkurnar á jarðskjálfta eru"
       :Mag4-over-time "Mag 4+ jarðskjálftar í %1 með tímanum"
       :How-many-bar-chart "Hversu margir jarðskjálftar af stærðinni 4 eða fleiri lenda í %1 á hverju 50 ára tímabili?" ; %1 is location
       :compare-world-cities "Hvernig er %1 samanborið við heiminn?"
       :compare-cities-1 "Líkurnar á stærð 4 eða"
       :compare-cities-2 "meira á næstu 7 dögum er"
       :compared-to-these-cities "miðað við meðalviku í þessum borgum"
       :Athens "Athens"
       :LosAngeles "Los Angeles"
       :Tokyo "Tokyo"
       :Rome "Rome"
       :Zurich "Zurich"
       :Sossi "Sossi"
       :use-the-map "Notaðu kortið til að velja svæðið sem þú vilt spá fyrir:"
       :what-might-like "Hvernig gæti það verið?"
       :past-examples "Fyrri dæmi um jarðskjálfta af stærðinni 4 og hærri:"
       :click-here "Smelltu hér til að fá frekari upplýsingar..."
       :what-can-I-do "Hvað get ég gert við þessar upplýsingar?"
       :much-less-certain "Jarðskjálftaspár eru mun óvissari en veðurspár þar sem við getum ekki séð hvað er að gerast neðanjarðar, en þær geta gefið gagnlegar upplýsingar til þeirra sem taka ákvarðanir."
       :explanation "Skýring á þessari spá:"
       :show-me "Sýndu mér þetta númer í samhengi"
       :back-to "Aftur í skýringar á hlutfallinu"
       :current-levels "Með núverandi skjálftavirkni eru líkurnar á því að jarðskjálfti af stærðinni 4 eða meira verði á þessu svæði á milli kl."
       :imagine-100000 [:<> "Ímyndaðu þér " [:b "100,000"] " svæði með nákvæmlega sömu líkur á jarðskjálfta og þetta."]
       :within-week "Innan vikunnar %1 <-> %2 með %3 möguleika sem við myndum búast við:"
       :is "%1 <-> %2 er:"
       :happen "Jarðskjálfti af stærðinni 4+ verður í"
       :not-happen "Enginn jarðskjálfti af stærðinni 4+ mun gerast í"
       :of-them "þeirra"
       :put-in-context "Til að setja þetta í samhengi:"
       :Jan "jan"
       :Feb "feb"
       :Mar "mar"
       :Apr "apr"
       :May "maí"
       :Jun "jún"
       :Jul "júl"
       :Aug "ágú"
       :Sep "sep"
       :Oct "okt"
       :Nov "nóv"
       :Dec "des"
       :presets "Forstillingar"
       :language "Tungumál"})