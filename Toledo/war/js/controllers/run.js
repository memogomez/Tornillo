app.run(['$rootScope','$http','$location',function ($rootScope,$http,$location) {
	$rootScope.CerrarSesion = function(){
		$http.get("/usuario/cerrarSesion").then(function(response) {
			$rootScope.variable = false;
			$location.path("/login");
		}, function(response) {
			$location.path("/login");
		});
	};
}]);