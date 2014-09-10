'use strict';

angular.module('controllers.blacklist', [])

  .controller('BlackListsCtrl', ['$scope', '$modal', '$stateParams', 'BlackList',
    function ($scope, $modal, $stateParams, BlackList) {
      $scope.startFrom = {type : null, value: null};
      $scope.blacklist = [];

      $scope.fetchList = function(startFrom) {
        BlackList.page(startFrom).then(function (response) {
          $scope.blacklist.push.apply($scope.blacklist, response);
        }, function () {
          $scope.blacklist = [];
        });
      };

      $scope.remove = function (client) {
        BlackList.delete(client).then(function (response) {
          $scope.fetchList($scope.startFrom);
        }, function (response) {
        });
      };
      $scope.fetchList($scope.startFrom);
    }]);
