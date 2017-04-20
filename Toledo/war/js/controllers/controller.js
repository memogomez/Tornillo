var app = angular.module("app", [ 'ngRoute', 'ngCookies' ]);
app.config([ '$routeProvider', function($routeProvider) {

	$routeProvider.when('/inicio', {
		templateUrl : "pages/inicio.html",
//		controller : "controller"
	});
	$routeProvider.when('/clientes', {
		templateUrl : "pages/clientes.html",
		controller : "clientesController"
	});
	$routeProvider.when('/proveedores', {
		templateUrl : "pages/proveedores.html",
		controller : "proveedoresController"
	});
	$routeProvider.when('/herramientas', {
		templateUrl : "pages/herramientas.html",
		controller : "herramientasController"
	});
	$routeProvider.when('/tornillos', {
		templateUrl : "pages/tornillos.html",
		controller : "tornillosController"
	});
	$routeProvider.when('/altaCliente', {
		templateUrl : "pages/altaCliente.html",
		controller : "clientesController"
	});
	$routeProvider.when('/altaProveedor', {
		templateUrl : "pages/altaProveedor.html",
		controller : "proveedoresController"
	});
	$routeProvider.when('/altaHerramienta', {
		templateUrl : "pages/altaHerramienta.html",
		controller : "herramientasController"
	});
	$routeProvider.when('/altaTornillos', {
		templateUrl : "pages/altaTornillos.html",
		controller : "tornillosController"
	});
	$routeProvider.when('/inventario', {
		templateUrl : "pages/inventario.html",
//		controller : "controller"
	});
	
} ]);