app.run(['$rootScope','$http','$location',function ($rootScope,$http,$location) {
	$rootScope.CerrarSesion = function(){
		$http.get("/usuario/cerrarSesion").then(function(response) {
			$rootScope.authenticated = true;
			$rootScope.variable = true;
			$location.path("/login");
		}, function(response) {
			$location.path("/login");
		});
	};
	
	$http.get("/alertas/numAlertas").then(function(response){
		$rootScope.numAlertas=response.data;
	})
}]);