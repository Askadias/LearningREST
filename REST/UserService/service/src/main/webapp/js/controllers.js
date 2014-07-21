'use strict';

angular.module('userServiceAdmin.controllers', ['ui.bootstrap'])
    .controller('MainCtrl', ['$scope', '$rootScope', '$location', 'Auth', '$stateParams',
        function ($scope, $rootScope, $location, Auth, $stateParams) {
            $scope.redirrectUrl = $location.search().redirect_url;
            $scope.currentUser = Auth.user;
            $scope.userRoles = Auth.userRoles;
            $scope.accessLevels = Auth.accessLevels;

            $scope.login = function (credentials) {
                Auth.login(credentials, function (response) {
                    $scope.currentUser = Auth.user;
                    if(!!$scope.redirrectUrl) {
                        window.location.replace($scope.redirrectUrl);
                    } else {
                        $location.path('/');
                    }
                }, function (error) {
                    $scope.error = error;
                });
            };
            $scope.register = function (credentials) {
                Auth.register(credentials).then(function (response) {
                    $scope.currentUser = Auth.user;
                    $location.path('/');
                }, function (error) {
                    $scope.error = error;
                })
            };
            $scope.logout = function () {
                Auth.logout();
                $scope.currentUser = Auth.user;
                $location.path('/app/login');
            };
        }])
    .controller('UsersListCtrl', ['$scope', '$modal', 'User',
        function ($scope, $modal, User) {
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
                return User.page.search($scope.filterCriteria).then(function (response) {
                    $scope.users = response.content;
                    $scope.totalPages = Math.ceil(response.total / response.size);
                    $scope.usersCount = response.total;
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
            User.get(email).then(function (response) {
                $scope.user = response;
            }, function (response) {
            });

            $scope.ok = function () {
                $modalInstance.close($scope.user);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }])
    .controller('UserDetailsCtrl', ['$scope', '$stateParams', 'User',
        function ($scope, $stateParams, User) {
            User.get($stateParams.email).then(function (response) {
                $scope.user = response;
            }, function (response) {
            });
        }]);