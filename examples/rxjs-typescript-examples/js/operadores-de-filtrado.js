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
console.log(listadoPersonajesObservables);
// pipe: cada vez que usaremos otro operador aparte del from
// operador filter: sólo emitirá los valores que cumplen la condición del predicado
var suscripcionPersonajesMasRapidos = listadoPersonajesObservables
    .pipe((0, rxjs_1.filter)(function (usuario) { return usuario.velocidad > 500; }))
    .subscribe(function (usuario) { return console.log('un personaje veloz es ', usuario.nombre); });
var suscripcionPersonajesMasLentos = listadoPersonajesObservables
    .pipe((0, rxjs_1.filter)(function (usuario) { return usuario.velocidad < 500; }))
    .pipe((0, rxjs_1.take)(1))
    .subscribe(function (usuario) { return console.log('un personaje lento es ', usuario.nombre); });
