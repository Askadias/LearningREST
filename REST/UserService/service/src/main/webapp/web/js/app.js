'use strict';

var userAdminApp = angular.module('userAdminApp', [
    'ngRoute',
    'userController',
    'userService'
]);

userAdminApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
            when('/users', {
                templateUrl: 'partials/users-list.html',
                controller: 'UsersListCtrl'
            }).
            when('/users/:userEmail', {
                templateUrl: 'partials/user-details.html',
                controller: 'UserDetailsCtrl'
            }).otherwise({
                redirectTo: '/users'
            })
    }]);