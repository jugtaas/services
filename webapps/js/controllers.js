var jugtaasApp = angular.module('jugtaasApp', []);

jugtaasApp.controller('EventListCtrl', function ($scope, $http) {
	
	var onSuccess = function(data) {
		$scope.events = data;
	};

	//$http.get('http://services-jugtaas.rhcloud.com/services/events')
	$http.get('services/events')
	.success(onSuccess)
	.error(function(data, status, headers, config) {
		$http.get('../data/events.json')
		.success(onSuccess)
		.error(function(data, status, headers, config) {
			console.log("Can't retrieve event list");
		});
	})
  	;
});