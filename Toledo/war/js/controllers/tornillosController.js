app.service('tornillosService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	function($http, $q, $location,$rootScope) {
	
	this.registraTornillos = function(newTornillo) {
		var d = $q.defer();
		$http.post("/tornillos/add/", newTornillo).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.findTornillos = function() {
		var d = $q.defer();
		$http.get("/tornillos/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
}])
app.controller("tornillosController",[
	'$scope',
	'tornillosService',
	'$routeParams',
	function($scope, tornillosService, $routeParams){
	
	$scope.registraTornillos = function(newTornillo) {
		console.log(newTornillo);		
		tornillosService.registraTornillos(newTornillo).then(function(newTornillo) {
					alert("Tornillo Agregado");
				})
	}
	$scope.tornillos = function() {
		tornillosService.findTornillos($routeParams.id).then(
			function(data) {
				$scope.tornillos = data;				
				console.log(data);
			})
	}
	$scope.tornillos();
		
}]);