'use strict';

var userController = angular.module('userController', []);

userController.controller('UsersListCtrl', ['$scope', 'UserService',
    function ($scope, UserService) {
        $scope.users = UserService.getUsers();
        $scope.order = 'email';
    }]);

userController.controller('UserDetailsCtrl', ['$scope', '$routeParams','UserService',
    function ($scope, $routeParams, UserService) {
        $scope.user = UserService.get({userEmail: $routeParams.email});
    }]);