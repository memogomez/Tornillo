app.service('clientesService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	function($http, $q, $location,$rootScope, $window) {
	
	this.registraCliente = function(newCliente) {
		var d = $q.defer();
		$http.post("/clientes/add/", newCliente).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.findClientes = function(page) {
		var d = $q.defer();
		$http.get("/clientes/findAll/"+page).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.findClientesFull= function(){
		var d = $q.defer();
		$http.get("/clientes/findFull").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.findCliente = function(id) {
		var d = $q.defer();
		$http.get("/clientes/find/"+id).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.buscar= function(search){
		var d = $q.defer();
		$http.get("/clientes/search/"+search).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.getPages= function(){
		var d = $q.defer();
		$http.get("/clientes/pages").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
}])
app.controller("clientesController",[
	'$scope',
	'clientesService',
	'$routeParams',
	'$location',
	'$window',
	function($scope, clientesService, $routeParams,$location,$window){
	
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
		
	$scope.registraCliente = function(newCliente) {
		console.log(newCliente);		
		clientesService.registraCliente(newCliente).then(function(newCliente) {
					alert("Cliente Agregado");
					$window.location.reload();
					$location.path("/clientes");
				})
	}
	$scope.clientesPage = function(page) {
		$scope.paginaAcutal=page;
		clientesService.findClientes(page).then(
			function(data) {
				$scope.clientes = data;				
				$scope.llenarPags();
				
			})
	}
	
	$scope.clientesPage(1);
	
	clientesService.getPages().then(function(data){
		$scope.maxPage=data;
	})
	
	$scope.editar = function(id) {
		$location.path("/clientes/edit/" + id);
	}
	
	$scope.buscar= function(){
		clientesService.buscar($scope.searchText).then(function(data){
			$scope.clientes= data;
			$scope.searchText="";
		})
	}
	
}]);
app.controller("clientesEditController",[
	'$scope',
	'clientesService',
	'$routeParams',
	'$location',
	'$window',
	function($scope, clientesService, $routeParams,$location, $window){
		clientesService.findCliente($routeParams.id).then(function(data){
			$scope.newCliente=data;
		})
		
		$scope.editaCliente = function(newCliente) {
		console.log(newCliente);		
		clientesService.registraCliente(newCliente).then(function(newCliente) {
					alert("Cliente Modificado");
					$window.location.reload();
					$location.path("/clientes");
				})
	}	
}]);