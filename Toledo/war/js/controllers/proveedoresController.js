app.service('proveedoresService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	function($http, $q, $location,$rootScope,$window) {
	
	this.registraProveedor = function(newProveedor) {
		var d = $q.defer();
		$http.post("/proveedores/add/", newProveedor).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.findProveedores = function() {
		var d = $q.defer();
		$http.get("/proveedores/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	this.findProveedor = function(id) {
		var d = $q.defer();
		$http.get("/proveedores/find/"+id).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
}])
app.controller("proveedoresController",[
	'$scope',
	'proveedoresService',
	'$routeParams',
	'$location',
	'$window',
	function($scope, proveedoresService,$routeParams, $location,$window){
	
	$scope.registraProveedor = function(newProveedor) {
		console.log(newProveedor);		
		proveedoresService.registraProveedor(newProveedor).then(function(newProveedor) {
					alert("Proveedor Agregado");
					$window.location.reload();
					$location.path("/proveedores");
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
		
	$scope.editar = function(id) {
		$location.path("/proveedores/edit/" + id);
	}
	
}]);
app.controller("proveedoresEditController",[
	'$scope',
	'proveedoresService',
	'$routeParams',
	'$location',
	'$window',
	function($scope, proveedoresService,$routeParams, $location,$window){
	proveedoresService.findProveedor($routeParams.id).then(function(data){
		$scope.newProveedor=data;
	})
	
	$scope.registraProveedor = function(newProveedor) {
		console.log(newProveedor);		
		proveedoresService.registraProveedor(newProveedor).then(function(newProveedor) {
					alert("Proveedor Modificado");
					$window.location.reload();
					$location.path("/proveedores");
				})
	}
}]);