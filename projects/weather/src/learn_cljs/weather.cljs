(ns ^:figwheel-hooks learn-cljs.weather
  (:require
   [goog.dom :as gdom]
   [reagent.dom :as rdom]
   [reagent.core :as r]
   [ajax.core :as ajax]))

(defonce app-state (r/atom {:title "ManuWeather"
                            :postal-code ""
                            :city "Buenos Aires"
                            :country-code "AR"
                            :temperaturas {:hoy {:label "Hoy" :value []}}
                            :temperatures {:today {:label "Today"
                                                   :value nil}
                                           :tomorrow {:label "Tomorrow"
                                                      :value nil}}}))

;; XPath: //h1
(defn title []
  [:h1 (:title @app-state)])

;; XPath: //div[@class='temperature']/div[@class='value'] | //div[@class='temperature']/h2
(defn temperature [temp]
  [:div {:class "temperature"}
   [:div {:class "value"}
    (:value temp)]
   [:h2 (:label temp)]])

;; .-target obtiene el elemento input del DOM
;; .-value obtiene el valor del atributo value del input (input que previamente capturamos con .-target)

;; reescribo la escucha del evento on-change de diferentes formas para que se entienda mejor como funciona
(comment
  "si usamos la función anónima acortada"
  #(swap! app-state
          assoc :postal-code (.-value (.-target %)))

  "si usamos la función anónima acortada y la macro arrow"
  #(swap! app-state
          assoc :postal-code (-> % .-target .-value))

  "si usamos la función anónima"
  (fn [x]
    (swap! app-state assoc
           :postal-code (.-value (.-target x))))

  "si usamos la función anónima y la macro arrow"
  (fn [x]
    (swap! app-state assoc
           :postal-code (-> x .-target .-value))))

(def actualizar (fn [x]
  (swap! app-state assoc
         :postal-code (-> x .-target .-value))))

(defn pronostico-del-dia[]
  [:div {:class "w-full max-w-screen-sm bg-white p-10 rounded-x1 ring-8 ring-white ring-opactity-40"}
   [:div "temperatura"]])

(defn buscador-de-ciudad []
  [:div {:class "buscador-ciudad"}
   [:input {:type "text"
            :class "border-b-4 border-b-blue-900 rounded-lg p-2 text-x1 focus:outline-none"
            :placeholder "Ingresar nombre de ciudad"}]
   [:button {:on-click get-forecast!
             :class "border-2 border-teal-800 rounded-lg p-2 text-xl text-center transition ease-in-out delay-150 active:scale-95 bg-gradient-to-r from-emerald-500 to-teal-500 drop-shadow-md hover:drop-shadow-2xl"}
    "Mostrar Clima"]])

;;{:on-click get-forecast!} "Ir"

;; - XPath: //div[@class='postal-code'][h3|input|button]
;;
;; la función de on-change
;; =======================
;;
;; - para facilitar la interpretación de la lógica que contiene,
;; acotar el dominio de la función posta-code sólo al elemento [:input ]
;;
;; - se activa/despierta y "ejecuta la función que le asociamos".. ante "cada evento de cambio de estado" del input text/campo de texto
;; (osea cuando escribamos en él)
;;
;; - tiene asociada una "función anónima acortada" #(), que nos evita la sintáxis de una "función nombrada" y la declaración sus parámetros
;; dónde el símbolo % representará el parámetro que solemos declarar en una función nombrada (fn [parametro] cuerpo-funcion)
;;
;; - la función anónima utiliza la macro arrow (->), que nos evita el uso de paréntesis que es típico en "composición de funciones"
;; y es útil si son funciones cortas
;;
;; Ej. cada vez que escribamos un caracter en el campo de texto, osea en el elemento del DOM [:input ..]
;; 1. se invocará a la función anónima acortada #() asociada en el atributo on-change de ese campo de texto
;; 2. la función asociada en "on-change"
;; 2.1 "modificará el valor asociado a la keyword" llamada :postal-code incluída en app-state
;; 2.2 el paso anterior, usará el valor obtenido del campo de texto con (-> % .-target .-value)
;; dónde el símbolo % representa el parámetro de la función anónima dónde está incluida la función (swap! ..)
;; si fuese una función anónima sería.. (fn [param] (swap! app-state assoc :postal-code (-> param .-target .-value)))
;; ó bien también.. (fn [param] (swap! app-state assoc :postal-code (.-value (.-target param))))
(defn postal-code []
  [:div {:class "postal-code"}
   [:h3 "Escribi tu código postal"]
   [:input {:type "text"
            :placeholder "Código Postal"
            :value (:postal-code @app-state)
            :on-change #(swap! app-state
                               assoc :postal-code
                               (-> % .-target .-value))}]])

