(ns ^:figwheel-hooks learn-cljs.salary-calculator
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [reagent.core :as r]
   [reagent.ratom :as ratom]
   [reagent.dom :as rdom]))

;; - cada vez que un componente cambie el estado de la app (state), Reagent volverá a renderizar a ese componente
;; porque desreferenció el estado de la app (state)
(defonce state
  (r/atom {:inputs {:salario-minimo 5000}}))

(defn horas-trabajadas-input []
  [:div.input-horas-trabajadas
   [:label "Horas"]
   [:input {:type "number" :min 1 :step 1}]])

(defn antiguedad-laboral-input []
  [:div.input-antiguedad-laboral
   [:label "Años en la empresa"]
   [:input {:type "number" :min 1 :step 1}]])

;; - podemos darle un valor por default usando la Estructura de Datos del state
;; agregando la entrada :value en el input que desreferencie al state
;;
;; - React no permitirá cambiar el valor en la UI, porque su valor está vinculado a la UI State
;; (por lo anterior de la entrada :value ..)
;;
;; - el problema anterior se resuelve agregando la entrada :on-change y pasandole la funcion (swap!)
;; (OJO! porque el cambio del valor en el input salario-minimo cambiará el estado de la aplicación)
;;
;; it is not ideal for performance because every time state changes, Reagent will try to re-render this component.
;;
;; DUDA: <----------
;; 1. todavia no entiendo que problema tiene éste
;; se supone que un cambio en el input salario-minimo hace que se recarge el componente..
;; entonces incluí apropósito los componentes [horas-trabajadas-input] y [antiguedad-laboral-input]
;; para agregarles un valor desde la UI y luego cambiar salario-minimo pero.. no renderizó el componente, o si (?)
(defn salario-minimo []
  [:div.input-salario-minimo
   [horas-trabajadas-input]
   [antiguedad-laboral-input]
   [:label "Salario minimo"]
   [:input {:type "number"
            :value (get-in @state [:inputs :salario-minimo])
            :on-change #(swap! state assoc-in [:inputs :salario-minimo]
                               (.. % -target -value))}]])

;; 1. creamos el cursor antes de montar el componente (en el momento que usamos r/cursor .. y lo viculamos a la variable valor)
;; 2. desreferenciamos el cursor, haciendo al componente reactivo (en la entrada :value)
;; 3. actualizamos el estado de la app (state) a través del cursor (en la entrada :on-change)
;;
;; Reagent provides a utility called a cursor. A cursor acts like a reactive atom that points to a specific location inside another reactive atom. When the value at that location is updated, the cursor is updated, and any component that dereferences the cursor is updated
;;
(defn salario-minimo* []
  (let [valor (r/cursor state [:inputs :salario-minimo])]
    (fn []
      [:div.input-salario-minimo
       [:label "Salario minimo"]
       [:input {:type "number"
                :value @valor
                :on-change #(reset! valor
                                   (.. % -target -value))}]])))

(defn confirmar-button []
  [:div.actions
   [:button {:type "submit"} "Confirmar"]])

;; podemos comentar con #_[componente]
(defn formulario []
  [:form
   #_[horas-trabajadas-input]
   #_[antiguedad-laboral-input]
   [salario-minimo]
   [confirmar-button]])

(defn app []
  [formulario])

;; TODO
(comment
  (defn posicion-laboral-select []
    [:div.select-posicion-laboral
     [:label "Posición Laboral"]])
  )

(rdom/render
 [app]
 (gdom/getElement "app"))


