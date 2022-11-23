(ns ^:figwheel-hooks learn-cljs.interoperability-with-js
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

;; TODO: en la REPL cambiar el namespace por default con siguiente.. (ns learn-cljs.interoperability-with-js)

(defonce app-state (atom {:text "Interoperabilidad con JavaScript!"}))

(defn get-app-element []
  (gdom/getElement "app"))

;; definimos en clojure un vector con estructuras map como elementos
(def personajes [{:id 1 :nombre "Hisoka"}
                 {:id 2 :nombre "Gon"}
                 {:id 9 :nombre "Killua"}
                 {:id 15 :nombre "Kurapika"}])

;; clj->js
;; - transformamos código Clojure a un objeto JavaScript
(def personajes-js (clj->js personajes))

;; #js
;; - definimos en javascript un array
(def examen-cazador #js ["Fase Preliminar"
                          "Primera Fase"
                          "Un juego a Medianoche"
                          "Tercera Fase"
                          "Cuarta Fase"
                          "Fase Final"])

;; aget
;; - obtenemos un elemento de un array de javascript
(def primera-fase-examen-cazador
  (aget examen-cazador 0))

;; .nombreFuncionJavascript
;; - usamos métodos/funciones primitivas de javascript con arrays (de javascript)
(def numero-de-etapa-final-examen-cazador
  (.indexOf examen-cazador "Fase Final"))

;; aset
;; - generamos efecto de lado sobre un array de javascript, cambiando el valor de un elemento,
;; - si una variable está bindeada/vinculada a un objeto javascript, entonces puede mutar (por tanto no es inmutable..)
(defn renombrar-fase-examen [numero-fase nuevo-nombre]
  (aset examen-cazador numero-fase nuevo-nombre))

;; #js
;; - definimos un objeto en JavaScript
(def presidente-cazadores
  #js {"id" 1
       "nombre" "Isaac Netero",
       "rango" "Presidente de Asociación de Cazadores",
       "edad" 110,
       "habilidades" ["fuerza superhumana"
                      "velocidad"
                      "resistencia"],
       "nen" {"tipo" "intensificación",
              "poderes" ["Primera Mano"
                         "Tercera Mano"
                         "100-Tipo de Guanyin"]}})

;; .-nombreAtributo
;; - obtenemos el valor asociado a un atributo de un objeto javascript con .-
(def edad-presidente-cazadores
  (.-edad presidente-cazadores))

;; js->clj
;; - transformamos un objeto javascript a clojure, para usar las operaciones propias de clojure
;; - se suele sugerir esto para aprovechar la inmutabilidad característica de Clojure
(def habilidades-presidente-cazadores
  (map
   (fn [habilidad] (str "habilidad: " habilidad))
   (js->clj (.-habilidades presidente-cazadores))))

;; TODO: no funciona, devuelve nil
(println
 (.. presidente-cazadores -nen -tipo))

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Probando la interoperabilidad entre ClojureScript y JavaScript"]])

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(mount-app-element)

(defn ^:after-load on-reload []
  (mount-app-element))
