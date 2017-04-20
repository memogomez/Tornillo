app.service('herramientasService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	function($http, $q, $location,$rootScope) {
	
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
	}
}])
app.controller("herramientasController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	function($scope, herramientasService,$routeParams){
	
	$scope.registraHerramienta = function(newHerramienta) {
		console.log(newHerramienta);		
		herramientasService.registraHerramienta(newHerramienta).then(function(newHerramienta) {
					alert("Herramienta Agregada");
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
		
}]);