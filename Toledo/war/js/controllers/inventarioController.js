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
		
		this.loadInventario=function(page){
			var d = $q.defer();
			$http.get("/inventario/pages/"+page).then(function(response) {
				console.log(response);
				d.resolve(response.data);
			}, function(response) {
				d.reject(response);
			});
			return d.promise;
		}
		this.numPages = function() {
			var d = $q.defer();
			$http.get("/inventario/numPages").then(function(response) {
				console.log(response);
				d.resolve(response.data);
			}, function(response) {
				d.reject(response);
			});
			return d.promise;
		};
		this.buscar = function(search) {
			var d = $q.defer();
			$http.get("/inventario/buscar/"+search).then(function(response) {
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
		$scope.paginaActual=1;
		$scope.llenarPags=function(){
			var inicio=0;
			if($scope.paginaActual>3){
				inicio=$scope.paginaActual-3;
			}
			var fin = inicio+5;
			if(fin>$scope.maxPage){
				fin=$scope.maxPage;
			}
			$scope.paginas=[];
			for(var i = inicio; i< fin; i++){
				$scope.paginas.push(i+1);
			}
			for(var i = inicio; i<= fin; i++){
				$('#pag'+i).removeClass("active");
			}
			$('#pag'+$scope.paginaActual).addClass("active");
		}
//		$scope.paginas = new Array(20);
//		$scope.getNumber = function(num) {
//		    return new Array(num);   
//		}
		$scope.inventario=[];
		$scope.herramientas = function() {
			herramientasService.findHerramientas().then(
				function(data) {
					$scope.herramientas = data;				
					console.log(data);
					for(var i =0; i<data.length; i++){
						$scope.inventario.push(data[i]);
					}
					console.log($scope.inventario);
					for (var i = 0; i < $scope.inventario.length; i++) {
						$scope.inventario[i].busquedaAttr = $scope.inventario[i].nombre
						+ " "
						+ $scope.inventario[i].clave
						+ " "
						+ $scope.inventario[i].medidas
						+ " "
					}					
				})
		}
//		$scope.herramientas();
		
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
//		$scope.tornillos();
	
		$scope.mostrarLotes = function(inv){
			$scope.nombreInventario=inv.nombre;
			lotesService.findLote(inv.id).then(
					function(data) {
						$scope.lotes = data;				
						console.log(data);						
				})
		}
	
		$scope.cargaInventario=function(page){
			inventarioService.loadInventario(page).then(function(data){
				console.log(data);
				$scope.todos=true;
				$scope.inventario=[];
				for(var i =0; i<data[0].length;i++){
					$scope.inventario.push(data[0][i]);
				}
				if(data[1]){
				for(var i =0; i<data[1].length;i++){
					$scope.inventario.push(data[1][i]);
				}
				}
			})
		}
		
		$scope.cargarPagina=function(pag){
			if($scope.paginaActual!=pag){
				$scope.paginaActual=pag;
				$scope.cargaInventario(pag);
			}
		}
		inventarioService.numPages().then(function(data){
			$scope.maxPage=data;
			$scope.llenarPags();
			
		})
		$scope.cargaInventario(1);
	
		$scope.buscarInv=function(){
			inventarioService.buscar($scope.search).then(function(data){
				$scope.inventario=[];
				if(data[0]){
					for(var i =0; i<data[0].length;i++){
						$scope.inventario.push(data[0][i]);
					}
					if(data[1]){
						for(var i =0; i<data[1].length;i++){
							$scope.inventario.push(data[1][i]);
						}
					}
				}else{
					alert("No se encontraron articulos")
				}
			})
		}
}]);
