'use strict';

var userController = angular.module('userController', []);

userController.controller('UsersListCtrl', ['$scope', '$routeParams', 'User',
    function ($scope, $routeParams, User) {
        $scope.totalPages = 0;
        $scope.usersCount = 0;
        $scope.headers = [
            {
                title: 'Email',
                value: 'email'
            },
            {
                title: 'Login',
                value: 'login'
            },
            {
                title: 'First Name',
                value: 'firstName'
            },
            {
                title: 'Last Name',
                value: 'lastName'
            },
            {
                title: 'Gender',
                value: 'gender'
            },
            {
                title: 'Birthday',
                value: 'birthDate'
            },
            {
                title: 'Registration',
                value: 'createDate'
            }
        ];

        $scope.filterCriteria = {
            page: 1,
            sortDir: 'ASC',
            sortedBy: 'email'
        };

        //The function that is responsible of fetching the result from the server and setting the grid to the new result
        $scope.fetchResult = function () {
            return User.page.search($scope.filterCriteria).then(function (data) {
                $scope.users = data.content;
                $scope.totalPages = Math.ceil(data.total / data.size);
                $scope.usersCount = data.total;
            }, function () {
                $scope.users = [];
                $scope.totalPages = 0;
                $scope.usersCount = 0;
            });
        };

        //called when navigate to another page in the pagination
        $scope.selectPage = function (page) {
            $scope.filterCriteria.page = page;
            $scope.fetchResult();
        };

        //Will be called when filtering the grid, will reset the page number to one
        $scope.filterResult = function () {
            $scope.filterCriteria.page = 1;
            $scope.fetchResult().then(function () {
                //The request fires correctly but sometimes the ui doesn't update, that's a fix
                $scope.filterCriteria.page = 1;
            });
        };

        //call back function that we passed to our custom directive sortBy, will be called when clicking on any field to sort
        $scope.onSort = function (sortedBy, sortDir) {
            $scope.filterCriteria.sortDir = sortDir;
            $scope.filterCriteria.sortedBy = sortedBy;
            $scope.filterCriteria.page = 1;
            $scope.fetchResult().then(function () {
                //The request fires correctly but sometimes the ui doesn't update, that's a fix
                $scope.filterCriteria.page = 1;
            });
        };

        //manually select a page to trigger an ajax request to populate the grid on page load
        $scope.selectPage(1);
    }]);

userController.controller('UserDetailsCtrl', ['$scope', '$routeParams', 'User',
    function ($scope, $routeParams, User) {
        User.get($routeParams.email).then(function (data) {
            $scope.user = data;
        }, function () {
        });
    }]);