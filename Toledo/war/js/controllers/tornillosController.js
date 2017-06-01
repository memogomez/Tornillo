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
	this.numPages = function() {
		var d = $q.defer();
		$http.get("/tornillos/numPages").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.busqueda = function(buscar) {
		var d = $q.defer();
		$http.get("/tornillos/search/"+buscar).then(function(response) {
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
				$('#pagA'+i).removeClass("active");
				$('#pagB'+i).removeClass("active");
			}
			$('#pagA'+$scope.paginaActual).addClass("active");
			$('#pagB'+$scope.paginaActual).addClass("active");
		}
		
		$scope.cargarPagina=function(pag){
			if($scope.paginaActual!=pag){
				$scope.paginaActual=pag;
				$scope.cargaTornillos(pag);
			}
		}
		tornillosService.numPages().then(function(data){
			$scope.maxPage=data;
			$scope.llenarPags();
			
		})
	
	$scope.registraTornillos = function(newTornillo) {
		console.log(newTornillo);		
		tornillosService.registraTornillos(newTornillo).then(function(newTornillo) {
					alert("Tornillo Agregado");
//					$window.location.reload();
//					$location.path("/herramientas");
				})
	}
	$scope.cargaTornillos = function(page) {
		tornillosService.findTornillosPage(page).then(
			function(data) {
				$scope.tornillos = data;	
				$scope.llenarPags();
				console.log(data);
			})
	}
	$scope.cargaTornillos(1);
	
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
	$scope.todos=true;
	$scope.buscar = function(buscar){
		$scope.todos=false;
		tornillosService.busqueda(buscar).then(
				function(data) {
					$scope.tornillos = data;	
					$scope.busqueda.buscar="";
					console.log(data);
				})
	}
	
	$scope.mostrarTornillos = function(){
		$scope.todos=true;
		$scope.cargarPagina(1);
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