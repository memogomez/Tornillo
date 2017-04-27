app.service('inventarioService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	'herramientasService',
	'tornillosService',
	'lotesService',
	function($http, $q, $location,$rootScope, $window, proveedoresService, herramientasService, tornillosService, lotesService) {
		this.findHerramientas = function() {
			var d = $q.defer();
			$http.get("/productos/findAll/").then(function(response) {
				console.log(response);
				d.resolve(response.data);
			}, function(response) {
				d.reject(response);
			});
			return d.promise;
		};
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
		this.findLote = function(id) {
			var d = $q.defer();
			$http.get("/lotes/find/"+id).then(function(response) {
				console.log(response);
				d.resolve(response.data);
			}, function(response) {
				d.reject(response);
			});
			return d.promise;
		};
}])
app.controller("inventarioController",[
	'$scope',
	'inventarioService',
	'$routeParams',
	'$location',
	'$window',
	'herramientasService',
	'tornillosService',
	'lotesService',
	function($scope, inventarioService, $routeParams,$location, $window, herramientasService, tornillosService, lotesService){
		
		
		$scope.inventario=[];
		$scope.herramientas = function() {
			herramientasService.findHerramientas().then(
				function(data) {
					$scope.herramientas = data;				
					console.log(data);
					for(var i =0; i<data.length; i++){
						$scope.inventario.push(data[i]);
					}
					
				})
		}
		$scope.herramientas();
		
		$scope.tornillos = function() {
			tornillosService.findTornillos().then(
				function(data) {
					$scope.tornillos = data;				
					console.log(data);
					for(var i =0; i<data.length; i++){
						$scope.inventario.push(data[i]);
					}
				})
		}
		$scope.tornillos();
	
		$scope.mostrarLotes = function(inv){
			$scope.nombreInventario=inv.nombre;
			lotesService.findLote(inv.id).then(
					function(data) {
						$scope.lotes = data;				
						console.log(data);						
				})
		}
	
	
}]);
