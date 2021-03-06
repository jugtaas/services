
var jugtaasApp = angular.module('jugtaasApp', ['ngRoute']);

jugtaasApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/events', {
        templateUrl: 'partials/incontri.html',
        controller: 'EventListController'
      }).
      when('/events/new', {
        templateUrl: 'partials/incontro.html',
        //controller: 'EventController'
      }).
      when('/events/:id', {
        templateUrl: 'partials/incontro.html',
        //controller: 'EventController'
      }).
      otherwise({
        redirectTo: 'partials/incontri.html',
        controller: 'EventListController'
      });
  }
]);


function expandLinks(links) {
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

/*
jugtaasApp.controller('EventListController', function ($scope, $http) {

	$http.get('services/events')
	.success(function(data) {
		var events = expandLinks(data._links);
		for(idx in events) {
			var event = events[idx];
		}
		$scope.events =  events;
	});
});
*/

jugtaasApp.controller('EventListController', function ($scope, $http) {

	$http.get('services/events')
	.success(function(data) {
		$scope.events = [];

		angular.forEach(data._links, function (eventUrl, idx) {
          $http.get(eventUrl).then(function(response) {
            $scope.events[idx] = response.data;
          })
        });
	});
});

jugtaasApp.controller('EventController', function($scope, $http, $route, $routeParams, $q, $location) {

	var getSpeakersFrom = function(speakers, persons) {
		for(idx in speakers) {
			var speaker = speakers[idx];

			for(idx in $scope.persons) {
				var person = $scope.persons[idx];
				if(person.id == speaker.id) {
					speaker = person;
					break;
				}
			}

			speakers[idx] = speaker;
		}

		return speakers;
	};

	$scope.event = {};

	var idToObject = function(id) {
		return {id: id};
	};

	$scope.update = function(event) {
		$scope.event = angular.copy(event);

		var id = $scope.event.id;

		$http.put('services/events/' + id, $scope.event)
		.success(function(data) {
			$route.reload();
		});
	};

	$scope.create = function(event) {
		$scope.event = angular.copy(event);


		$http.post('services/events/', $scope.event)
		.success(function(data) {
			$location.path("events/" + data.id);
		});
	};

	$scope.reset = function() {
		$scope.event = angular.copy($scope.event);
	};

	if(!$routeParams.id) {
		return;
	}

	var findById = function(id, list) {
		for(idx in list) {
			var element = list[idx];
			if(element.id == id) {
				return element;
			}
		}
	}

	$http.get('services/events/' + $routeParams.id)
	.success(function(data) {
		$scope.event = data;
		$scope.event.speakers = getSpeakersFrom($scope.event.speakers, $scope.persons);
	});
});

jugtaasApp.controller('PersonListController', function ($scope, $http) {

	$http.get('services/persons')
	.success(function(data) {
		$scope.persons = [];

		angular.forEach(data._links, function (personUrl, idx) {
          $http.get(personUrl).then(function(response) {
            $scope.persons[idx] = response.data;
          })
        });
	});
});