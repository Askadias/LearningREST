'use strict';

angular.module('authServiceAdmin.controllers', ['ui.bootstrap'])
    .controller('MainCtrl', ['$scope', '$rootScope', 'OAuth',
        function ($scope, $rootScope, OAuth) {

            $scope.login = function () {
                OAuth.getTokenByPopup().then(function (params) {
                    OAuth.verifyAsync(params.access_token).then(function (data) {
                        $sessionStorage.token = params.access_token;
                        $sessionStorage.expires_in = params.expires_in;
                    })
                })
            }
        }])
    .controller('AlertCtrl', ['$rootScope', function ($rootScope) {
        $rootScope.alerts = [];
        $rootScope.closeAlert = function (index) {
            $rootScope.alerts.splice(index, 1);
        };
    }])
    .controller('LoginCtrl', ['$scope', '$state', '$rootScope', '$location', 'Auth', '$stateParams', '$http', '$sessionStorage', 'AUTH_EVENTS',
        function ($scope, $state, $rootScope, $location, Auth, $stateParams, $http, $sessionStorage, AUTH_EVENTS) {
            $scope.redirectUrl = $location.search().redirect_url;
            $scope.$storage = $sessionStorage;
            $scope.credentials = {};

            $scope.login = function (credentials) {
                $rootScope.alerts = [];
                Auth.login(credentials, function (response) {
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess, {
                        user : $sessionStorage.user,
                        redirectUrl: $scope.redirectUrl
                    });
                    $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode(
                        $sessionStorage.user.email + ':' + $sessionStorage.user.password
                    );

                    var returnUrl = $stateParams.redirect_url ? $stateParams.redirect_url : "/";
                    $location.url(returnUrl);
                }, function (error) {
                    error.data.messages.forEach(function (item) {
                        $rootScope.alerts.push({type: 'danger', msg: item})
                    });
                });
            };
            $scope.logout = function () {
                Auth.logout();
                $state.transitionTo('login', {redirect_url: $location.url()});
            };
        }])
    .controller('TokensListCtrl', ['$scope', '$modal', 'Token',
        function ($scope, $modal, Token) {
            $scope.totalPages = 0;
            $scope.tokensCount = 0;
            $scope.headers = [
                {
                    title: 'Token Key',
                    value: 'tokenKey'
                },
                {
                    title: 'Client ID',
                    value: 'clientID'
                },
                {
                    title: 'Type',
                    value: 'type'
                },
                {
                    title: 'Refresh Token',
                    value: 'refreshToken'
                },
                {
                    title: 'Subject',
                    value: 'subject'
                },
                {
                    title: 'Expires In(ms)',
                    value: 'expiresIn'
                },
                {
                    title: 'Issued At',
                    value: 'issuedAt'
                }
            ];

            $scope.filterCriteria = {
                page: 1,
                sortDir: 'ASC',
                sortedBy: 'tokenKey'
            };

            //The function that is responsible of fetching the result from the server and setting the grid to the new result
            $scope.fetchResult = function () {
                return Token.page($scope.filterCriteria).then(function (response) {
                    $scope.tokens = response.content;
                    $scope.totalPages = Math.ceil(response.total / response.size);
                    $scope.tokensCount = response.total;
                }, function () {
                    $scope.tokens = [];
                    $scope.totalPages = 0;
                    $scope.tokensCount = 0;
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
    .controller('TokenDetailsCtrl', ['$scope', '$stateParams', 'Token',
        function ($scope, $stateParams, Token) {
            Token.get($stateParams.tokenKey).then(function (response) {
                $scope.token = response;
            }, function (response) {
            });
        }])
    .controller('ClientsListCtrl', ['$scope', '$modal', '$stateParams', 'Client',
        function ($scope, $modal, $stateParams, Client) {
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
                return Client.page.search($scope.filterCriteria).then(function (response) {
                    $scope.clients = response.content;
                    $scope.totalPages = Math.ceil(response.total / response.size);
                    $scope.clientsCount = response.total;
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
            $scope.remove = function (client) {
                Client.delete(client).then(function (response) {
                    $scope.fetchResult();
                }, function (response) {
                });
            };

            //manually select a page to trigger an ajax request to populate the grid on page load
            $scope.selectPage(1);
        }])
    .controller('ClientDetailsCtrl', ['$scope', '$stateParams', 'Client',
        function ($scope, $stateParams, Client) {
            $scope.mode = $stateParams.mode;

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

            Client.get($stateParams.clientID).then(function (response) {
                if (response) {
                    $scope.client = response;
                    $scope.original = angular.copy($scope.client)
                }
            }, function () {
            });

            $scope.discard = function () {
                $scope.client = angular.copy($scope.original);
            };

            $scope.save = function () {
                $scope.original = angular.copy($scope.client);
                switch ($scope.mode) {
                    case 'new' :
                        Client.add($scope.client).then(function (response) {

                        }, function (response) {

                        });
                        break;
                    case 'edit' :
                        $scope.client.save();
                        break;
                }
                $scope.cancel();
            };

            $scope.addRole = function (role) {
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

            $scope.discard();
        }])
    .controller('AuthCtrl', ['$scope', 'OAuth', '$location',
        function ($scope, OAuth, $location) {
            //$scope.user = $location.search().user;
            $scope.authorize = function () {
                OAuth.getTokenByPopup().then(function (params) {
                    OAuth.verifyAsync(params.access_token).then(function (data) {
                        $sessionStorage.token = params.access_token;
                        $sessionStorage.expires_in = params.expires_in;
                    })
                })
            }
        }]);