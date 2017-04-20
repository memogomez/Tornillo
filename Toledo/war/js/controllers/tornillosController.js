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
}])
app.controller("tornillosController",[
	'$scope',
	'tornillosService',
	function($scope, tornillosService){
	
	$scope.registraTornillos = function(newTornillo) {
		console.log(newTornillo);		
		tornillosService.registraTornillos(newTornillo).then(function(newTornillo) {
					alert("Tornillo Agregado");
				})
	}
		
}]);