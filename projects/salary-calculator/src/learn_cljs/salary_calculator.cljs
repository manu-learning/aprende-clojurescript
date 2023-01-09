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

(defn contrato-laboral [codigo-empleado salario fecha-vigencia]
  {:codigo-empleado codigo-empleado
   :salario salario
   :fecha-vigencia fecha-vigencia})

(defn sueldo-inicial []
  {:codigo-empleado 0
   :salario-minimo 5000
   :fecha-vigencia (current-date-string (js/Date.))})

;; - cada vez que un componente cambie el estado de la app (state), Reagent volverá a renderizar a ese componente
;; porque desreferenció el estado de la app (state)
(defonce state
  (r/atom {:inputs (sueldo-inicial)
           :historial []}))

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
(defn render-input
  ([keyword titulo] (render-input keyword titulo "text"))
  ([keyword titulo tipo-input]
   (let [valor (r/cursor state [:inputs keyword])]
     (fn []
       [:div {:class (str (:name keyword) " relative z-0 w-full mb-6 group")}
        [:label {:class "peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6"}
         titulo]
        [:input {:type tipo-input
                 :value @valor
                 :on-change #(reset! valor (.. % -target -value))
                 :class "block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"}]]))))

(defn salario-minimo-input []
  (render-input :salario-minimo "Salario mínimo"))

(defn fecha-vigencia-input []
  (render-input :fecha-vigencia "Fecha Vigencia de Contrato" "date"))

(defn codigo-empleado-input []
  (render-input :codigo-empleado "Código de Empleado"))

(defn confirmar-button []
  [:div.actions
   [:button {:type "submit"
             :class "text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"} "Confirmar"]])

(defn confirmar-formulario [state]
  (let [{:keys [fecha-vigencia codigo-empleado salario-minimo]} (:inputs state)
        fecha-actual (current-date-string (js/Date.))]
    (-> state
        (update-in [:historial] #(conj % (contrato-laboral codigo-empleado salario-minimo fecha-vigencia)))
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

(defn historial-cambios-listado-item [index cambio]
  (let [{:keys [codigo-empleado salario fecha-vigencia]} cambio]
    [:tr {:class "bg-white border-b dark:bg-gray-800 dark:border-gray-700"}
     [:td {:scope "row"} (str "#" index)]
     [:td {:scope "row"} codigo-empleado]
     [:td {:scope "row"} fecha-vigencia]]))

(defn historial-cambios-header []
  [:thead {:class "text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400"}
   [:tr
    [:th "Nº Operación"]
    [:th {:class "px-6 py-3"
          :scope "col"}
     "Código Empleado"]
    [:th "Vigencia Contrato"]]])

;; TODO: agregar algún feature de cálculo para aplicar ratom/make-reaction
(defn historial-cambios []
  (let [historial (r/cursor state [:historial])]
    [:div
     [:h1 "Historial de cambios"]
     (if (empty? @historial)
       [:p "no hay cambios"]
       [:div
        [:table {:class "w-full text-sm text-left text-gray-500 dark:text-gray-400"}
         (historial-cambios-header)
         [:tbody
          (map-indexed (fn [index-cambio cambio] (historial-cambios-listado-item index-cambio cambio))
                       @historial)]]])]))

(defn app []
  [:div {:class "block p-6 rounded-lg shadow-lg bg-white max-w-sm"}
   [formulario]
   [:hr {:class "h-px my-8 bg-gray-200 border-0 dark:bg-gray-700"}]
   [historial-cambios]])

(rdom/render
 [app]
 (gdom/getElement "app"))


