app.service('tornillosService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	function($http, $q, $location,$rootScope,$window) {
	
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
	};
	this.findTornillo = function(id) {
		var d = $q.defer();
		$http.get("/tornillos/find/"+id).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
}])
app.controller("tornillosController",[
	'$scope',
	'tornillosService',
	'$routeParams',
	'$location',
	'$window',
	function($scope, tornillosService, $routeParams, $location,$window){
	
	$scope.registraTornillos = function(newTornillo) {
		console.log(newTornillo);		
		tornillosService.registraTornillos(newTornillo).then(function(newTornillo) {
					alert("Tornillo Agregado");
					$window.location.reload();
					$location.path("/herramientas");
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
	
	$scope.editar = function(id) {
		$location.path("/tornillos/edit/" + id);
	}
	
}]);
app.controller("tornillosEditController",[
	'$scope',
	'tornillosService',
	'$routeParams',
	'$location',
	'$window',
	function($scope, tornillosService, $routeParams, $location,$window){
		tornillosService.findTornillo($routeParams.id).then(function(data){
			$scope.newTornillo=data;
		})
		
		$scope.editaTornillo = function(newTornillo) {
			console.log(newTornillo);		
			tornillosService.registraTornillos(newTornillo).then(function(newTornillo) {
						alert("Tornillo Modificado");
						$window.location.reload();
						$location.path("/tornillos");
					})
		}	
}]);