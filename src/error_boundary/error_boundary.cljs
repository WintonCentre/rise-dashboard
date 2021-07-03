(ns error-boundary.error-boundary
  (:require [reagent.core :as r]
            ;[reagent.impl.component :as comp]
            ;[reagent.impl.util :as util]
            ;[goog.object :as gobj]
            ))

(defn err-boundary
  " See https://lilac.town/writing/modern-react-in-cljs-error-boundaries/ for Reactv16"
  [& children]
  (let [err-state (r/atom nil)]
    (r/create-class
     {:display-name "ErrBoundary"
      :component-did-catch (fn [err info]
                             (reset! err-state [err info]))
      :get-derived-state-from-error (fn [err] [:p "The error is " (pr-str err)])
      :reagent-render (fn [& children]
                        (if (nil? @err-state)
                          (into [:<>] children)
                          (let [[err info] @err-state]
                            [:<>
                             [:pre [:code (pr-str info)]]
                             [:pre [:code (pr-str err)]]])))})))


;; This for Reactv15

#_;; (c) 2016 Paulus Esterhazy
;;
;; License: MIT

;; Store in a file called error_boundary/error_boundary.cljs in your classpath.
;;
;; Tested with reagent 0.6.0
;;
;; See
;;
;; https://github.com/reagent-project/reagent/issues/272
;;
;; https://github.com/facebook/react/blob/15-stable/src/renderers/shared/stack/reconciler/__tests__/ReactErrorBoundaries-test.js
;;
;; https://github.com/facebook/react/issues/2461

  (def error-boundary
    "Wrapper component for recovering from exceptions in downstream
  render fns. Creates an error boundary that prevents exceptions from corrupting
  the React component hierarchy.

  Use this component to wrap a single reagent (root) component. Any exception
  thrown in downstream render fns will be caught and logged. The component's
  child and its children will not be rendered.

  This is useful in a reloading-based development workflow.

  Example usage:

  (ns my-ns
    (:require [error-boundary.error-boundary :refer [error-boundary]]))

  (r/render-component (fn [] [error-boundary [root]])
                      (.. js/document (querySelector \"#container\")))

  Note that this relies on the undocumented unstable_handleError API introduced
  in React 15.

  This componenet may have performance implications, so it is recommended to
  enable it only during development."
    (r/adapt-react-class (comp/create-class
                          {:getInitialState
                           (fn []
                             #js {:error false})

                           :unstable_handleError
                           (fn [e]
                             (this-as this
                                      (if (ex-data e)
                                        (js/console.error (pr-str e))
                                        (js/console.error e))
                                      (.setState this #js {:error true})))

                           :render
                           (fn []
                             (this-as this
                                      (let [children (.. util/react
                                                         -Children
                                                         (toArray (.. this -props -children)))]
                                        (when (not= 1 (count children))
                                          (js/console.warn "Component error-boundary requires a single child component. Additional children are ignored."))
                                        (if (gobj/get (.. this -state) "error")
                                          (do
                                            (js/console.warn "An error occurred downstream (see errors above). The element subtree will not be rendered.")
                                            nil)
                                          (first children)))))})))