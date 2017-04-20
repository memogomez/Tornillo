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
}])
app.controller("herramientasController",[
	'$scope',
	'herramientasService',
	function($scope, herramientasService){
	
	$scope.registraHerramienta = function(newHerramienta) {
		console.log(newHerramienta);		
		herramientasService.registraHerramienta(newHerramienta).then(function(newHerramienta) {
					alert("Herramienta Agregada");
				})
	}
		
}]);