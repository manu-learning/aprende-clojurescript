(ns ^:figwheel-hooks learn-cljs.google-closure
  (:require [goog.dom :as gdom]
            [goog.events :as gevents]))

;; .querySelector
;; - es una función nativa de javascript que expone clojurescript, para obtener el primer elemento del DOM que coincida
;; - usamos # para capturar por el atributo id de un elemento del DOM
;; - usamos . para capturar por el atributo class de un elemento del DOM
;;
;; js/document
;; - obtenemos el DOM (se lo pasaremos por parámetro al querySelector)
(def body (.querySelector js/document "#bienvenida"))

;; goog.dom
;; - es la biblioteca de google closure
;;
;; createElement(nombre-tag)
;; - función de goog.dom, crea/devuelve un elemento con el nombre del tag que le pasamos
(def titulo (gdom/createElement "h1"))
(def parrafo (gdom/createElement "p"))

;; setTextContent(elemento, texto)
(gdom/setTextContent titulo "Cuenta de Acceso Premium")
(gdom/setTextContent parrafo "Estas en la sección premium")


;; appendChild(elemento-padre, elemento-hijo)
(comment "
  (gdom/appendChild body titulo)
  (gdom/appendChild body parrafo)
")

;; (gdom/removeNode body)

;; setProperties(elemento, objeto javascript con las propiedades como pares ordenados de la forma propiedad-valor)
;; ese objeto js toma la misma forma que una estructura map de clojure {:a 1 :b 3}
(gdom/setProperties titulo #js {"style" "color:red;"
                                "class" "big-titulo"})

;; getElement
;; - obtenemos un elemento del DOM como un objeto javascript
;; - al devolver un objeto js podemos usar funciones de cljs como (.-) para obtener el valor asociado a un atributo de un objeto js
(def field-nombre (gdom/getElement "field-nombre"))

(def nombre (gdom/getElement "nombre"))

;; getElementById
;; - es una función nativa de javascript que expone clojurescript, para obtener un elemento del DOM que contenga ese valor en su atributo id
;; - goog.dom tiene getElement(id-del-elemento) como función wrapper que facilita su invocación
(def field-apodo (.getElementById js/document "field-apodo"))
(def apodo (.getElementById js/document "apodo"))

(gdom/setTextContent nombre
                     (.-value field-nombre))

(gdom/setTextContent apodo
                     (.-value field-apodo))

;; ..
;; - función de clojure para acceder a un objeto javascript con estructuras anidadas
;; por ejemplo (.. persona -datos -nombre)
(defn actualizar-nombre [evento]
  (gdom/setTextContent nombre
                       (.. evento -currentTarget -value)))

(defn passwords-fields-coinciden? [password1 password2]
  (= (.-value password1)
     (.-value password2)))

;; versiones que fuimos haciendo refactor
(defn validar-password-v1 [password repassword estado-validacion]
  (if (not= (.-value password) (.-value repassword))
    (gdom/setTextContent estado-validacion "Las contraseñas no coinciden :(")
    (gdom/setTextContent estado-validacion "Las contraseñas coinciden OK :)")))

(defn validar-password-v2 [password repassword estado-validacion]
  (gdom/setTextContent estado-validacion
                       (if (not= (.-value password) (.-value repassword))
                         "Las contraseñas no coinciden"
                         "Las contraseñas coinciden OK")))

(defn validar-password-v3 [password repassword estado-validacion]
  (gdom/setTextContent estado-validacion
                       (if (passwords-fields-coinciden? password repassword)
                         "Las contraseñas coinciden :)"
                         "Las contraseñas NO coinciden >:(")))

;; listen(elemento observado, tipo de evento a escuchar/observar, función que invocará cada vez que ocurra el evento escuchado/observado)
;; - la función pasada como 3er parámetro, debe recibir un parámetro (el evento)
(gevents/listen field-nombre
                "keyup"
                actualizar-nombre)

;; macro #
;; - para crear una función anónima corta sin nombre y pasar los parámetros que necesita la función que maneja el evento
(let [password (gdom/getElement "field-password")
      repassword (gdom/getElement "field-repassword")
      estado-validacion (gdom/getElement "estado-validacion-password")]
  (gevents/listen password "keyup"
                  #(validar-password-v3 password repassword estado-validacion))
  (gevents/listen repassword "keyup"
                  #(validar-password-v3 password repassword estado-validacion)))
