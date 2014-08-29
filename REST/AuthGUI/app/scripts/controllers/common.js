'use strict';

angular.module('controllers.common', ['ui.bootstrap'])
    .controller('MainCtrl', ['$scope', '$rootScope', '$state',
        function ($scope, $rootScope, $state) {
            $scope.tabs = [
                {heading: 'Users', route: 'users.list', active: false},
                {heading: 'Applications', route: 'clients.list', active: false},
                {heading: 'Tokens', route: 'tokens.list', active: false}
            ];

            $scope.go = function (route) {
                $state.go(route);
            };

            $scope.active = function (route) {
                return $state.is(route);
            };

            $scope.$on('$stateChangeSuccess', function () {
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

    .controller('LoginCtrl', ['$scope', '$state', 'AlertMgr', '$location', 'Auth', '$stateParams', '$sessionStorage', '$animate',
        function ($scope, $state, AlertMgr, $location, Auth, $stateParams, $sessionStorage, $animate) {
            $scope.redirrectUrl = $location.search().redirect_url;
            $scope.$storage = $sessionStorage;
            $scope.credentials = {};
            var loginForm = $('#login-form');

            $scope.login = function (credentials) {
                Auth.login(credentials, function () {
                    //$httpProvider.defaults.headers.common['Authorization'] = 'Bearer ' + response;
                    var returnUrl = $stateParams.redirect_url ? $stateParams.redirect_url : '/';
                    $location.url(returnUrl);
                }, function (error) {
                    error.data.messages.forEach(function (item) {
                        AlertMgr.addAlert('danger', item);
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
        }]);
