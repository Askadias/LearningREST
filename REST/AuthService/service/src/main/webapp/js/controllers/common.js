'use strict';

angular.module('authServiceAdmin.controllers.common', ['ui.bootstrap'])
    .controller('MainCtrl', ['$scope', '$rootScope', 'OAuth', '$state',
        function ($scope, $rootScope, OAuth, $state) {
            $scope.tabs = [
                {heading: "Users", route: "users.list", active: false},
                {heading: "Applications", route: "clients.list", active: false},
                {heading: "Tokens", route: "tokens.list", active: false}
            ];

            $scope.go = function (route) {
                $state.go(route);
            };

            $scope.active = function (route) {
                return $state.is(route);
            };

            $scope.$on("$stateChangeSuccess", function () {
                $scope.tabs.forEach(function (tab) {
                    tab.active = $scope.active(tab.route);
                });
            });
        }])

    .controller('AlertCtrl', ['$scope', 'AlertMgr',
        function ($scope, AlertMgr) {
            $scope.alerts = AlertMgr.alerts;
            $scope.closeAlert = function (type) {
                delete $scope.alerts[type];
            };
        }])

    .controller('LoginCtrl', ['$scope', '$state', 'AlertMgr', '$location', 'Auth', '$stateParams', '$http', '$sessionStorage', '$animate',
        function ($scope, $state, AlertMgr, $location, Auth, $stateParams, $http, $sessionStorage, $animate) {
            $scope.redirrectUrl = $location.search().redirect_url;
            $scope.$storage = $sessionStorage;
            $scope.credentials = {};
            var loginForm = $('#login-form');

            $scope.login = function (credentials) {
                Auth.login(credentials, function (response) {
                    $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode(
                        $sessionStorage.user.email + ':' + $sessionStorage.user.password
                    );

                    var returnUrl = $stateParams.redirect_url ? $stateParams.redirect_url : "/";
                    $location.url(returnUrl);
                }, function (error) {
                    error.data.messages.forEach(function (item) {
                        AlertMgr.addAlert('danger', item)
                    });
                    $animate.addClass(loginForm, 'shake', function () {
                        $animate.removeClass(loginForm, 'shake');
                    });
                });
            };
            $scope.logout = function () {
                Auth.logout();
                $state.transitionTo('login', {redirect_url: $location.url()});
            };
        }])

    .controller('AuthCtrl', ['$scope', 'OAuth', '$location',
        function ($scope, OAuth, $location) {
            //$scope.user = $location.search().user;
            $scope.loginByPopup = function () {
                OAuth.getTokenByPopup().then(function (params) {
                    OAuth.verifyAsync(params.access_token).then(function (data) {
                        $sessionStorage.token = params.access_token;
                        $sessionStorage.expires_in = params.expires_in;
                    })
                })
            }
        }]);
