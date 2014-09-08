'use strict';

angular.module('controllers.common', ['ngAnimate'])
  .controller('MainCtrl', ['$scope', '$state',
    function ($scope, $state) {
    }])

  .controller('LoginCtrl', ['$scope', '$state', 'AlertMgr', '$location', 'Auth', '$stateParams', '$sessionStorage', '$animate',
    function ($scope, $state, AlertMgr, $location, Auth, $stateParams, $sessionStorage, $animate) {
      $scope.redirrectUrl = $location.search().redirect_url;
      $scope.$storage = $sessionStorage;
      $scope.credentials = {};
      $scope.rememberMe = false;
      $scope.alerts = [];
      var loginForm = $('#login-form');

      $scope.login = function () {
        Auth.login($scope.credentials, $scope.rememberMe, function () {
          var returnUrl = $stateParams.redirect_url ? $stateParams.redirect_url : '/';
          $location.url(returnUrl);
        }, function (error) {
          $scope.alerts = [];
          error.data.messages.forEach(function (item) {
            $scope.alerts.push({
              msg: item,
              type: 'danger'
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
