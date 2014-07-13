'use strict';

var UserAdminApp = angular.module('UserAdminApp', [
    'ngRoute',
    'userController',
    'userService',
    'userDirectives',
    'restangular'
]);

UserAdminApp.config(['$routeProvider', 'RestangularProvider',
    function ($routeProvider, RestangularProvider) {
        RestangularProvider.setBaseUrl('service/rest/v1/');
        $routeProvider.
            when('/users', {
                templateUrl: 'partials/users-list.html',
                controller: 'UsersListCtrl'
            }).
            when('/users/:email', {
                templateUrl: 'partials/user-details.html',
                controller: 'UserDetailsCtrl'
            }).otherwise({
                redirectTo: '/users'
            })
    }]);