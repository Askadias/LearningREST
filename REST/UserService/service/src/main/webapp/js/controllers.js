'use strict';

angular.module('userServiceAdmin.controllers', ['ui.bootstrap'])
    .controller('MainCtrl', ['$scope', '$routeSegment', function ($scope, $routeSegment) {
        $scope.$routeSegment = $routeSegment;
    }])
    .controller('UsersListCtrl', ['$scope', '$modal', '$routeSegment', 'User',
        function ($scope, $modal, $routeSegment, User) {
            $scope.$routeSegment = $routeSegment;
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

            $scope.open = function (size, email) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/users/details.html',
                    controller: 'UserDetailsModalCtrl',
                    backdrop: true,
                    size: size,
                    resolve: {
                        email: function () {
                            return email;
                        }
                    }
                });

                modalInstance.result.then(function (user) {
                    $scope.newUser = user;
                }, function () {
                });
            };
        }])
    .controller('UserDetailsModalCtrl', ['$scope', '$modalInstance', 'email', 'User',
        function ($scope, $modalInstance, email, User) {
            User.get(email).then(function (data) {
                $scope.user = data;
            }, function () {
            });

            $scope.ok = function () {
                $modalInstance.close($scope.user);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }])
    .controller('UserDetailsCtrl', ['$scope', '$routeSegment', 'User',
        function ($scope, $routeSegment, User) {
            $scope.$routeSegment = $routeSegment;
            User.get($routeSegment.$routeParams.email).then(function (data) {
                $scope.user = data;
            }, function () {
            });
        }])
    .controller('ClientsListCtrl', ['$scope', '$modal', '$routeSegment', 'Client',
        function ($scope, $modal, $routeSegment, Client) {
            $scope.$routeSegment = $routeSegment;
            $scope.totalPages = 0;
            $scope.clientsCount = 0;
            $scope.headers = [
                {
                    title: 'ClientID',
                    value: 'clientID'
                },
                {
                    title: 'Application Name',
                    value: 'applicationName'
                },
                {
                    title: 'URI',
                    value: 'applicationWebUri'
                },
                {
                    title: 'Last Updated By',
                    value: 'updatedBy'
                },
                {
                    title: 'Update Date',
                    value: 'updateDate'
                },
                {
                    title: 'Created By',
                    value: 'createdBy'
                },
                {
                    title: 'Create Date',
                    value: 'createDate'
                }
            ];

            $scope.filterCriteria = {
                page: 1,
                sortDir: 'ASC',
                sortedBy: 'applicationName'
            };

            //The function that is responsible of fetching the result from the server and setting the grid to the new result
            $scope.fetchResult = function () {
                return Client.page.search($scope.filterCriteria).then(function (data) {
                    $scope.clients = data.content;
                    $scope.totalPages = Math.ceil(data.total / data.size);
                    $scope.clientsCount = data.total;
                }, function () {
                    $scope.clients = [];
                    $scope.totalPages = 0;
                    $scope.clientsCount = 0;
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
        }])
    .controller('ClientDetailsCtrl', ['$scope', '$routeSegment', 'Client',
        function ($scope, $routeSegment, Client) {
            $scope.$routeSegment = $routeSegment;

            $scope.roles = ['reader', 'writer', 'admin'];
            $scope.client = {
                applicationName: '',
                clientSecret: '',
                applicationDescription: '',
                applicationWebUri: '',
                redirectUris: [],
                allowedGrantTypes: []
            };
            $scope.original = angular.copy($scope.client);

            Client.get($routeSegment.$routeParams.clientID).then(function (data) {
                if (data) {
                    $scope.client = data;
                    $scope.original = angular.copy($scope.client)
                }
            }, function () {
            });

            $scope.cancel = function () {
                $scope.client = angular.copy($scope.original);
            };

            $scope.save = function () {
                $scope.original = angular.copy($scope.client);
                Client.add($scope.client).then(function (data) {

                }, function () {

                });
                //$scope.client.add();
                $scope.cancel();
            };

            $scope.addRole = function(role) {
                if ($scope.client.allowedGrantTypes.indexOf(role) == -1) {
                    $scope.client.allowedGrantTypes.push(role);
                }
            };

            $scope.isCancelDisabled = function () {
                return angular.equals($scope.original, $scope.client);
            };

            $scope.isSaveDisabled = function () {
                return $scope.myForm.$invalid || angular.equals($scope.original, $scope.client);
            };

            $scope.cancel();
        }]);