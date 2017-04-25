app.service('lotesService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	function($http, $q, $location,$rootScope, $window) {
	
	this.registraLote = function(newLote) {
		var d = $q.defer();
		$http.post("/lotes/add/", newLote).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
		
}])
app.controller("lotesController",[
	'$scope',
	'lotesService',
	'$routeParams',
	'$location',
	'$window',
	function($scope, lotesService, $routeParams,$location,$window){
	
	$scope.registraLote = function(newLote) {
		console.log(newLote);		
		lotesService.registraLote(newLote).then(function(newLote) {
					alert("Lote Agregado");
					$window.location.reload();
					$location.path("/inventario");
				})
	}	
	
}]);
