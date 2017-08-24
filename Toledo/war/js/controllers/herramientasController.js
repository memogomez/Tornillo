app.service('herramientasService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	'proveedoresService',
	function($http, $q, $location,$rootScope,$window,proveedoresService) {
	
	this.registraHerramienta = function(newHerramienta) {
		var d = $q.defer();
		$http.post("/productos/add/", newHerramienta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	
	this.eliminaHerramienta = function(herramienta) {
		var d = $q.defer();
		$http.post("/productos/elimina/", herramienta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	
	this.findHerramientas = function(page) {
		var d = $q.defer();
		$http.get("/productos/pages/"+page).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.findHerramientasAll = function(page) {
		var d = $q.defer();
		$http.get("/productos/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.findHerramienta = function(id) {
		var d = $q.defer();
		$http.get("/productos/find/"+id).then(function(response) {
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
		$http.get("/productos/numPages").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.busqueda = function(buscar) {
		var d = $q.defer();
		$http.get("/productos/search/"+buscar).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
}])
app.controller("herramientasController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, herramientasService,$routeParams,$location,$window, proveedoresService){
	
	$scope.paginaActual=1;
	$scope.llenarPags=function(){
		var inicio=0;
		if($scope.paginaActual>3){
			inicio=$scope.paginaActual;
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
	
	$scope.cargarPagina=function(pag){
		if($scope.paginaActual!=pag){
			$scope.herramientas(pag);
		}
	}
	herramientasService.numPages().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	})
	$scope.busqueda={};
	$scope.registraHerramienta = function(newHerramienta) {
		console.log(newHerramienta);		
		herramientasService.registraHerramienta(newHerramienta).then(function(newHerramienta) {
					alert("Herramienta Agregada");
					$window.location.reload();
					$location.path("/herramientas");
				})
	}
	$scope.herramientasload = function(page) {
		herramientasService.findHerramientas(page).then(
			function(data) {
				$scope.herramientas = data;
				$scope.llenarPags();
				console.log(data);
			})
	}
	
	$scope.editar = function(id) {
		$location.path("/herramientas/edit/" + id);
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
		$location.path("/altaLotes/0/" + id);
	}
	
	$scope.buscar = function(buscar){
		
		herramientasService.busqueda(buscar).then(
				function(data) {
					$scope.herramientas = data;	
					$scope.busqueda.buscar="";
					console.log(data);
				})
	}
	$scope.mostrarHerramientas = function(){
		herramientasService.findHerramientas(1).then(
				function(data) {
					$scope.herramientas = data;
					$scope.paginaActual=1;
					$scope.llenarPags();
					console.log(data);
				})
	}
	$scope.cargaPagina=function(page){
		$scope.paginaActual=page;
		$scope.herramientasload(page);
	}
	$scope.cargaPagina(1);
	
	$scope.eliminar= function(herramienta){
			herramientasService.eliminarHerramienta(herramienta).then(function(data){
				alert("Artículo eliminado");
				$window.location.reload();
			});
	}
	
}]);
app.controller("herramientasEditController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, herramientasService,$routeParams,$location, $window, proveedoresService){
		herramientasService.findHerramienta($routeParams.id).then(function(data){
			$scope.newHerramienta=data;
		})

		$scope.editaHerramienta = function(newHerramienta) {
			console.log(newHerramienta);		
			herramientasService.registraHerramienta(newHerramienta).then(function(newHerramineta) {
						alert("Herramienta Modificada");
						$window.location.reload();
						$location.path("/herramientas");
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