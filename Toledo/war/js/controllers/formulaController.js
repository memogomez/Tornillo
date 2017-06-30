app.service('formulaService',['$http','$q',function($http,$q){
	this.aplicar=function(impuesto,descuento,ganancia){
		var d = $q.defer();
		var send=impuesto+","+descuento+","+ganancia;
		$http.post("/productos/aplicaFormula/", send).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	};
}]);

app.controller('formulaController',['$scope','formulaService',function($scope,formulaService){
	$scope.aplicaFormula=function(){
		formulaService.aplicar($scope.impuesto,$scope.descuento,$scope.ganancia).then(function(data){
			alert("Se ha aplicado la formula");
		});
	}
}]);