(ns ^:figwheel-hooks learn-cljs.todo-list
  (:require-macros [hiccups.core :as hiccups])
  (:require [goog.dom :as gdom]
            [goog.events :as gevents]
            [hiccups.runtime]
            [clojure.string :as str]))

;; (def lista-tareas [{:titulo "Cocinar torta" :prioridad 0 :done? false :editing? false}
;;                    {:titulo "Mirar TV" :prioridad 0 :done? false :editing? false}
;;                    {:titulo "Lavar la ropa" :prioridad 0 :done? false :editing? false}
;;                    {:titulo "Preparar la cena" :prioridad 0 :done? true :editing? false}])

(def lista-tareas [])

;; necesario por la dependencia de varias funciones que dependen de ésta,
;; pero su definición es posterior a la definición a ellas
(declare reload-app)

(def app-state
  {:tareas lista-tareas
   :tarea-seleccionada nil
   :editando? false})

;; select-keys, devolvemos de la estructura map sólo las entradas de la secuencia de keywords
(defn crear-tarea [tarea]
  (select-keys tarea [:titulo :prioridad :done?]))

;; conj, devolvemos una nueva lista-tareas con el elemento nueva-tarea agregado
(defn agregar-tarea [lista-tareas nueva-tarea]
  (conj lista-tareas nueva-tarea))

;; concat + subvec, especificamos que elementos extraer de un vector (similar a la función slice de javascript)
;; vec, transformamos la secuencia devuelta por concat a vector (porque usamos un vector de estructuras map)
(defn remover-tarea [lista-tareas numero-tarea]
  (vec
   (concat
    (subvec lista-tareas 0 numero-tarea)
    (subvec lista-tareas (inc numero-tarea)))))

;; assoc, devolvemos la misma estructura map con una entrada modificada
(defn reemplazar-tarea [lista-tareas numero-tarea nueva-tarea]
  (assoc lista-tareas numero-tarea (crear-tarea nueva-tarea)))

;; {:keys secuencia-keys} concepto de destructuring para estructuras asociativas map
(defn tarea-detalles [tarea]
  (let [{:keys [:titulo :prioridad]} tarea]
    (str "(" prioridad ") " titulo)))

;; UI
(def app-container (gdom/getElement "app-container"))

(defn render-button [nombre texto]
  [:div
   [:button {:id (str "button-" nombre)
             :class "inline-block px-6 py-2 border-2 border-gray-800 text-gray-800 font-medium text-xs leading-tight uppercase rounded-full hover:bg-black hover:bg-opacity-5 focus:outline-none focus:ring-0 transition duration-150 ease-in-out"}
    texto]])

;; con (name atributo) transformamos en string el nombre de la keyword
;; con (atributo tarea) usamos la keyword como función getter
(defn render-input-tarea [atributo tarea]
  [:div {:class "relative z-0 mb-6 w-full group"}
   [:label {:for (str "input-" (name atributo))
            :class "peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6"}
    (name atributo)]
   [:input {:type "text"
            :id (str "input-" (name atributo))
            :value (atributo tarea)
            :class "block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
            :placeholder " "}]])

(defn render-form-tarea [tarea]
  [:div {:class "block p-2 rounded-lg shadow-lg bg-gray-100 max-w-sm"}
   [:h3 {:class "text-gray-900 text-xl leading-tight font-medium mb-2"}
    "Tarea"]
   (render-input-tarea :titulo tarea)
   (render-input-tarea :prioridad tarea)
   [:div {:class "flex space-x-2 justify-center"}
    (render-button "guardar-tarea" "Guardar")
    (when (not-empty tarea)
      (render-button "eliminar-tarea" "Eliminar"))
    (render-button "cancelar-tarea" "Cancelar")
    ]])

(defn render-ver-tarea [numero-tarea tarea]
  [:div {:class "py-3 ml-2 w-full text-sm font-medium text-gray-900 dark:text-gray-300"}
    (tarea-detalles tarea)])

;; - selected? es true si la tarea está en selected de state, lógica en render-lista-tareas
;; - el atributo data-index lo usaremos para editar cada tarea
(defn render-lista-tareas-item [indice-tarea tarea tarea-seleccionada?]
  (let [editando? (:editando? tarea)]
    [:li {:class (str (when tarea-seleccionada? "tarea-seleccionada ") "lista-tareas-item w-full rounded-t-lg border-b border-gray-200 dark:border-gray-600")
          :data-index indice-tarea}
     [:div {:class "flex items-center pl-3"}
      (render-ver-tarea indice-tarea tarea)
      ]]))

