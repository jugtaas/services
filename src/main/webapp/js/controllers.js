
var jugtaasApp = angular.module('jugtaasApp', ['ngRoute']);

jugtaasApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'partials/home.html',
      }).
      when('/events', {
        templateUrl: 'partials/incontri.html',
        controller: 'EventListController'
      }).
      when('/events/new', {
        templateUrl: 'partials/incontro.html',
      }).
      when('/events/:id', {
        templateUrl: 'partials/incontro.html',
      }).
      when('/mailinglist', {
        templateUrl: 'partials/mailinglist.html',
      }).
      when('/users', {
        templateUrl: 'partials/users.html',
      }).
      when('/manifesto', {
        templateUrl: 'partials/manifesto.html',
      }).
      when('/contacts', {
        templateUrl: 'partials/contacts.html',
      }).
      otherwise({
        redirectTo: 'home',
      });
  }
]);

jugtaasApp.directive('isActiveNav', [ '$location', function($location) {
	return {
		restrict: 'A',
		link: function(scope, element) {
			scope.location = $location;
			scope.$watch('location.path()', function(currentPath) {
				if('#' + currentPath === element[0].attributes['href'].nodeValue) {
					element.parent().addClass('active');
				} else {
					element.parent().removeClass('active');
				}
			});
		}
	};
}]);


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

/*
	var findById = function(id, list) {
		for(idx in list) {
			var element = list[idx];
			if(element.id == id) {
				return element;
			}
		}
	}
*/

	var getSpeakersFrom = function(speakers, persons) {
		for(s in speakers) {
			var speaker = speakers[s];

			for(p in persons) {
				var person = persons[p];
				if(person.id == speaker.id) {
					speakers[s] = person;
					break;
				}
			}
		}

		return speakers;
	};


	var personsCall = $http.get('services/persons');
	var eventCall = $http.get('services/events/' + $routeParams.id);

	$q.all([personsCall, eventCall])
	.then(function(results) {
		var personsData = results[0].data._links;
		var eventData = results[1].data;

		var personsURLs = [];
		for(key in personsData) {
			personsURLs[parseInt(key)] = $http.get(personsData[key]);
		}
		
		$q.all(personsURLs)
		.then(function(innerResults) {
			var persons = []
			for(i in innerResults) {
				persons[i] = innerResults[i].data;
			}

			$scope.persons = persons;

			var speakers = getSpeakersFrom(eventData.speakers, $scope.persons);
			var event = eventData;
			event.speakers = speakers;

			$scope.event = event;
		});


	});


});