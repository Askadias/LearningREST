'use strict';

angular.module('controllers.common', ['ngAnimate'])
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

  .controller('LoginCtrl', ['$scope', '$state', 'AlertMgr', '$location', 'Auth', '$stateParams', '$sessionStorage', '$animate', '$alert',
    function ($scope, $state, AlertMgr, $location, Auth, $stateParams, $sessionStorage, $animate, $alert) {
      $scope.redirrectUrl = $location.search().redirect_url;
      $scope.$storage = $sessionStorage;
      $scope.credentials = {};
      $scope.rememberMe = false;
      $scope.alert = null;
      var loginForm = $('#login-form');

      $scope.login = function () {
        Auth.login($scope.credentials, $scope.rememberMe, function () {
          var returnUrl = $stateParams.redirect_url ? $stateParams.redirect_url : '/';
          $location.url(returnUrl);
        }, function (error) {
          if (!!$scope.alert) {
            $scope.alert.hide();
          }
          error.data.messages.forEach(function (item) {
            $scope.alert = $alert({
              animation: 'alert-pupup',
              title: 'Authentication error',
              content: item,
              container: '#alerts-container',
              duration: 3,
              type: 'danger',
              keyboard: true,
              show: true
            });
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
