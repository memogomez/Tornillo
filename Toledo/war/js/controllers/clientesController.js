app.service('clientesService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	function($http, $q, $location,$rootScope) {
	
	this.registraCliente = function(newCliente) {
		var d = $q.defer();
		$http.post("/clientes/add/", newCliente).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.findClientes = function() {
		var d = $q.defer();
		$http.get("/clientes/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
}])
app.controller("clientesController",[
	'$scope',
	'clientesService',
	'$routeParams',
	function($scope, clientesService, $routeParams){
	
	$scope.registraCliente = function(newCliente) {
		console.log(newCliente);		
		clientesService.registraCliente(newCliente).then(function(newCliente) {
					alert("Cliente Agregado");
				})
	}
	$scope.clientes = function() {
		clientesService.findClientes($routeParams.id).then(
			function(data) {
				$scope.clientes = data;				
				console.log(data);
			})
	}
	$scope.clientes();
}]);