;; tarea-seleccionada? es true si la tarea está en state
(defn render-lista-tareas [state]
  (let [{:keys [:tareas :tarea-seleccionada]} state]
    [:div
     [:h3 {:class "text-gray-900 text-xl leading-tight font-medium mb-2"}
      "Lista Tareas"]
     (if (empty? tareas)
       [:span "sin tareas"])
     [:ul {:class "w-48 text-sm font-medium text-gray-900 bg-white rounded-lg border border-gray-200 dark:bg-gray-700 dark:border-gray-600 dark:text-white"}
      (map-indexed (fn [index-tarea tarea]
                     (render-lista-tareas-item index-tarea tarea (= index-tarea tarea-seleccionada)))
                   tareas)
      ]]))

;; UI acciones
;; TODO: refactor
(defn get-field-value [id]
  (let [value (.-value (gdom/getElement id))]
    (when (seq value) value)))

(defn get-tarea-form-data []
  {:titulo (get-field-value "input-titulo")
   :prioridad (get-field-value "input-prioridad")})

;; tiene doble función, usamos para agregar una tarea nueva y para guardar la edición de una tarea
(defn listener-guardar-tarea [state]
  (reload-app
   (let [tarea (get-tarea-form-data)
         indice-tarea (:tarea-seleccionada state)
         state (dissoc state :tarea-seleccionada :editando?)]
     (if indice-tarea
       (update state :tareas reemplazar-tarea indice-tarea tarea)
       (update state :tareas agregar-tarea tarea)))))

(defn listener-nueva-tarea [state]
  (reload-app (-> state
                  (assoc :editando? true)
                  (dissoc :tarea-seleccionada))))

;; - con .. accedemos a un objeto javascript que es una estructura anidada, pasandole la secuencia de propiedades
;; - la propiedad -index la habíamos agregado nosotros en el DOM y figura como data-index
;; - tarea-seleccionada para guardar cambios (según si es una tarea nueva ó una tarea que editamos)
(defn listener-editar-tarea [evento state]
  (reload-app
   (let [indice-tarea (int (.. evento -currentTarget -dataset -index))]
     (assoc state :tarea-seleccionada indice-tarea :editando? true))))

(defn listener-eliminar-tarea [state]
  (reload-app (-> state
                  (update :tareas remover-tarea (:tarea-seleccionada state))
                  (dissoc :tarea-seleccionada :editando?))))

(defn listener-cancelar-tarea [state]
  (reload-app (assoc state :editando? false)))

;; - es común confundir getElementsByClass con getElementByClass
;; - usamos funciones anónimas como 3º parámetro en goog.events/listen porque necesitamos pasarle el parámetro state
(defn escuchar-eventos [state]
  (when-let [nueva-tarea-button (gdom/getElement "button-nueva-tarea")]
    (gevents/listen nueva-tarea-button "click" #(listener-nueva-tarea state)))
  (when-let [cancelar-tarea-button (gdom/getElement "button-cancelar-tarea")]
    (gevents/listen cancelar-tarea-button "click" #(listener-cancelar-tarea state)))
  (when-let [guardar-button (gdom/getElement "button-guardar-tarea")]
    (gevents/listen guardar-button "click" #(listener-guardar-tarea state)))
  (when-let [borrar-button (gdom/getElement "button-eliminar-tarea")]
    (gevents/listen borrar-button "click" #(listener-eliminar-tarea state)))
  ;; agregamos un listener en cada elemento de la lista de tareas
  ;; no podemos usar una función anónima usando #() porque necesitamos el parámetro evento y con % podría no quedar entendible
  (doseq [tarea (array-seq (gdom/getElementsByClass "lista-tareas-item"))]
    (gevents/listen tarea "click"
                    (fn [evento] (listener-editar-tarea evento state)))))

(defn set-app-html [html]
  (set! (.-innerHTML app-container) html))

;; (get-in state [:tareas (:tarea-seleccionada state)] {})
;; 1) primer parámetro accedemos al estado de la aplicación que es una estructura map con 3 entradas
;; 2) segundo parámetro secuencia de keywords
;; 2.1) :tareas es un vector con estructuras map
;; 2.2) (:tarea-seleccionada state) obtenemos el índice de la estructura map seleccionada
;; el índice lo obtuvimos previamente en on-editar-tarea
;; 3) tercer parámetro {} la estructura map vacía se devuelve si la secuencia de keywords no existe
(defn render-app [state]
  (set-app-html
   (hiccups/html
    [:div {:class "container"}
     [:h3 {:class "mb-4 font-semibold text-gray-900 dark:text-white"}
      "Manu To-Do List"]
     (render-lista-tareas state)
     (render-button "nueva-tarea" "Nueva Tarea")
     (if (:editando? state)
       (render-form-tarea (get-in state [:tareas (:tarea-seleccionada state)] {})))])))

(defn reload-app [state]
  (render-app state)
  (escuchar-eventos state))

(reload-app app-state)
