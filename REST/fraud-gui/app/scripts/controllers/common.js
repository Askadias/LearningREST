'use strict';

angular.module('controllers.common', ['ngAnimate'])
  .controller('AppCtrl', ['$scope', '$state',
    function ($scope, $state) {
      $scope.app = {
        settings: {
          asideFolded : false
        }
      }
    }]);
