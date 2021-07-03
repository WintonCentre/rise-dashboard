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

  0)
#_(defn get-client-rect
    "return the bounding rectangle of a node"
    [node]
    (let [r (.getBoundingClientRect node)]
      {:left (.-left r), :top (.-top r) :right (.-right r) :bottom (.-bottom r) :width (.-width r) :height (.-height r)}))


#_(defn link-text
    "The route :data :link-text gives an indication of link text for this route, which must be adjusted
   according to its path-params"
    [route]

    (let [path (-> route :path-params)
          _ (js/console.log route)
          organ (:organ path)
          centre (:centre path)]
      (if organ
        organ
        "Home")))

#_(comment
;;;;;
;;
;;
    (defn- resolve-href
      [to path-params query-params]
      (if (keyword? to)
        (rfe/href to path-params query-params)
        (let [match  (rfr/match-by-path rfr/router to)
              route  (-> match :data :name)
              params (or path-params (:path-params match))
              query  (or query-params (:query-params match))]
          (if match
            (rfe/href route params query)
            to))))


    (defn Link
      [{:keys [to path-params query-params active]} & children]
      (let [href (resolve-href to path-params query-params)]
        (into
         [:a {:href href} (when active "> ")] ;; Apply styles or whatever
         children)))

    (defn- name-matches?
      [name path-params match]
      (and (= name (-> match :data :name))
           (= (not-empty path-params)
              (-> match :parameters :path not-empty))))

    (defn- url-matches?
      [url match]
      (= (-> url (split #"\?") first)
         (:path match)))

    (defn NavLink
      [{:keys [to path-params current-route] :as props} & children]
      [Link {:to to
             :path-params path-params
             :query-params (when current-route
                             (get-in current-route [:data :query-params]))
             :active (when current-route
                       (or (name-matches? to path-params current-route)
                           (url-matches? to current-route)))}])

    (defn NavLink-
      [{:keys [to path-params current-route] :as props} & children]
      (let [active (or (name-matches? to path-params current-route)
                       (url-matches? to current-route))]
        [Link (assoc props :active active) children])))
;;
;;;;;


#_(defn active-key
    "Return the active href key given the current-route"
    [route]
    (let [ak  (cond
                (= {} (:path-params route)) (href :rise.views/home)
                :else (get-in route [:path-params :organ]))]
      ak))

(defn navbar
  "Straight out of the react-bootstrap example with reitit routing patched in."
  [{:keys [home-url logo current-route router]}]
  (let [route @(rf/subscribe [::subs/current-route])
        page-name (get-in route [:data :name])]
    (locals)
    [:h1 "Navbar"]
    [:> bs/Navbar {:bg "light" :expand "md"
                   :style {:border-bottom "1px solid black" :opacity "1"}}
     [:> bs/Navbar.Brand  {:href home-url} [:img {:src logo :style {:height 40} :alt "RISE logo" :title "Dashboard Demo"}]]
     [:> bs/Navbar.Toggle {:aria-controls "basic-navbar-nav"}]
     [:> bs/Navbar.Collapse {:id "basic-navbar-nav" :style {:margin-left 70}}

      [:> bs/Nav {:active-key (if page-name (name page-name) "home")
                 ;:class "mr-auto" :style {:height "100%" :vertical-align "middle"}
                  }
       [:> bs/Nav.Link {:event-key :home
                        :href (href :rise.views/home)} "Home"]
       [:> bs/Nav.Link {:event-key :info
                        :href (href :rise.views/info)} "Info"]
       #_[:> bs/Nav.Link {:event-key :hex
                        :href (href :rise.views/hex {:id 324})} "Location"]
       [:> bs/NavDropdown {:title "Location" :id "basic-nav-dropdown"}
        [:> bs/NavDropdown.Item {:href (href :rise.views/hex {:id "Spoleto"})
                                 :key "Spoleto"}
         "Spoleto"]]]]]))

