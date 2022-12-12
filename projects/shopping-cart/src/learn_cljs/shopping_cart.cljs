(ns ^:figwheel-hooks learn-cljs.shopping-cart
  (:require
   [goog.dom :as gdom]))

(def tasa-descuento-jubilado 0.10)
(def tasa-descuento-premium 0.20)

(def galletas-chocolate {:nombre "galletas de chocolate"
                         :categoria "galletas"
                         :precio 50})

(def canasta-de-navidad [{:nombre "pan dulce" :categoria "panaderia" :precio 300 :precio-cuidado? true}
                         {:nombre "turrón" :categoria "dulces" :precio 200 :precio-cuidado? true}
                         {:nombre "sidra" :categoria "bebidas" :precio 500 :precio-cuidado? false}])

(def productos-precios-cuidados
  (filter :precio-cuidado? canasta-de-navidad))

(defn detalle-producto [producto]
  (let [{:keys [nombre precio descuento]} producto]
    (str nombre " $" precio ", valor del descuento $" descuento)))

(defn nombre-productos []
  (map :nombre canasta-de-navidad))

(defn aplicar-descuento [producto descuento]
  (let [{:keys [precio]} producto]
    (assoc producto :descuento (* precio descuento))))

(aplicar-descuento galletas-chocolate tasa-descuento-premium)

(def canasta-navidad-jubilados
  (map (fn [producto] (aplicar-descuento producto tasa-descuento-jubilado))
       canasta-de-navidad))

;; UI
;;(def lista-productos (gdom/createDom "ul" nil ""))
(def lista-productos-jubilados (gdom/getElement "lista-productos-jubilados"))

(defn item-producto [producto]
  (gdom/createDom "li" #js {} (detalle-producto producto)))

(def cargar-productos
  (doseq [producto canasta-navidad-jubilados]
    (gdom/appendChild
     lista-productos-jubilados
     (item-producto producto))))

(defonce productos-loaded
  (gdom/appendChild (.-body js/document) lista-productos-jubilados))

(cargar-productos)
(productos-loaded)

