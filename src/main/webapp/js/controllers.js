var jugtaasApp = angular.module('jugtaasApp', []);

jugtaasApp.controller('EventListCtrl', function ($scope, $http) {
	
	var onSuccess = function(data) {
		var events = [];
		$.each(data._links, function(key, url) {
			$http.get(url)
				.success(function(data) {
					events[parseInt(key)] = data;
				});
		});

		$scope.events =  events;
	};

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