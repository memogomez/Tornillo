app.service("ventasService",['$http','$q',function($http,$q){
	this.addVenta=function(venta){
		var d = $q.defer();
		$http.post("/ventas/add/", venta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	};
	
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
	
	this.getProductos= function(page){
		var d = $q.defer();
		$http.get("/ventas/productos/"+page).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
}]);

app.controller("ventaController",['clientesService','ventasService','tornillosService','herramientasService','$scope','$location',function(clientesService,ventasService,tornillosService,herramientasService,$scope,$location){
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
	
	$scope.cargarPagina=function(pag){
		if($scope.paginaActual!=pag){
			$scope.paginaActual=pag;
			$scope.cargaProductos(pag);
		}
	}
	ventasService.numPages().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	})
	
	
	$scope.venta={
			fecha:new Date(),
			detalles:[]
	};
	clientesService.findClientes().then(function(data){
		$scope.clientes=data;
	})
	
	
//	$scope.productos=[];
//	$scope.herramientas = function() {
//		herramientasService.findHerramientasAll().then(
//			function(data) {
//				$scope.herramientas = data;				
//				for(var i =0; i<data.length; i++){
//					$scope.productos.push(data[i]);
//				}
//				
//			})
//	}
//	$scope.herramientas();
//	$scope.tornillos = function() {
//		tornillosService.findTornillos().then(
//			function(data) {
//				$scope.tornillos = data;				
//				for(var i =0; i<data.length; i++){
//					$scope.productos.push(data[i]);
//				}
//			})
//	}
//	$scope.tornillos();
	
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
	
	$scope.busqueda=[];
	
//	$scope.buscar = function(buscar){
//		$scope.todos=false;
//		console.log(buscar);
//		tornillosService.busqueda(buscar).then(
//				function(data) {
//					$scope.productos = data;
//					for(var i =0; i<data.length; i++){
//						$scope.busqueda.push(data[i]);
//					}
//					$scope.busqueda.buscar="";
//					console.log(data);
//				})
//	}
	$scope.buscar = function(buscar){
		$scope.todos=false;
		$scope.busqueda=[];
		herramientasService.busqueda(buscar).then(
				function(data) {
//					$scope.herramientas = data;	
					for(var i =0; i<data.length; i++){
						$scope.busqueda.push(data[i]);
					}
					tornillosService.busqueda(buscar).then(
							function(data) {
//								$scope.productos = data;
								for(var i =0; i<data.length; i++){
									$scope.busqueda.push(data[i]);
								}
								$scope.busqueda.buscar="";
								$scope.productos=$scope.busqueda;
								console.log(data);
							});
				})
	}
	
	$scope.cargaProductos=function(page){
	ventasService.getProductos(page).then(function(data){
		$scope.productos=[];
		$scope.todos=true;
		for(var i =0; i<data[1].length;i++){
			$scope.productos.push(data[1][i]);
		}
		for(var i =0; i<data[0].length;i++){
			$scope.productos.push(data[0][i]);
		}
	})
	}
	
	$scope.calculaImporte=function(producto){
		var valor=producto.cantidad*producto.precio;
		
		return valor.toFixed(2)*1;
	}
	$scope.cargaProductos(1);
	
	$scope.guardarVenta= function (){
		ventasService.addVenta($scope.venta).then(function(data){
			alert("La venta ha sido guardada");
			
			$window.location.reload();
		});
	}
	
}]);