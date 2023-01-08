"use strict";
exports.__esModule = true;
var rxjs_1 = require("rxjs");
var unStream = (0, rxjs_1.from)(['1', '2', 'foo', '3', '4']);
unStream
    .subscribe(function (x) { return console.log('valor emitido por el observable', x); });
unStream
    .pipe((0, rxjs_1.map)(function (x) { return parseInt(x); }), (0, rxjs_1.filter)(function (x) { return !isNaN(x); }))
    .subscribe(function (x) { return console.log('entero emitido por el observable', x); });
// podemos tomar valores de un array
var unArray = [1, 2, 3, 4, 5, 6];
(0, rxjs_1.interval)(1000)
    .pipe((0, rxjs_1.take)(5), (0, rxjs_1.filter)(function (x) { return x < 5; }), (0, rxjs_1.map)(function (x) { return unArray[x]; }))
    .subscribe(function (x) { return console.log('valor obtenido de un array luego de un segundo', x); });
