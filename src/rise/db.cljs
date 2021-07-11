(ns rise.db)

(def default-db
  {:mag+ 4
   :countries {:title "Countries"
               :items [{:href :rise.views/countries
                        :title "Italy"
                        :id "italy"
                        :map "italy.png"}
                       #_{:href :rise.views/countries
                        :title "New Zealand"
                        :id "nz"
                        :map "nz.png"}
                       {:href :rise.views/countries
                        :title "Switzerland"
                        :id "switzerland"
                        :map "switzerland.png"}
                       {:href :rise.views/countries
                        :title "Iceland"
                        :id "iceland"
                        :map "iceland.png"}]}
   :regions {:title "Regions"
             :items [{:href :rise.views/regions
                      :title "Umbria"
                      :id "umbria"
                      :map "umbria.png"
                      :country "italy"}
                     {:href :rise.views/regions
                      :title "Lazio"
                      :id "lazio"
                      :map "lazio.png"
                      :country "italy"}
                     {:href :rise.views/regions
                      :title "Abruzzo"
                      :id "abruzzo"
                      :map "abruzzo.png"
                      :country "italy"}]}
   :communities {:title "Communities"
                 :items [{:title "Foligno"
                          :region "umbria"
                          :id "foligno"}
                         {:title "Orvieto"
                          :region "umbria"
                          :id "orvieto"}
                         {:title "Perugia"
                          :region "umbria"
                          :id "perugia"}
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
                          :title "Spoleto-N"
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
                          :p-7day 0.022
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
                          :p-7day 0.0012
                          :mean-7day 0.00009
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
                         {:title "Todi"
                          :region "umbria"
                          :id "todi"}]}}
  )
