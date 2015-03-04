var jugtaasApp = angular.module('jugtaasApp', ['ngRoute']);

jugtaasApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/events', {
        templateUrl: 'partials/incontri.html',
        controller: 'EventListCtrl'
      }).
      when('/events/:id', {
        templateUrl: 'partials/incontro.html',
        controller: 'EventController'
      }).
      otherwise({
        redirectTo: 'partials/incontri.html',
        controller: 'EventListCtrl'
      });
  }]);

function expandLinks(links) {
	var ret = [];
	for(key in links) {
		var url = links[key];
		$.ajax({
			url: url,
			async: false,
			success: function(data) {
				ret[parseInt(key)] = data;
			}
		});
	}
	return ret;
}

function speakerToString(speaker) {
	var string = speaker.name + " " + speaker.surname;
	if(speaker.email) {
		string = '<a href="mailto:' + speaker.email + '">' + string + '</a>';
	} else if(speaker.url) {
		string = '<a href="' + speaker.url + '">' + string + '</a>';
	}
	return string;
}

function speakersToString(speakers) {
	for(idx in speakers) {
		speakers[idx] = speakerToString(speakers[idx]);
	}

	return speakers.join(", ");
}

jugtaasApp.controller('EventListCtrl', function ($scope, $http) {
	
	var onSuccess = function(data) {
		var events = expandLinks(data._links);
		for(idx in events) {
			var event = events[idx];
			/*
			if(event.speakers) {
				event.speakers = speakersToString(event.speakers);
			}
			*/
		}
		$scope.events =  events;
	};

	$http.get('services/events')
	.success(onSuccess)
	.error(function(data, status, headers, config) {
		console.log("fallback events call");
		$http.get('../data/events.json')
		.success(onSuccess)
		.error(function(data, status, headers, config) {
			console.log("Can't retrieve event list");
		});
	})
  	;
});

jugtaasApp.controller('EventController', function($scope, $http, $routeParams) {

	$http.get('services/events/' + $routeParams.id)
	.success(function(data) {
		delete data.speakers;
		$scope.event = data;
	});

	$scope.update = function(event) {
		$scope.event = angular.copy(event);

		$scope.event.id = 2;

		$http.put('services/events/2', $scope.event)
		.success(function(data) {
			alert('update success');
		});
	};

	$scope.reset = function() {
		$scope.event = angular.copy($scope.event);
	};

	//$scope.reset();
  });