"use strict";
exports.__esModule = true;
var usuario_1 = require("./model/usuario");
var rxjs_1 = require("rxjs");
var usuario1 = new usuario_1.Usuario();
usuario1.idUsuario = 1;
usuario1.nombre = "Jean Luc";
var usuario2 = new usuario_1.Usuario();
usuario2.idUsuario = 2;
usuario2.nombre = "William";
var usuario3 = new usuario_1.Usuario();
usuario3.idUsuario = 3;
usuario3.nombre = "William";
// of: es un operador de creación
var obs1 = (0, rxjs_1.of)(usuario1);
var obs2 = (0, rxjs_1.of)(usuario2);
var obs3 = (0, rxjs_1.of)(usuario3);
// merge es un operador de combinación
(0, rxjs_1.merge)(obs1, obs2, obs3).subscribe(function (x) { return console.log('personaje ', x); });
