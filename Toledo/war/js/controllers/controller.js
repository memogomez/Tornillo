var app = angular.module("app", [ 'ngRoute', 'ngCookies' ]);
app.config([ '$routeProvider', function($routeProvider) {

	$routeProvider.when('/inicio', {
		templateUrl : "pages/inicio.html",
		controller : "inicioController"
	});
	$routeProvider.when('/clientes', {
		templateUrl : "pages/clientes.html",
		controller : "clientesController"
	});
	$routeProvider.when('/clientes/edit/:id', {
		templateUrl : "pages/clientesEdita.html",
		controller : "clientesEditController"
	});
	$routeProvider.when('/proveedores', {
		templateUrl : "pages/proveedores.html",
		controller : "proveedoresController"
	});
	$routeProvider.when('/proveedores/edit/:id', {
		templateUrl : "pages/proveedoresEdita.html",
		controller : "proveedoresEditController"
	});
	$routeProvider.when('/herramientas', {
		templateUrl : "pages/herramientas.html",
		controller : "herramientasController"
	});
	$routeProvider.when('/herramientas/edit/:id', {
		templateUrl : "pages/herramientasEdita.html",
		controller : "herramientasEditController"
	});
	$routeProvider.when('/tornillos', {
		templateUrl : "pages/tornillos.html",
		controller : "tornillosController"
	});
	$routeProvider.when('/tornillos/edit/:id', {
		templateUrl : "pages/tornillosEdita.html",
		controller : "tornillosEditController"
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
	$routeProvider.when('/altaLotes/:id', {
		templateUrl : "pages/altaLotes.html",
		controller : "lotesController"
	});
	
	$routeProvider.when('/ventas', {
		templateUrl : "pages/venta.html",
		controller : "ventaController"
	});
	
	$routeProvider.when('/ventasList', {
		templateUrl : "pages/ventasList.html",
		controller : "ventaListController"
	});

	$routeProvider.when('/inventario', {
		templateUrl : "pages/inventario.html",
		controller : "inventarioController"
	});
	$routeProvider.when('/login', {
		templateUrl : "pages/login.html",
		controller : "navigation"
	});

	$routeProvider.when('/altaperfiles', {
		templateUrl : "pages/altaperfil.html",
		controller : "perfilController"
	})

	$routeProvider.when('/modificarperfiles', {
		templateUrl : "pages/modificaperfiles.html",
		controller : "controladorListaPerfiles"
	})

	$routeProvider.when('/altausuarios', {
		templateUrl : "pages/altausuario.html",
		controller : "usuarioController"
	})

	$routeProvider.when('/modificarusuarios', {
		templateUrl : "pages/modificausuarios.html",
		controller : "controladorListaUsuarios"
	})

	$routeProvider.when('/resetPass', {
		templateUrl : "pages/resetpass.html",
		controller : "controladorReset"
	})
	$routeProvider.when('/archivo', {
		templateUrl : "pages/archivo.html",
		controller : "archivoController"
	});
	
	$routeProvider.otherwise({
		redirectTo : '/inicio'
	});
} ]);

app.controller('inicioController',function(){
	
})

app.run(['$rootScope','sessionService',function ($rootScope,sessionService) {
	sessionService.isAuthenticated();
}]);