; [:button {:on-click get-forecast!} "Ir"]

;; XPath: //div[@class='app']/div[@class='temperatures']
;;
;; la función vals
;; ===============
;;
;; -  devuelve una secuencia de los "valores asociados a una estructura map"
;; Ej. (vals {:a "foo", :b "bar"})
;; en éste caso devolvería ("foo" "bar")
(defn app []
  [:div {:class "flex flex-col items-center justify-center w-screen min-h-screen text-gray-700 p-10 bg-gradient-to-br from-pink-200 via-purple-200 to-indigo-200"}
   [title]
   [buscador-de-ciudad]
   [pronostico-del-dia]
   [:div {:class "temperatures"}
    (for [temp (vals (:temperatures @app-state))]
      [temperature temp])]
   [postal-code]])

;; lógica para la API Rest
;; =======================
;;
;; 1. "bindeamos/vinculamos a las variables" today y tomorrow, con el valor de la temperatura que devuelve el response (respuesta)
;;
;;   - con la función (get-in estructura-anidada secuencia-keys) "accedemos a los valores" de la "estructura map anidada del response"
;;
;;   - una "estructura map anidada" son estructuras dentro de otras, de la forma {:a {:b {:x 1, :y 2}, :c {:x 1, :y 2}}}
;;
;;   - una "secuencia de keys" es sería la ruta para llegar al valor indicando implícitamente el "nivel de profundidad" al que accedemos,
;;     por ejemplo con [:a :b :x] la ruta de acceso sería en éste orden.. a -> b -> x -> 2
;;
;; 2. "modificamos el estado de la aplicación de forma atómica", con los valores asociados de las variables que declaramos e inicializamos en la función (let ..)
;; y.. estas variables "sólo viven en el Scope de let"
;;
;;   - utilizamos la función (swap! ..) porque la variable app-state "guarda la referencia" a un "tipo de dato Atom"
;;
;;   - con la función (update-in ..) "modificamos la estructura map anidada", y devuelve una "nueva estructura map"
(defn handle-response [resp]
  (let [today (get-in resp ["list" 0 "main" "temp"])
        tomorrow (get-in resp ["list" 8 "main" "temp"])]
    (swap! app-state
           update-in [:temperatures :today :value] (constantly today))
    (swap! app-state
           update-in [:temperatures :tomorrow :value] (constantly tomorrow))))

;; TODO: falta lógica
(defn es-hoy? [dia]
  true)

(defn handle-temperaturas [resp]
  (let [temperaturas-del-dia (filter es-hoy? (get-in resp ["list"]))]
    (swap! app-state
           update-in [:temperatures :today :value] temperaturas-del-dia)))

;; 15 -> 6, 9, 12, 15, 18, 21
;; 16 -> 0, 3, 6, 9, 15, 18, 21
;; 17

;; 1. definimos una función que no recibe parámetros
;; 2. bindeamos/asociamos el valor de la variable postal-code del estado de la app
;; 3. realizamos una acción GET a la API
;;
;; 3.1 el cuerpo del request/solicitud en :params (acá específicamos que queremos que nos devuelva en el response)
;; 3.2 la respuesta/response de la solicitud se la pasamos a handle-response que actualizará el estado de la aplicación
;; (en nuestro caso el handle-response lo llamamos handle-temperaturas)
(defn get-forecast! []
  (let [postal-code (:postal-code @app-state)]
    (ajax/GET "http://api.openweathermap.org/data/2.5/forecast"
              {:params {"q" postal-code
                        "appid" "14bfd5131980f3464f4d59cc8d1a3eba"
                        "units" "metric"}
               :handler handle-temperaturas})))

(defn mount-app-element []                                 ;; <3>
  (rdom/render [app] (gdom/getElement "app")))

(mount-app-element)

(defn ^:after-load on-reload []                            ;; <4>
  (mount-app-element))
