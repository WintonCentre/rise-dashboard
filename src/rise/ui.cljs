(ns rise.ui
  "This should become the high level ui interface and should have all ns references factored out into 
the low level ui."
  (:require [clojure.string :refer [capitalize]]
            [reagent.core :as rc]
            [reitit.frontend.easy :as rfe]
            ["react-bootstrap" :as bs]
            [re-frame.core :as rf]
            [rise.events :as events]
            [rise.subs :as subs]
            [shadow.debug :refer [?-> ?->> locals]]))

(enable-console-print!)


(def container "a react/bootstrap component adapter" (rc/adapt-react-class bs/Container))
(def col "a react/bootstrap component adapter" (rc/adapt-react-class bs/Col))
(def row "a react/bootstrap component adapter" (rc/adapt-react-class bs/Row))
(def button "a react/bootstrap component adapter" (rc/adapt-react-class bs/Button))
(def tabs "a react/bootstrap component adapter" (rc/adapt-react-class bs/Tabs))
(def tab "a react/bootstrap component adapter" (rc/adapt-react-class bs/Tab))

(defn href
  "Return relative url for given route. Url can be used in HTML links. Note that k is a route name defined 
in the routes table."
  ([k]
   (href k nil nil))
  ([k params]
   (href k params nil))
  ([k params query]
   (rfe/href k params query)))


(comment
  (href :rise.views/home)
  ;; => "#/"

  (href :rise.views/info)
  ;; => "#/info"

  (href :rise.views/hex {:id 34})
  ;; => "#/hex/34"

  (href :rise.views/regions {:id "lazio"})

  (href :rise.views/countries {:id "italy"})
  0)


(defn navbar-dropdown-menu
  [submenu-key]
  (let [submenu @(rf/subscribe [submenu-key])]
    (into [:> bs/NavDropdown {:title (submenu :title) :id "basic-nav-dropdown"}]
          (map (fn [item]
                 (locals)
                 (when (item :href) 
                   [:> bs/NavDropdown.Item {:href (href (item :href) {:id (item :id)})
                                                           :key (item :id)}
                                   (item :title)]
                   ))
               (submenu :items)))))

(defn navbar
  "Straight out of the react-bootstrap example with reitit routing patched in."
  [{:keys [home-url logo]}]
  (let [route @(rf/subscribe [::subs/current-route])
        page-name (get-in route [:data :name])]

    [:h1 "Navbar"]
    [:> bs/Navbar {:bg "light" :expand "md"
                   :style {:border-bottom "1px solid black" :opacity "1"}}
     [:> bs/Navbar.Brand  {:href home-url} 
      [:img {:src logo :style {:height 40} :alt "RISE logo" :title "Dashboard Demo"}]
      [:span {:style {:margin-left 10}} "Earthquake dashboard"]]
     [:> bs/Navbar.Toggle {:aria-controls "basic-navbar-nav"}]
     [:> bs/Navbar.Collapse {:id "basic-navbar-nav" :style {:margin-left 70}}

      [:> bs/Nav {:style {:display "flex" :justify-content "space-between" :width "100%"}
                  :active-key (if page-name (name page-name) "home")}
       [:> bs/Nav.Link {:event-key :home
                        :href (href :rise.views/home)} "Home"]
       #_[:> bs/Nav.Link {:event-key :info
                        :href (href :rise.views/info)} "Info"]
       [navbar-dropdown-menu ::subs/countries]
       [navbar-dropdown-menu ::subs/regions]
       [navbar-dropdown-menu ::subs/communities]
       [:div {:style {:flex-grow 6}} " "]
       [:> bs/Nav.Link {:event-key :settings
                        :href (href :rise.views/settings)} "Settings"]

       #_[:> bs/NavDropdown {:title "Communities" :id "basic-nav-dropdown"}
        [:> bs/NavDropdown.Item {:href (href :rise.views/hex {:id "Spoleto"})
                                 :key "Spoleto"}
         "Spoleto"]]]]]))

(comment
  @(rf/subscribe [::subs/communities])
  @(rf/subscribe [::subs/regions])
  0)

(defn page
  "A generic  component, rendering a title and the page's children"
  ([title content]
   [container {:key 1
               :fluid "xl"
               :style {:min-height "calc(100vh - 200px)"
                       :background-color "#ffffffbb"
                       :max-width 2000
                       :margin-bottom 20}}
    [row
     [col
      title
      content]]]))

(defn three-columns
  [{:keys [col1 col2 col3]}]
  [row
   [col {:lg 3 :style {:max-width 500 :margin-bottom 30 :margin-right 0 :padding-right 0}}
    col1]
   [col {:lg 5 :style {:max-width 600 :margin-left -2 :padding-left 0}}
    col2]
   [col {:lg 3 :style {:max-width 500}}
    col3]])

(defn footer
  "Site footer."
  []
  [:div {:style {:width "100%" :height "100px" :background-color "black" :color "white"
                 :display "flex" :align-items "center" :justify-content "center"
                 :font-size 12}}
   [:div
    [:span "Data source: INGV, n. 3456783567"]
    [:br]
    [:span "Responsibilty: Civil Protection n.327 347684"]]
   [:div {:style {:margin-left 40}}
    [:span [:b "Ambulance:"] " Call 118"]
    [:br]
    [:span [:b "Emergency number:"] " Call 112"]]])

(defn root-component
  "The root of the component tree which is mounted on the main app html element"
  [{:keys [router subscribe-current-route]}]
  (let [current-route @(subscribe-current-route)]
    [:div {:style {:display :flex :flex-direction "column-reverse" :justify-content "space-between"}}
     (when current-route
       [:div {:style {:margin-top "0px" :padding-top 20}}
        [(-> current-route :data :view)]
        [footer]])
     [navbar {:router router
              :current-route current-route
              :home-url "/"
              :logo "/assets/rise-logo.png"
              :tool-name "rise"}]]))

(def mobile-break
  "Screens of this size or smaller are rendered with mobile oriented views."
  1200 ;800
  )

(defn open-icon
  "wrapper for access open-icon access"
  ([name]
   (open-icon nil name))
  ([style name]
   [:span (assoc {:class (str "oi oi-" name)
                  :title name
                  :aria-hidden "true"}
                 :style style)]))



