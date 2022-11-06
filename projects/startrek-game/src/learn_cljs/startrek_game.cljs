(ns ^:figwheel-hooks learn-cljs.startrek-game
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(defonce app-state (atom {:game "Star Trek Enterprise"
                          :escenario {}
                          :bitacora []}))

;; dataset
(def escenarios
  [{:nombre :bitacora
    :interaccion true
    :titulo "Bitacora Espacial"
    :dialogo "Bitacora de comandos de la USS Enterprise"}
   {:nombre :reglas
    :interaccion false
    :asistente "worf"
    :titulo "Reglas de la Flota Estelar"
    :dialogo "La flota estelar es muy rigurosa, por tanto.."}
   {:nombre :explorar
    :interaccion true
    :titulo "Explorando nuevos mundos"
    :dialogo "Buscando posiblidad de nuevos mundos en las cercanías"}
   {:nombre :mision
    :interaccion false
    :asistente "data"
    :titulo "Misión infiltración"
    :dialogo "Tu misión asignada es infiltrarse entre los romulanos"}])

;; lógica
;; TODO: Refactor, no es intuitivo usar (bitacora)
(defn bitacora []
  (get-in @app-state [:bitacora]))

;; TODO: Refactor quizás usando (update-in) ó la macro (->)
(defn agregar-bitacora [bitacora-nueva]
  (swap! app-state
         assoc :bitacora
         (conj (bitacora) bitacora-nueva)))

(defn escenario-detalle [nombre]
  (first (filter #(= (% :nombre) nombre) escenarios)))

(defn control-de-mando [opcion]
  (agregar-bitacora (escenario-detalle opcion))
  (swap! app-state
         assoc :escenario (escenario-detalle opcion)))

(defn limpiar-bitacora []
  (swap! app-state assoc :bitacoras []))

(defn modo-interactivo? []
  (= (get-in @app-state [:escenario :interaccion]) true))

(defn usando-bitacora? []
  (= (get-in @app-state [:escenario :nombre]) :bitacora))

;; Componentes UI
(defn foto-de-tripulante [nombre]
  [:img {:src (str "images/tripulacion/" nombre ".jpg")
         :class "object-contain h-36"}])

(defn foto-asistente-del-escenario []
  [:span (foto-de-tripulante (get-in @app-state [:escenario :asistente]))])

(defn terminal-escribir
  ([mensaje animacion]
  [:p {:class "animate-typing font-mono w-0 overflow-hidden whitespace-nowrap"}
   [:span mensaje]
   [:span {:class "animate-blink border-r-4 border-indigo-600"} " "]])
  ([mensaje]
   [:p {:class "font-mono"}
    [:span mensaje]]))

(defn escribir-bitacora []
  [:p
   [terminal-escribir (get-in @app-state [:escenario :dialogo])]
   ;;(map (fn [elem] (-> elem :titulo terminal-escribir )) (bitacora))
   (map #(-> % :titulo terminal-escribir) (bitacora))
   [:span {:class "animate-blink border-r-4 border-indigo-600"} " "]])

(defn dialogo-escenario-actual []
  [:span (get-in @app-state [:escenario :dialogo])])

(defn escenario-interactivo []
  [:div {:class "w-full h-96 max-w-screen-sm bg-gray-900 p-1 rounded-xl ring-8 ring-white ring-opacity-40"}
   [:div {:class "flex justify-between"}
    [:div {:class "flex flex-col"}
     (if (usando-bitacora?)
       [escribir-bitacora]
       [terminal-escribir (dialogo-escenario-actual) :blink])]]])

(defn escenario-estatico [nombre]
  [:div {:class "w-full max-w-screen-sm bg-gray-600 p-10 rounded-xl ring-8 ring-white ring-opacity-40"}
   [:div {:class "flex justify-between"}
    [:div {:class "flex flex-col"}
     [:h1 {:class "text-4xl font-bold"} (get-in @app-state [:escenario :titulo])]
     [:span (get-in @app-state [:escenario :dialogo])]]
    [foto-asistente-del-escenario]]])

;; TODO: Refactor, si hubieran varios comandos, repitiríamos el css
(defn boton-mision []
  [:button {:class "bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
            :on-click #(control-de-mando :mision)}
   [:span {:class "uppercase"} "mision"]])

(defn boton-explorar []
  [:button {:class "bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
            :on-click #(control-de-mando :explorar)}
   [:span {:class "uppercase"} "exploracion"]])

(defn boton-reglas []
  [:button {:class "bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
            :on-click #(control-de-mando :reglas)}
   [:span {:class "uppercase"} "reglas"]])

(defn boton-bitacora []
  [:button {:class "bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
            :on-click #(control-de-mando :bitacora)}
   [:span {:class "uppercase"} "bitacora"]])

(defn menu-de-comando []
  [:div {:class "flex flex-col space-y-6 w-full max-w-screen-sm bg-gray-800 mt-10 rounded-xl ring-8 ring-gray-600 ring-opacity 40"}
   [:div {:class "flex justify-between"}
    [:div {:class "flex flex-row"}
     [boton-mision]
     [boton-reglas]
     [boton-explorar]
     [boton-bitacora]]]])

(defn app []
  [:div {:class "flex flex-col items-center justify-center w-screen min-h-screen bg-gradient-to-br from-gray-800 to-gray-600 text-white"}
   [:h1  {:class "mb-10 text-6xl font-bold w-full text-center"} (:game @app-state)]
   (if (modo-interactivo?)
     [escenario-interactivo]
     [escenario-estatico])
   [menu-de-comando]])

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount-app []
  (swap! app-state assoc :escenario (escenario-detalle :mision))
  (rdom/render [app] (get-app-element)))

(mount-app)

(defn ^:after-load on-reload []
  (mount-app))
