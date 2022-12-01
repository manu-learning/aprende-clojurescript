(ns ^:figwheel-hooks learn-cljs.temperature-converter
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]))

;; lógica
(defn fahrenheit->celsius [temperatura]
  (-> temperatura (- 32) (/ 1.8)))

(defn celsius->fahrenheit [temperatura]
  (-> temperatura (* 1.8) (+ 32)))

;; DOM, input
(def opcion-celsius (gdom/getElement "opcion-celsius"))
(def opcion-fahrenheit (gdom/getElement "opcion-fahrenheit"))
(def temperatura-ingresada (gdom/getElement "temperatura"))

;; DOM, output
(def temperatura-convertida (gdom/getElement "temperatura-convertida"))
(def unidad-temperatura (gdom/getElement "unidad-temperatura-elegida"))

(defn unidad-temperatura-elegida []
  (if (.-checked opcion-celsius)
    :celsius
    :fahrenheit))

(defn valor-temperatura-ingresada []
  (js/parseInt (.-value temperatura-ingresada)))

(defn actualizar-temperatura [nueva-temperatura]
  (gdom/setTextContent temperatura-convertida nueva-temperatura))

(defn actualizar-unidad-temperatura [nueva-unidad]
  (gdom/setTextContent unidad-temperatura nueva-unidad))

;; TODO: utilizar la macro (->) u similar y mejorar la expresividad
;; función para el event handler
(defn convertir-temperatura [_]
  (if (= :celsius (unidad-temperatura-elegida))
    (do (actualizar-temperatura (celsius->fahrenheit (valor-temperatura-ingresada)))
        (actualizar-unidad-temperatura "C"))
    (do (actualizar-temperatura (fahrenheit->celsius (valor-temperatura-ingresada)))
        (actualizar-unidad-temperatura "F"))))

;; TODO: quizás do no sea la mejor opción, porque no necesariamente la creación de la escucha de eventos debe ser en ese orden
(defonce conversor-temperatura-loaded
  (do
    (gevents/listen temperatura-ingresada "keyup" convertir-temperatura)
    (gevents/listen opcion-celsius "click" convertir-temperatura)
    (gevents/listen opcion-fahrenheit "click" convertir-temperatura)))

(conversor-temperatura-loaded)
