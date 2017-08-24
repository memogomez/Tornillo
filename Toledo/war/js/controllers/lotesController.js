app.service('lotesService',
		[
				'$http',
				'$q',
				'$location',
				'$rootScope',
				'$window',
				'proveedoresService',
				function($http, $q, $location, $rootScope, $window,
						proveedoresService) {

					this.registraLote = function(newLote) {
						var d = $q.defer();
						$http.post("/lotes/add/", newLote).then(
								function(response) {
									console.log(response);
									d.resolve(response.data);
								});
						return d.promise;
					}
					this.findHerramienta = function(id) {
						var d = $q.defer();
						$http.get("/productos/find/" + id).then(
								function(response) {
									console.log(response);
									d.resolve(response.data);
								}, function(response) {
									d.reject(response);
								});
						return d.promise;
					};
					this.findTornillo = function(id) {
						var d = $q.defer();
						$http.get("/tornillos/find/" + id).then(
								function(response) {
									console.log(response);
									d.resolve(response.data);
								}, function(response) {
									d.reject(response);
								});
						return d.promise;
					};
					this.findProveedores = function() {
						var d = $q.defer();
						$http.get("/proveedores/findAll/").then(
								function(response) {
									console.log(response);
									d.resolve(response.data);
								}, function(response) {
									d.reject(response);
								});
						return d.promise;
					};
					this.findLote = function(id) {
						var d = $q.defer();
						$http.get("/lotes/find/" + id).then(function(response) {
							console.log(response);
							d.resolve(response.data);
						}, function(response) {
							d.reject(response);
						});
						return d.promise;
					};
				} ])
app.controller("lotesController", [
		'$scope',
		'lotesService',
		'$routeParams',
		'$location',
		'$window',
		'herramientasService',
		'tornillosService',
		'proveedoresService',
		function($scope, lotesService, $routeParams, $location, $window,
				herramientasService, tornillosService, proveedoresService) {
			$scope.newLote = {
					tipo:$routeParams.tipo
			};
			if($routeParams.tipo==0){
			herramientasService.findHerramienta($routeParams.id).then(
					function(data) {
						$scope.newHerramienta = data;
						if (!$scope.newLote.idProducto) {
							$scope.newLote.producto = data.nombre;
							$scope.newLote.idProducto = data.id;
							$scope.newLote.fecha = new Date();
						}
					});
			}else{
			tornillosService.findTornillo($routeParams.id).then(function(data) {
				$scope.newTornillo = data;
				if (!$scope.newLote.idProducto) {
					$scope.newLote.producto = data.nombre;
					$scope.newLote.idProducto = data.id;
					$scope.newLote.fecha = new Date();
				}
			})
			}
			$scope.registraLote = function(newLote) {
				console.log(newLote);
				lotesService.registraLote(newLote).then(function(newLote) {
					alert("Lote Agregado");
					$window.location.reload();
					$location.path("/inventario");
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

		} ]);
