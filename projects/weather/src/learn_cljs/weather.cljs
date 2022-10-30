(ns ^:figwheel-hooks learn-cljs.weather                    ;; <1>
  (:require
   [goog.dom :as gdom]
   [reagent.dom :as rdom]
   [reagent.core :as r]))

(defn hello-world []                                       ;; <2>
  [:div
   [:h1 {:class "app-title"} "Hello, World"]])

(defonce app-state (r/atom {:title "WhichWeather"
                            :postal-code ""
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
  #(swap! app-state assoc :postal-code (.-value (.-target %)))

  "si usamos la función anónima acortada y la macro arrow"
  #(swap! app-state assoc :postal-code (-> % .-target .-value))

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

;; XPath: //div[@class='postal-code'][h3|input|button]
(defn postal-code []
  [:div {:class "postal-code"}
   [:h3 "Escribi tu código postal"]
   [:input {:type "text"
            :placeholder "Código Postal"
            :value (:postal-code @app-state)
            :on-change #(swap! app-state assoc :postal-code (-> % .-target .-value))}]
   [:button "Ir"]])

;; XPath: //div[@class='app']/div[@class='temperatures']
(defn app []
  [:div {:class "app"}
   [title]
   [:div {:class "temperatures"}
    (for [temp (vals (:temperatures @app-state))]
      [temperature temp])]
   [postal-code]])

(defn mount-app-element []                                 ;; <3>
  (rdom/render [app] (gdom/getElement "app")))

(mount-app-element)

(defn ^:after-load on-reload []                            ;; <4>
  (mount-app-element))
