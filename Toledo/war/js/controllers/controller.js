var app = angular.module("app", [ 'ngRoute', 'ngCookies' ]);
app.config([ '$routeProvider', function($routeProvider) {

	$routeProvider.when('/inicio', {
		templateUrl : "pages/inicio.html",
//		controller : "controller"
	});
	$routeProvider.when('/clientes', {
		templateUrl : "pages/clientes.html",
//		controller : "controller"
	});
	$routeProvider.when('/proveedores', {
		templateUrl : "pages/proveedores.html",
//		controller : "controller"
	});
	$routeProvider.when('/herramientas', {
		templateUrl : "pages/herramientas.html",
//		controller : "controller"
	});
	$routeProvider.when('/tornillos', {
		templateUrl : "pages/tornillos.html",
//		controller : "controller"
	});
	
} ]);