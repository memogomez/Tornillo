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
	};
	this.findCliente = function(id) {
		var d = $q.defer();
		$http.get("/clientes/find/"+id).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
}])
app.controller("clientesController",[
	'$scope',
	'clientesService',
	'$routeParams',
	'$location',
	function($scope, clientesService, $routeParams,$location){
	
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
	
	$scope.editar = function(id) {
		$location.path("/clientes/edit/" + id);
	}
	
}]);
app.controller("clientesEditController",[
	'$scope',
	'clientesService',
	'$routeParams',
	'$location',
	function($scope, clientesService, $routeParams,$location){
		clientesService.findCliente($routeParams.id).then(function(data){
			$scope.newCliente=data;
		})
		
		$scope.editaCliente = function(newCliente) {
		console.log(newCliente);		
		clientesService.registraCliente(newCliente).then(function(newCliente) {
					alert("Cliente Modificado");
				})
	}	
}]);