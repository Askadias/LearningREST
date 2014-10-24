'use strict';

angular.module('controllers.common', ['ngAnimate'])
  .controller('AppCtrl', ['$scope', '$rootScope', '$state',
    function ($scope, $rootScope, $state) {
      $scope.now = new Date();
      $rootScope.app = {
        settings: {
          asideFolded : false,
          velocity: {
            metricsSearch: {
              filterFolded : true
            },
            dataSearch: {
              filterFolded : true
            }
          }
        }
      }
    }]);
