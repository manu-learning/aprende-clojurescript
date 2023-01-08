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
// operador create: crea un sujeto observable a partir de la función pasada por parámetro,
// (la función pasada por parámetro define como se va a emitir los valores el Sujeto Observable)
var saludo = rxjs_1.Observable.create(function (observer) {
    observer.next('bienvenido');
    observer.next('a la fiesta');
    observer.complete();
});
var visitanteObservador = saludo.subscribe(function (x) { return console.log(x); });
// operador range: crea un Sujeto Observable que emite un números enteros entre un rango de valores
var televidenteObservador = (0, rxjs_1.range)(1, 5).subscribe(function (x) { return console.log('número de la loteria:', x); });
// operador from: convierte un objeto ó una estructura de datos en un Sujeto Observable
var listadoEntrevistadosObservable = (0, rxjs_1.from)([usuario1, usuario2]);
var entrevistadorObservador = listadoEntrevistadosObservable.subscribe(function (x) { return console.log('entrevisté a ', x); });
var listadoNumerosDeLaSuerteObservable = (0, rxjs_1.from)([10, 20, 39]);
var estudianteObservador = listadoNumerosDeLaSuerteObservable.subscribe(function (x) { return console.log('memorizando numero de la suerte ', x); });
// operador interval: Crea un observable que emite valores cada cierto intervalo de tiempo (en este caso cada 1 segundo)
var observador5 = (0, rxjs_1.interval)(1000).subscribe(function (x) { return console.log(x); });