(comment
  (keys @(rf/subscribe [::subs/organ-centres]))
  @(rf/subscribe [::subs/organ-centre])
  (rise.ui/href :rise.views/organ-centres {:organ (name :kidney)
                                           :centre "card"}))

(defn footer
  "Site footer. 
   todo: Needs to be made configurable."
  []
  [:div {:style {:width "100%" :height "60px" :background-color "black" :color "white"
                 :display "flex" :align-items "center" :justify-content "center"}}
   [:div {:style {:margin "20px"}} "Footer"]])

(defn root-component
  "The root of the component tree which is mounted on the main app html element"
  [{:keys [router subscribe-current-route]}]
  (let [current-route @(subscribe-current-route)]
    [:div {:style {:display :flex :flex-direction "column-reverse"}}
     (when current-route
       [:div {:style {:margin-top "0px" :padding-top 20}}
        [(-> current-route :data :view)]
        [footer]])
     [navbar {:router router
              :current-route current-route
              :home-url "/"
              :logo "/assets/Spoleto hex.png"
              :tool-name "rise"}]]))

(defn tool-buttons
  "Create buttons for each page"
  [{:keys [key label organ centre tool active-tool]}]

  (let [active (= (name tool) active-tool)]
    [button {:id (str (name organ) "-" (name centre) "-" (name key))
             :variant (if active "primary" "outline-primary")
             :style {:margin-bottom 2
                     :margin-right 2}
             :active active
             :key key
             :on-click #(rf/dispatch [::events/navigate :rise.views/organ-centre-tool
                                      {:organ organ
                                       :centre centre
                                       :tool tool}])}
     label]))

(defn page-menu
  []
  [:> bs/ButtonGroup {:vertical true}
   [:> bs/Button {:variant "secondary"} "Info Button 1"]])

(defn tools-menu
  "Render a group of tool selection buttons"
  [tools include-guidance? organ-name centre-name orientation]
  (let [active-tool (get-in @(rf/subscribe [::subs/current-route]) [:path-params :tool])
        mdata @(rf/subscribe [::subs/mdata])
        tools (->> (if include-guidance?
                     tools
                     (remove #(= :guidance (:key %)) tools))
                   (map #(conj % [:organ organ-name]))
                   (map #(conj % [:centre centre-name]))
                   (map #(conj % [:tool (:key %)]))
                   (map #(conj % [:active-tool active-tool]))
                   (map #(conj % [:mdata mdata])))] ;TODO: configure this filter!
    [:> bs/ButtonToolbar
   ;; :todo; There'll be a better CSS solution to keeping this on screen for both desktop and mobile
   ;; Even better would be to configure the break points as what makes sense will be ver application
   ;; specific.
     (->> (take 3 tools)
          (map tool-buttons)
          (into [:> bs/ButtonGroup orientation]))
     (->> (drop 3 tools)
          (map tool-buttons)
          (into [:> bs/ButtonGroup orientation]))]))

(defn background-link
  "Tool menu prefix rubric."
  [organ centre tool]
  [:p "For more information that will be helpful to patients, follow the link to "
   [:a {:style {:color "#007BFF"
                ;:text-decoration-line "underline"
                :cursor "pointer"}
        :on-click #(rf/dispatch [::events/navigate :rise.views/organ-centre-tool
                                 {:organ organ
                                  :centre centre
                                  :tool :guidance}])} "background guidance"]
   "."
   " There is also a " [:a {:target "_blank" :href (str (name organ) ".pdf")} "PDF download"]
   " which explains the tool in depth."])

(defn page
  "A generic  component, rendering a title and the 's children"
  ([title content]
   [container {:key 1
               :fluid "xl"
               :style {:min-height "calc(100vh - 165px"
                       :background-color "#ffffffbb"
                       :max-width 2000
                       :margin-bottom 20}}
    [row
     [col
      [:h1 {:style {:margin-top 20}} title]
      content]]]))

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



