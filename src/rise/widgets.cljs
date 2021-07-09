(ns rise.widgets
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [clojure.edn :as edn]
            ["react-bootstrap" :as bs]
            [rise.bsio :as bsio]
            [rise.events :as events]
            [shadow.debug :refer [locals ?> ?-> ?->>]]
            ))
;;
;; Transplants code some of which may come in handy in rise
;;

(defn key->id
  "Convert a namespaced factor key to an id"
  [k]
  (if k (str (namespace k) "-" (name k)) nil))

(defn bad-widget-type [msg]
  (js/alert msg))

(defmulti widget
  "Create a widget component of a given type.
   The first argument is the widget map, and the value of its :type slot determines the 
   widget type. If it's a keyword, dispatch on that. If it's a string, read it into a map and dispatch on that map's :type.
   This allows us to add parameters to the widget inside the type column in the spreadsheet."
  ;:type
  (fn [m]
    (if (keyword? (:type m))
      (:type m)
      (try
        (let [ms (edn/read-string (:type m))]
          (if (and (map? ms) (= (:type ms) :numeric))
            :numeric
            (do 
              (bad-widget-type ":type :numeric should be like {:type :numeric :dps 0 :min 0 :max 100}" )
              (js/console.log "culprit is: " (pr-str m))
              :unsupported)))
        (catch :default e
          (bad-widget-type "Invalid type")
          :unsupported))))
  :default :unsupported)

(comment
  (map? nil)
  (edn/read-string "{:a 1}")
  (nil? (edn/read-string "{:a}"))
  )
  
; Create a radio-button-group widget given a widget inputs map - for example:
(comment
  (def widget-inputs-map
    "Example of a widget-inputs-map"
    {:factor-key :kidney/sex
     :levels [{:level :male
               :level-name "Male"}
              {:level :female
               :level-name "Female"}]
     :default :male
     :optional :no
     :type :radio}))

; factors with a nil type field have no widget
(defmethod widget nil [_]
  nil)

(def mb 5)
(def mt 0)
(def label-width "label column grid-size" 5)
(def widget-width "Widget column grid-size" 7)

(defmethod widget :reset
  [w]
  [:> bs/Row {:style {:display "flex" :align-items  "center" :margin-bottom mb}}
   [:> bs/Col {:xs label-width}]
   [:> bs/Col {:xs widget-width}
    (bsio/reset-button {:on-click #(rf/dispatch [::events/reset-inputs])})]])


(defn radio
  [{:keys [factor-name factor-key levels default type vertical optional boxed] :as w}]
  (let [value-f (fn [] @(rf/subscribe [factor-key]))
        optional? (some? optional)
        boxed? (some? boxed)]
;    (locals)
;    (when optional? (?-> [optional levels] ::button-levels))
    [:> bs/Row {:style {:display "flex" :align-items  "center" :margin-bottom mb}}
     [:> bs/Col {:xs label-width
                 :style {:display "flex" :justify-content "flex-end"}}
      [:> bs/Form.Label {:style {:font-weight "bold" :text-align "right" :margin-bottom mb :line-height 1.2}}
       [:div
        (:factor-name w)]]]

     [:> bs/Col {:xs widget-width}

      (bsio/radio-button-group {:id (key->id factor-key)
                                :vertical vertical
                                :value-f value-f
                                :optional optional?
                                :on-change #(rf/dispatch [factor-key
                                                          (keyword %)])
                                :buttons-f (fn [] (vals levels))})
      ]]))



; radio buttons allow fast selection between options
(defmethod widget :radio
  [w]
  [radio (assoc w :vertical false)])

; radio buttons allow fast selection between options
(defmethod widget :v-radio
  [w]
  [radio (assoc w :vertical true)])


; dropdowns are similar to radio buttons but are useful when a radio-button-group
; would is too wide
(defmethod widget :dropdown
  [{:keys [factor-name factor-key levels default type] :as w}]
  (let [value-f (fn [] @(rf/subscribe [factor-key]))]
    
    [:> bs/Row {:style {:display "flex" :align-items  "center" :margin-bottom mb}}
     [:> bs/Col {:xs label-width
                 :style {:display "flex" :justify-content "flex-end"}}
      [:> bs/Form.Label {:style {:font-weight "bold"  :text-align "right" :line-height 1.2}}
       (:factor-name w)]]
     [:> bs/Col {:xs widget-width}
      (bsio/dropdown {:id (key->id factor-key)
                      :value-f value-f
                      :on-change #(rf/dispatch [factor-key
                                                (keyword %)])
                      :buttons-f (fn [] (vals levels))})]]))

; Note that the numeric-input arguments min, mapx, dps etc. come from the map encoded as a string inside the type column



(comment
  (def value-f (fn [] @(rf/subscribe [:lung/bmi])))
  (#(rf/dispatch [:lung/bmi %]) 60)
  (value-f)
  
  (widget {:type :radio})
  (widget {:type :foo}))

(defmethod widget :unsupported
  [{:keys [type :as m]}]
   (js/console.log "unsupported widget-type: "  m)
  [:div  type " widget badly configured"])

(defmethod widget :param
  [_]
  nil)

(defmethod widget :none
  [_]
  nil)
;------------------------
;
(comment
  (widget {:factor-key "foo" :type :radio})

  (bsio/radio-button-group {:id "Sex"
                            :value-k :sex
                            :value-f (fn [] :male)
                            :on-change identity
                            :buttons-f (fn [] [{:key :male :value :male :level-namel "Male"}
                                               {:key :female :value :female :level-name "Female"}])})


  (bsio/radio-button-group {:id "Sex"
                            :value-k :sex          ; factor
                            :value-f (fn [] :male) ; default value? 
                            :on-change identity    ; e.g. #(rf/dispatch [:set-factor-level %])
                            :buttons-f (fn [] [{:key :male
                                                :level :male
                                                :level-name "Male"}
                                               {:key :female
                                                :level :female
                                                :level-name "Female"}])})

  )

