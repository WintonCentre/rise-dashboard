(ns rise.db)

(def default-db
  {:communities {:title "Communities"
                 :items [{:href :rise.views/hex
                          :title "Spoleto"
                          :id "spoleto"
                          :map "spoleto hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :neighbours {:N "Spoleto-N"
                                       :NE "Spoleto-NE"
                                       :NW "Spoleto-NW"
                                       :S "Spoleto-S"
                                       :SE "Spoleto-SE"
                                       :SW "Spoleto-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-N"
                          :id "spoleto-N"
                          :map "spoleto-N hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :neighbours {:SE "Spoleto-NE"
                                       :SW "Spoleto-NW"
                                       :S "Spoleto"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-NE"
                          :id "spoleto-NE"
                          :map "spoleto-NE hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :neighbours {:NE "Spoleto-N"
                                       :SE "Spoleto"
                                       :S "Spoleto-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-SE"
                          :id "spoleto-SE"
                          :map "spoleto-SE hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :neighbours {:N "Spoleto-NE"
                                       :NW "Spoleto"
                                       :SW "Spoleto-S"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-S"
                          :id "spoleto-S"
                          :map "spoleto-S hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :neighbours {:NE "Spoleto-SE"
                                       :N "Spoleto"
                                       :NW "Spoleto-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-SW"
                          :id "spoleto-SW"
                          :map "spoleto-SW hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :neighbours {:NE "Spoleto"
                                       :SE "Spoleto-S"
                                       :N "Spoleto-NW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}
                         {:href :rise.views/hex
                          :title "Spoleto-NW"
                          :id "spoleto-NW"
                          :map "spoleto-NW hex.png"
                          :latitude 42.739
                          :longitude 12.7376
                          :neighbours {:NE "Spoleto-N"
                                       :SE "Spoleto"
                                       :S "Spoleto-SW"}
                          :osm-href "https://www.openstreetmap.org/relation/42105"}]}}
  )
