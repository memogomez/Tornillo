app.service('proveedoresService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	function($http, $q, $location,$rootScope) {
	
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
	function($scope, proveedoresService,$routeParams, $location){
	
	$scope.registraProveedor = function(newProveedor) {
		console.log(newProveedor);		
		proveedoresService.registraProveedor(newProveedor).then(function(newProveedor) {
					alert("Proveedor Agregado");
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
	function($scope, proveedoresService,$routeParams, $location){
	proveedoresService.findProveedor($routeParams.id).then(function(data){
		$scope.newProveedor=data;
	})
	
	$scope.registraProveedor = function(newProveedor) {
		console.log(newProveedor);		
		proveedoresService.registraProveedor(newProveedor).then(function(newProveedor) {
					alert("Proveedor Modificado");
				})
	}
}]);