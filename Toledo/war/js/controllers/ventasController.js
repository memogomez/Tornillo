app.service("ventasService",['$http','$q',function($http,$q){
	this.addVenta=function(){
		
	};
}]);

app.controller("ventaController",['clientesService','ventasService','tornillosService','herramientasService','$scope','$location',function(clientesService,ventasService,tornillosService,herramientasService,$scope,$location){
	$scope.venta={
			fecha:new Date(),
			detalles:[]
	};
	clientesService.findClientes().then(function(data){
		$scope.clientes=data;
	})
	
	$scope.productos=[];
	$scope.herramientas = function() {
		herramientasService.findHerramientas().then(
			function(data) {
				$scope.herramientas = data;				
				for(var i =0; i<data.length; i++){
					$scope.productos.push(data[i]);
				}
				
			})
	}
	$scope.herramientas();
	
	$scope.tornillos = function() {
		tornillosService.findTornillos().then(
			function(data) {
				$scope.tornillos = data;				
				for(var i =0; i<data.length; i++){
					$scope.productos.push(data[i]);
				}
			})
	}
	$scope.tornillos();
	
	$scope.agregarDetalle=function(producto){
		var detalle={}
		detalle.idProducto=producto.id;
		detalle.descripcion= producto.nombre;
		if(producto.medidas){
			detalle.descripcion= detalle.descripcion+" "+producto.medidas;
		}
		detalle.cantidad=producto.cantidad;
		detalle.precioUnitario=producto.precio;
		detalle.importe= producto.importe;
		detalle.tipo=producto.tipo;
		$scope.venta.detalles.push(detalle);
	}
}]);