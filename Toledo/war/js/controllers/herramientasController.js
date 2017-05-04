app.service('herramientasService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	'proveedoresService',
	function($http, $q, $location,$rootScope,$window,proveedoresService) {
	
	this.registraHerramienta = function(newHerramienta) {
		var d = $q.defer();
		$http.post("/productos/add/", newHerramienta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.findHerramientas = function() {
		var d = $q.defer();
		$http.get("/productos/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.findHerramienta = function(id) {
		var d = $q.defer();
		$http.get("/productos/find/"+id).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.findProveedores = function() {
		var d = $q.defer();
		$http.get("/proveedores/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.busqueda = function(buscar) {
		var d = $q.defer();
		$http.get("/productos/search/"+buscar).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
}])
app.controller("herramientasController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, herramientasService,$routeParams,$location,$window, proveedoresService){
	$scope.busqueda={};
	$scope.registraHerramienta = function(newHerramienta) {
		console.log(newHerramienta);		
		herramientasService.registraHerramienta(newHerramienta).then(function(newHerramienta) {
					alert("Herramienta Agregada");
					$window.location.reload();
					$location.path("/herramientas");
				})
	}
	$scope.herramientas = function() {
		herramientasService.findHerramientas($routeParams.id).then(
			function(data) {
				$scope.herramientas = data;				
				console.log(data);
			})
	}
	$scope.herramientas();
	
	$scope.editar = function(id) {
		$location.path("/herramientas/edit/" + id);
	}
	
	$scope.proveedores = function() {
		proveedoresService.findProveedores($routeParams.id).then(
			function(data) {
				$scope.proveedores = data;				
				console.log(data);
			})
	}
	$scope.proveedores();
	
	$scope.lotes = function(id) {			
		$location.path("/altaLotes/" + id);
	}
	
	$scope.buscar = function(buscar){
		
		herramientasService.busqueda(buscar).then(
				function(data) {
					$scope.herramientas = data;	
					$scope.busqueda.buscar="";
					console.log(data);
				})
	}
	$scope.mostrarHerramientas = function(){
		herramientasService.findHerramientas($routeParams.id).then(
				function(data) {
					$scope.herramientas = data;				
					console.log(data);
				})
	}
}]);
app.controller("herramientasEditController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, herramientasService,$routeParams,$location, $window, proveedoresService){
		herramientasService.findHerramienta($routeParams.id).then(function(data){
			$scope.newHerramienta=data;
		})

		$scope.editaHerramienta = function(newHerramienta) {
			console.log(newHerramienta);		
			herramientasService.registraHerramienta(newHerramienta).then(function(newHerramineta) {
						alert("Herramienta Modificada");
						$window.location.reload();
						$location.path("/herramientas");
					})
		}
		$scope.proveedores = function() {
			proveedoresService.findProveedores($routeParams.id).then(
				function(data) {
					$scope.proveedores = data;				
					console.log(data);
				})
		}
		$scope.proveedores();
			
}]);