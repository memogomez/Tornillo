app.service('tornillosService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	'proveedoresService',
	function($http, $q, $location,$rootScope,$window,proveedoresService) {
	
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
	
	this.findTornillosPage = function(page) {
		var d = $q.defer();
		$http.get("/tornillos/pages/"+page).then(function(response) {
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
	this.findProveedores = function() {
		var d = $q.defer();
		$http.get("/proveedores/findAll/").then(function(response) {
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
	'proveedoresService',
	function($scope, tornillosService, $routeParams, $location, $window, proveedoresService){
	
	
	$scope.registraTornillos = function(newTornillo) {
		console.log(newTornillo);		
		tornillosService.registraTornillos(newTornillo).then(function(newTornillo) {
					alert("Tornillo Agregado");
//					$window.location.reload();
//					$location.path("/herramientas");
				})
	}
	$scope.cargaTornillos = function() {
		tornillosService.findTornillosPage(1).then(
			function(data) {
				$scope.tornillos = data;				
				console.log(data);
			})
	}
	$scope.cargaTornillos();
	
	$scope.editar = function(id) {
		$location.path("/tornillos/edit/" + id);
	}
	$scope.proveedores = function() {
		proveedoresService.findProveedores($routeParams.id).then(
			function(data) {
				$scope.proveedores = data;				
				console.log(data);
			})
	}
	$scope.proveedores();
	
	$scope.lotes = function(id) {			
		$location.path("/altaLotes/" + id);
	}
	
}]);
app.controller("tornillosEditController",[
	'$scope',
	'tornillosService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, tornillosService, $routeParams, $location, $window, proveedoresService){
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
		$scope.proveedores = function() {
			proveedoresService.findProveedores($routeParams.id).then(
				function(data) {
					$scope.proveedores = data;				
					console.log(data);
				})
		}
		$scope.proveedores();
}]);