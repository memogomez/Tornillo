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
	
	this.buscar=function(fi,ff){
		var d = $q.defer();
		send={
				params:{
					fi:fi,
					ff,ff
				}
		}
		$http.get("/ventas/buscar/",send).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			}, function(response) {
				if(response.status==403){
					alert("No está autorizado para realizar esta acción");
					$location.path("/");
				}
			});
		return d.promise;
	}
	
	this.numPagesInventario = function() {
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
	
	this.findVentas= function(url,page){
		var d = $q.defer();
		$http.get(url+page).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	this.facturarVenta= function(venta){
		var d = $q.defer();
		$http.post("/ventas/facturar/",venta).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.numPages = function(){
		var d = $q.defer();
		$http.get("/ventas/numPages").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
}]);

app.controller("ventaController",['$window','clientesService','ventasService','tornillosService','herramientasService','$scope','$location',function($window,clientesService,ventasService,tornillosService,herramientasService,$scope,$location){
	$scope.MetodoPago=true;
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
	ventasService.numPagesInventario().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	})
	
	
	$scope.venta={
			fecha:new Date(),
			detalles:[],
			formaDePago:"Efectivo"
	};
	clientesService.findClientes().then(function(data){
		$scope.clientes=data;
	})
	
	$scope.$watch('indice',function(){
		if($scope.indice && $scope.indice!=-1){
			$scope.clienteSelected=$scope.clientes[$scope.indice];
			
			$scope.venta.idCliente=$scope.clienteSelected.id;
			$scope.venta.descuento=$scope.clienteSelected.descuento;
		}
		if($scope.indice && $scope.indice==-1){
			$scope.venta.idCliente=0;
			$scope.venta.descuento=0;
		}
	})
	
	$scope.calculaDesc=function(){
		if($scope.descuento){
			$scope.venta.total=0;
			for(var i=0; i<$scope.venta.detalles.length; i++){
				$scope.venta.detalles[i].importe =	$scope.venta.detalles[i].importe - ($scope.venta.detalles[i].importe * ($scope.venta.descuento/100));
				$scope.venta.detalles[i].importe= $scope.venta.detalles[i].importe.toFixed(2);
				$scope.venta.total+=	parseFloat($scope.venta.detalles[i].importe);
			}
		}else{
			$scope.venta.total=0;
			for(var i=0; i<$scope.venta.detalles.length; i++){ 
				$scope.venta.detalles[i].importe =	($scope.venta.detalles[i].importe*100) / (100 - $scope.venta.descuento);
				$scope.venta.detalles[i].importe= $scope.venta.detalles[i].importe.toFixed(2);
				$scope.venta.total+=	parseFloat($scope.venta.detalles[i].importe);
			}
		}
	}
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
		$scope.MetodoPago=false;
		var detalle={}
		detalle.idProducto=producto.id;
		detalle.descripcion= producto.nombre;
		if(producto.medidas){
			detalle.descripcion= detalle.descripcion+" "+producto.medidas;
		}
		detalle.cantidad=producto.cantidad;
		detalle.precioUnitario=producto.precio;
		detalle.importe= producto.importe;
		if($scope.descuento){
				detalle.importe =	producto.importe - (producto.importe * ($scope.venta.descuento/100));
				detalle.importe= parseFloat(detalle.importe.toFixed(2));
		}
		
		detalle.tipo=producto.tipo;
		$scope.venta.detalles.push(detalle);
		$scope.venta.total=0;
		for(var i=0; i<$scope.venta.detalles.length; i++){
			$scope.venta.total+=	parseFloat($scope.venta.detalles[i].importe);
			$scope.venta.total=parseFloat($scope.venta.total.toFixed(2));
		}
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
		$scope.llenarPags();
	})
	}
	
	$scope.calculaImporte=function(producto){
		var valor=producto.cantidad*producto.precio;
		
		return valor.toFixed(2)*1;
	}
	$scope.cargaProductos(1);
	
	
	
	$scope.guardarVenta= function (){
		console.log();
		if($scope.venta.idCliente === undefined){
			alert("Selecciona un cliente");
		}else{
		ventasService.addVenta($scope.venta).then(function(data){
			alert("La venta ha sido guardada");
			$location.path('/ventasList');
			$window.location.reload();
		});
		}
	}
	
}]);
app.controller("ventaListController",['clientesService','ventasService','tornillosService','herramientasService','$scope','$location','$window',function(clientesService,ventasService,tornillosService,herramientasService,$scope,$location,$window){
	$scope.paginaActual=1;
	$scope.url="/ventas/findAll/"
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
	$scope.ventas = function(page) {
		ventasService.findVentas($scope.url,page).then(
			function(data) {
				$scope.ventas = data;
				console.log(data);
				$scope.llenarPags();
			})
	}

	

	$scope.facturar = function(venta){
		if(venta.idCliente!=0){
			ventasService.facturarVenta(venta).then(
					function(data){
							$scope.factura = data;
						console.log(data);	
						alert("Facturado con éxito");
						$windoy.location.reload();
					})
		}else{
			alert('Esta Venta no tiene asociado un Cliente registrado');
		}
	}
	
	$scope.cargarPagina=function(pag){
		if($scope.paginaActual!=pag){
			$scope.paginaActual=pag;
			$scope.ventas(pag);
		}
	}
	ventasService.numPages().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	});
	$('.datepicker').datepicker({format: 'mm-dd-yyyy'});
	
	$('.input-daterange').datepicker({
	    format: "mm-dd-yyyy"
	});
	
	
	
	$('.input-daterange input').each(function() {
	    $(this).datepicker("format","mm-dd-yyyy");
	});
	
	$scope.ventas(1);
	
	//busqueda por fechas
	$scope.buscar=function(){
		$scope.url
		ventasService.buscar($scope.fechaInicio,$scope.fechaFin).then(function(data){
			$scope.ventas= data;
			$scope.todos=false;
		});
	}

	
}]);
