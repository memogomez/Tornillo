app.service("ventasService",['$http','$q',function($http,$q){
	this.addVenta=function(){
		
	};
}]);

app.controller("ventaController",['clientesService','ventasService','tornillosService','herramientasService','$scope','$location',function(clientesService,ventasService,tornillosService,herramientasService,$scope,$location){
	$scope.venta={};
	clientesService.findClientes().then(function(data){
		$scope.clientes=data;
		console.log($scope.clientes);
	})
	
}]);