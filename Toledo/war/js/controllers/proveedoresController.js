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
}])
app.controller("proveedoresController",[
	'$scope',
	'proveedoresService',
	function($scope, proveedoresService){
	
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
		
}]);