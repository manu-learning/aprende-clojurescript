(ns ^:figwheel-hooks learn-cljs.counter
  (:require-macros [hiccups.core :as hiccups])
  (:require
   [hiccups.runtime]
   [goog.dom :as gdom]
   [goog.events :as gevents]))

(defonce app-state (atom {:contador 0}))

;; UI
(def app-container (gdom/getElement "app"))

(defn render-counter [state]
  [:div
   [:p "Contador: " state]
   [:button {:id "button-incrementar"} "incrementar"]
   [:button {:id "button-decrementar"} "decrementar"]])

(defn set-app-html! [html]
  (set! (.-innerHTML app-container) html))

(defn render-app! [state]
  (set-app-html!
   (hiccups/html
    [:div {:class "container"}
     (render-counter (:contador state))])))

(defn incrementar-contador-en [cantidad]
  (swap! app-state #(update % :contador + cantidad)))

(defn decrementar-contador-en [cantidad]
  (swap! app-state #(update % :contador - cantidad)))

(defn listener-events-contador [evento]
  (condp = (aget evento "target" "id")
    "button-incrementar" (incrementar-contador-en 1)
    "button-decrementar" (decrementar-contador-en 1)))

;; add-watch
;; - 1º parámetro, variable de tipo átomo que tenemos interés en observar los cambios de Estado
;; - 2º parámetro, keyword con la que luego podemos remover la función watch (dejará de observar)
;; - 3º parámetro, la función watch, la que tiene los cambios de estado (del estado anterior y el nuevo estado)
;; de la función watch los parámetros de mayor interés suelen ser (3º param) anterior-valor y (4º param) nuevo-valor
(defn watch-state-contador []
  (add-watch app-state :counter-observer
             (fn [key atom anterior-valor nuevo-valor]
               (render-app! nuevo-valor))))

(defonce app-inicializada?
  (do
    (gevents/listen app-container "click" listener-events-contador)
    (watch-state-contador)
    (render-app! @app-state)
    true))

app-inicializada?
