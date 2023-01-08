"use strict";
exports.__esModule = true;
var usuario_1 = require("./model/usuario");
var rxjs_1 = require("rxjs");
var usuario1 = new usuario_1.Usuario();
usuario1.idUsuario = 1;
usuario1.nombre = "Jean Luc";
usuario1.velocidad = 100;
var usuario2 = new usuario_1.Usuario();
usuario2.idUsuario = 2;
usuario2.nombre = "William";
usuario2.velocidad = 200;
var usuario3 = new usuario_1.Usuario();
usuario3.idUsuario = 3;
usuario3.nombre = "Data";
usuario3.velocidad = 900;
var listadoPersonajesObservables = (0, rxjs_1.from)([usuario1, usuario2, usuario3]);
// operador pipe: cada vez que usaremos otro operador aparte del from
// operador map: transforma los elementos emitidos por un sujeto Observable, aplicando una función a cada elemento
var suscripcionPersonajesEvolucionados = listadoPersonajesObservables
    .pipe((0, rxjs_1.map)(function (usuario) { return usuario.velocidad * 2; }))
    .subscribe(function (dobleDeVelocidad) { return console.log('Velocidad doble: ', dobleDeVelocidad); });
