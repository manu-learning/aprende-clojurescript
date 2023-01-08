(ns ^:figwheel-hooks learn-cljs.salary-calculator
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [reagent.core :as r]
   [reagent.ratom :as ratom]
   [reagent.dom :as rdom]))

(defn- current-date-string [d]
  (let [pad-zero #(.padStart (.toString %) 2 "0")
        y (.getFullYear d)
        m (-> (.getMonth d) inc pad-zero)
        d (pad-zero (.getDate d))]
    (str y "-" m "-" d)))

(defn sueldo-inicial []
  {:codigo-empleado 0
   :salario-minimo 5000
   :fecha-vigencia (current-date-string (js/Date.))})

;; - cada vez que un componente cambie el estado de la app (state), Reagent volverá a renderizar a ese componente
;; porque desreferenció el estado de la app (state)
(defonce state
  (r/atom {:inputs (sueldo-inicial)
           :historial {}}))

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
(comment
  (defn salario-minimo []
    [:div.input-salario-minimo
     [:label "Salario minimo"]
     [:input {:type "number"
              :value (get-in @state [:inputs :salario-minimo])
              :on-change #(swap! state assoc-in [:inputs :salario-minimo]
                                 (.. % -target -value))}]]))


;; 1. creamos el cursor antes de montar el componente (en el momento que usamos r/cursor .. y lo viculamos a la variable valor)
;; 2. desreferenciamos el cursor, haciendo al componente reactivo (en la entrada :value)
;; 3. actualizamos el estado de la app (state) a través del cursor (en la entrada :on-change)
;;
;; Reagent provides a utility called a cursor. A cursor acts like a reactive atom that points to a specific location inside another reactive atom. When the value at that location is updated, the cursor is updated, and any component that dereferences the cursor is updated
;;
(defn salario-minimo-input []
  (let [valor (r/cursor state [:inputs :salario-minimo])]
    (fn []
      [:div.input-salario-minimo
       [:label "Salario minimo"]
       [:input {:type "number"
                :value @valor
                :on-change #(reset! valor
                                   (.. % -target -value))}]])))

(defn fecha-vigencia-input []
  (let [valor (r/cursor state [:inputs :fecha-vigencia])]
    (fn []
      [:div.input-fecha-vigencia
       [:label "Fecha vigencia"]
       [:input {:type "date"
                :value @valor
                :on-change #(reset! valor
                                    (.. % -target -value))}]])))

(defn codigo-empleado-input []
  (let [valor (r/cursor state [:inputs :codigo-empleado])]
    (fn []
      [:div.input-codigo-empleado
       [:label "Código de Empleado"]
       [:input {:type "number"
                :value @valor
                :on-change #(reset! valor
                                    (.. % -target -value))}]])))

(defn codigo-empleado-input* []
  [:div.input-codigo-empleado
   [:label "Código de Empleado"]
   [:input {:type "number"}]])

(defn confirmar-button []
  [:div.actions
   [:button {:type "submit"} "Confirmar"]])

;; TODO: el update-in en la fecha-vigencia debería crear un vector (por el momento es una lista)
(defn confirmar-formulario [state]
  (let [{:keys [fecha-vigencia codigo-empleado salario-minimo]} (:inputs state)
        fecha-actual (js/Date.now)]
    (-> state
        (update-in [:historial fecha-actual] #(conj % {:codigo-empleado codigo-empleado
                                                       :salario salario-minimo
                                                       :fecha-vigencia fecha-vigencia}))
        (assoc :inputs (sueldo-inicial)))))

;; podemos comentar con #_[componente]
(defn formulario []
  [:form {:on-submit (fn [evento]
                       (.preventDefault evento)
                       (swap! state confirmar-formulario))}
   [codigo-empleado-input]
   [fecha-vigencia-input]
   [salario-minimo-input]
   #_[horas-trabajadas-input]
   #_[posicion-laboral-input]
   [confirmar-button]])

(defn render-historial-cambios-item [index cambio]
  (let [{:keys [codigo-empleado]} cambio]
    [:li
     [:span index]
     [:span codigo-empleado]]))

(defn historial-cambios []
  (let [historial (r/cursor state [:historial])]
    [:p "Historial de cambios"]))

(defn app []
  [:div
   [formulario]
   [historial-cambios]])

(rdom/render
 [app]
 (gdom/getElement "app"))


