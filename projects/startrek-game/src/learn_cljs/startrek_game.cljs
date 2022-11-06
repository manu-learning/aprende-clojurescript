(ns ^:figwheel-hooks learn-cljs.startrek-game
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(defonce app-state (atom {:game "Star Trek Enterprise"
                          :escenario {}}))

;; dataset
(def escenarios
  [{:nombre :sugerencias
    :titulo "Sugerencias del juego"
    :dialogo "¿Sabías el control de navegación que aparece debajo te da acceso completo a la nave espacial?"}
   {:nombre :explorar
    :titulo "Explorando nuevos mundos"
    :dialogo "Estamos buscando la posiblidad de nuevos mundos en las cercanías"}
   {:nombre :avanzar
    :titulo "Avanzando por el espacio"
    :dialogo "Cambiamos la a velocidad de warp 9,9 (3,27 mil millones de km/h)"}])

;; lógica
(defn escenario-detalle [nombre]
  (first (filter #(= (% :nombre) nombre) escenarios)))

(defn control-de-navegacion [opcion]
  (swap! app-state
         assoc :escenario (escenario-detalle opcion)))

;; Componentes UI
(defn foto-de-tripulante [nombre]
  [:img {:src (str "images/tripulacion/" nombre ".jpg")
         :class "object-contain h-36"}])

(defn escenario [nombre]
  [:div {:class "w-full max-w-screen-sm bg-gray-600 p-10 rounded-xl ring-8 ring-white ring-opacity-40"}
   [:div {:class "flex justify-between"}
    [:div {:class "flex flex-col"}
     [:h1 {:class "text-6xl font-bold"} (get-in @app-state [:escenario :titulo])]
     [:span (get-in @app-state [:escenario :dialogo])]]
    [foto-de-tripulante "data"]]])

(defn boton-sugerencias []
  [:button {:class "bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
            :on-click #(control-de-navegacion :sugerencias)}
   [:span {:class "uppercase"} "sugerencias"]])

(defn boton-avanzar []
  [:button {:class "bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
            :on-click #(control-de-navegacion :avanzar)}
   [:span {:class "uppercase"} "avanzar nave"]])

(defn boton-explorar []
  [:button {:class "bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
            :on-click #(control-de-navegacion :explorar)}
   [:span {:class "uppercase"} "explorar mundos"]])

(defn menu-opciones []
  [:div {:class "flex flex-col space-y-6 w-full max-w-screen-sm bg-gray-800 mt-10 rounded-xl ring-8 ring-gray-600 ring-opacity 40"}
   [:div {:class "flex justify-between"}
    [:div {:class "flex flex-row"}
     [boton-sugerencias]
     [boton-avanzar]
     [boton-explorar]]]]
  )

(defn app []
  [:div {:class "flex flex-col items-center justify-center w-screen min-h-screen bg-gradient-to-br from-gray-800 to-gray-600 text-white"}
   [:h1  {:class "mb-10 text-6xl font-bold w-full text-center"} (:game @app-state)]
   [escenario]
   [menu-opciones]])

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount-app []
  (swap! app-state
         assoc :escenario (escenario-detalle :sugerencias))
  (rdom/render [app] (get-app-element)))

(mount-app)

(defn ^:after-load on-reload []
  (mount-app)
)
