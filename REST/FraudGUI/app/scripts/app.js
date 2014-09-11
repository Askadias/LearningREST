'use strict';

angular.module('fraudAdmin', [
  'ngStorage',
  'ngSanitize',
  'ngAnimate',
  'restangular',
  'ui.bootstrap',
  'ui.router',
  'controllers.common',
  'controllers.blacklist',
  'controllers.velocity',
  'services.config',
  'services.blacklist',
  'services.velocity',
  'directives',
  'filters'
])
  .config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', 'RestangularProvider', 'config',
    function ($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, RestangularProvider, config) {

      RestangularProvider.setDefaultHeaders({
        'Content-Type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
      });

      RestangularProvider.setBaseUrl(config.fraudEndpoint);

      $urlRouterProvider.otherwise('/blacklist/');

      $stateProvider
        .state('blacklist', {
          abstract: true,
          templateUrl: 'views/layout.html',
          controller: 'MainCtrl'
        })
        .state('blacklist.list', {
          url: '/blacklist/',
          templateUrl: 'views/blacklist/list.html',
          controller: 'BlackListsCtrl'
        })
        .state('velocity', {
          abstract: true,
          templateUrl: 'views/layout.html',
          controller: 'MainCtrl'
        })
        .state('velocity.config', {
          abstract: true,
          templateUrl: 'views/velocity/stumb.html',
          controller: 'MainCtrl'
        })
        .state('velocity.config.list', {
          url: '/velocity/config/',
          templateUrl: 'views/velocity/config/list.html',
          controller: 'VelocityConfigsListCtrl'
        })
        .state('velocity.config.details', {
          url: '/velocity/config/:metric_type/:mode/',
          templateUrl: 'views/velocity/config/details.html',
          controller: 'VelocityConfigDetailsCtrl'
        });

      // FIX for trailing slashes. Gracefully "borrowed" from https://github.com/angular-ui/ui-router/issues/50
      $urlRouterProvider.rule(function ($injector, $location) {
        if ($location.protocol() === 'file') {
          return;
        }

        // Note: misnomer. This returns a query object, not a search string
        var path = $location.path(),
          search = $location.search(),
          params = [];

        // check to see if the path already ends in '/'
        if (path[path.length - 1] === '/') {
          return;
        }

        // If there was no search string / query params, return with a `/`
        if (Object.keys(search).length === 0) {
          return path + '/';
        }

        // Otherwise build the search string and return a `/?` prefix
        angular.forEach(search, function (v, k) {
          params.push(k + '=' + v);
        });
        return path + '/?' + params.join('&');
      });

      $locationProvider.html5Mode(true);
    }]);
