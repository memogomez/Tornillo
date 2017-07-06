app.service("emisorService",['$http','$q',function($http,$q){
	this.registrar = function(emisor) {
		var d = $q.defer();
		$http.post("/emisor/registrar/",emisor).then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.eliminar = function(id) {
		var d = $q.defer();
		$http.post("/emisor/eliminar/",id).then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.consultar = function() {
		var d = $q.defer();
		$http.get("/emisor/getAll/").then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.consultarId = function(id) {
		var d = $q.defer();
		$http.get("/emisor/get/"+id).then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.activar = function(id) {
		var d = $q.defer();
		$http.post("/emisor/activar/",id).then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
}]);

app.controller("emisoresListController",['$window','emisorService','$scope','$location',function($window,emisorService,$scope,$location){
	emisorService.consultar().then(function(data){
		$scope.emisores=data;
	})
	
	$scope.editar=function(id){
		$location.path("/editEmisor/"+id);
	}
	$scope.eliminar=function(id){
		var c= confirm("Confirmar eliminación");
		if(c){
			emisorService.eliminar(id).then(function(data){
				alert("Eliminado");
				$location.path("/datosFacturacion");
				$window.location.reload();
			})
		}
	}
	$scope.activar=function(id){
		var c= confirm("Confirmar activación");
		if(c){
			emisorService.activar(id).then(function(data){
				alert("Activado");
				$location.path("/datosFacturacion");
				$window.location.reload();
			})
		}
	}
	
}]);
	
app.controller("emisorEditController",['$window','emisorService','$scope','$location','$routeParams',function($window,emisorService,$scope,$location,$routeParams){
	emisorService.consultarId($routeParams.id).then(function(data){
		$scope.emisor=data;
	})
	$scope.registraEmisor= function(){
		emisorService.registrar($scope.emisor).then(function(data){
			alert("Registrado");
			$location.path("/datosFacturacion");
			$window.location.reload();
		})
	}
	
}]);


app.controller("emisorController",['$window','emisorService','$scope','$location',function($window,emisorService,$scope,$location){
	$scope.registraEmisor= function(){
		emisorService.registrar($scope.emisor).then(function(data){
			alert("Registrado");
			$location.path("/datosFacturacion");
			$window.location.reload();
		})
	}

}]);

