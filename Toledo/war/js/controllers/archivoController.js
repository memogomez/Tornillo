app.service('fileService', function () {
    var file = {};
    var fileService = {};

    fileService.getFile = function (name) {
        return file[name];
    };

    fileService.setFile = function (newFile, index, name) {
        if (index === 0 && file[name] === undefined)
          file[name] = [];
        file[name][index] = newFile;
    };

    return fileService;
})
app.directive('multipleFileModel', function (fileService) {
    return{
    	restrict: 'EA',
		scope: {
			setFileData: "&"
		},
		link:function (scope, element, attrs) {
            element.bind('change', function () {
            var index;
            var index_file = 0;
            for (index = 0; index < element[0].files.length; index++) {
              fileService.setFile(element[0].files[index], index_file, attrs.multipleFileModel);
              index_file++;
            }
            index_file = 0;
        });
    }
    }
});


app.service('archivoService',['$http','$q',function($http,$q){
	this.sendfile=function(cadena){
		var d = $q.defer();
		$http.post("/productos/addMultipleH/",cadena).then(
				function(response) {
					console.log(response);
					d.resolve(response.data);
				},
				function(response) {
				});
		return d.promise;
	}
	this.sendfile2=function(cadena,url){
		var d = $q.defer();
		$http.post(url,cadena).then(
				function(response) {
					console.log(response);
					d.resolve(response.data);
				},
				function(response) {
				});
		return d.promise;
	}
	
	
	 this.uploadFileToUrl = function(data,url) {
   	  var d= $q.defer();
       var fd = new FormData();
       var indice= data.length;
       for(var i = 0; i<indice; i++){
       	fd.append('files'+i, data[i]);
       }
//fd.append('files', data);
       fd.append('length',indice);
//       fd.append('idEvento',id);
//       alert(url);
       $http.post(url, fd, {
    	   transformRequest: angular.identity,
           headers: {'Content-Type': undefined}
         })
         .success(function(response, status, headers, config) {
           console.log(response);
         })
         .error(function(error, status, headers, config) {
           console.log(error);
         });
       return d.promise;
     }
}]);

app.controller('archivoController',['$scope','archivoService','fileService',function($scope,archivoService,fileService){
	$scope.mensaje="";
	$scope.ver=false;
	
	document.querySelector('input').addEventListener('change', function() {

		  var reader = new FileReader();
		  reader.onload = function() {

		    var arrayBuffer = this.result;
//		      array = new Uint8Array(arrayBuffer),
//		      binaryString = String.fromCharCode.apply(null, array);
		    console.log(arrayBuffer);
		    $('#cadena').text(arrayBuffer);
		  }
		  reader.readAsText(this.files[0]);

		}, false);
	$scope.ok=function(){
		var c= $('#cadena').text();
		archivoService.sendfile(c).then(function(data){
			console.log(data);
			$scope.mensaje= data;
		});
		
//		$scope.images = fileService.getFile("b_pics");
//		
//		if ($scope.images.length) {
//			var file = $scope.images;
//			archivoService.uploadFileToUrl(file, "/file/upload").then(
//					function(data) {
//						var id= data.idEvento;
//						var notfound=true;
//						for(var i = 0;i< $scope.eventos.length;i++){
//							if($scope.eventos[i].idEvento==id){
//								$scope.eventos[i]=data;
//								notfound=false;
//								break;
//							}
//						}
//						if(notfound){
//							$scope.eventos.push(data);
//						}
//						// recursivo
//					});
//		}
		
	}
}]);