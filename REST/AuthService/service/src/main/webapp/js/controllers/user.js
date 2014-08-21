'use strict';

angular.module('authServiceAdmin.controllers.user', ['ui.bootstrap'])

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
                    value: 'first_name'
                },
                {
                    title: 'Last Name',
                    value: 'last_name'
                },
                {
                    title: 'Gender',
                    value: 'gender'
                },
                {
                    title: 'Update Date',
                    value: 'update_date'
                },
                {
                    title: 'Create Date',
                    value: 'create_date'
                }
            ];

            $scope.filterCriteria = {
                page: 1,
                sort_dir: 'ASC',
                sorted_by: 'email',
                gender: ''
            };

            //The function that is responsible of fetching the result from the server and setting the grid to the new result
            $scope.fetchResult = function () {
                var filter = angular.copy($scope.filterCriteria);
                filter.gender = filter.gender || null;
                return User.page(filter).then(function (response) {
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
                $scope.filterCriteria.sort_dir = sortDir;
                $scope.filterCriteria.sorted_by = sortedBy;
                $scope.filterCriteria.page = 1;
                $scope.fetchResult().then(function () {
                    //The request fires correctly but sometimes the ui doesn't update, that's a fix
                    $scope.filterCriteria.page = 1;
                });
            };

            $scope.remove = function (user) {
                User.delete(user).then(function (response) {
                    $scope.fetchResult();
                }, function (response) {
                });
            };

            //manually select a page to trigger an ajax request to populate the grid on page load
            $scope.selectPage(1);

            $scope.open = function (size, login) {

                var modalInstance = $modal.open({
                    templateUrl: 'partials/users/details.html',
                    controller: 'UserDetailsModalCtrl',
                    backdrop: true,
                    size: size,
                    resolve: {
                        login: function () {
                            return login;
                        }
                    }
                });

                modalInstance.result.then(function (user) {
                    $scope.newUser = user;
                }, function () {
                });
            };
        }])

    .controller('UserDetailsModalCtrl', ['$scope', '$modalInstance', 'login', 'User',
        function ($scope, $modalInstance, login, User) {
            User.get(login).then(function (response) {
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
/*
    .controller('UserDetailsCtrl', ['$scope', '$stateParams', 'User',
        function ($scope, $stateParams, User) {
            User.get($stateParams.login).then(function (response) {
                $scope.user = response;
            }, function (response) {
            });
        }])*/

    .controller('UserDetailsCtrl', ['$scope', '$stateParams', 'User', '$state', 'AlertMgr',
        function ($scope, $stateParams, User, $state, AlertMgr) {
            $scope.mode = $stateParams.mode;

            $scope.roles = ['user', 'admin'];
            $scope.user = {
                email: '',
                password: '',
                login: '',
                first_name: '',
                last_name: '',
                gender: null,
                groups: []
            };
            $scope.original = angular.copy($scope.user);

            if ($scope.mode === 'edit') {
                User.get($stateParams.login).then(function (response) {
                    if (response) {
                        $scope.user = response;
                        $scope.original = angular.copy($scope.user)
                    }
                }, function () {
                });
            }

            $scope.discard = function () {
                $scope.user = angular.copy($scope.original);
            };

            $scope.save = function () {
                $scope.user.gender = $scope.user.gender || null;
                $scope.original = angular.copy($scope.user);
                switch ($scope.mode) {
                    case 'new' :
                        User.add($scope.user).then(function (response) {
                            $state.go('users.details', {login : $scope.user.email, mode : 'edit'});
                        }, function (error) {
                            error.data.messages.forEach(function (item) {
                                AlertMgr.addAlert('danger', item)
                            });
                        });
                        break;
                    case 'edit' :
                        $scope.user.save();
                        break;
                }
                $scope.cancel();
            };

            $scope.isCancelDisabled = function () {
                return angular.equals($scope.original, $scope.user);
            };

            $scope.isSaveDisabled = function () {
                return $scope.userForm.$invalid || angular.equals($scope.original, $scope.user);
            };

            $scope.discard();
        }]);
