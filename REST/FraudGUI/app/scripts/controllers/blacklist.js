'use strict';

angular.module('controllers.blacklist', [])

  .controller('BlackListsCtrl', ['$scope', '$modal', '$stateParams', 'BlackList',
    function ($scope, $modal, $stateParams, BlackList) {
      $scope.totalPages = 0;
      $scope.clientsCount = 0;
      $scope.headers = [
        {
          title: 'Subject',
          value: 'subject'
        },
        {
          title: 'List Type',
          value: 'type'
        },
        {
          title: 'Updated By',
          value: 'updated_by'
        },
        {
          title: 'Created By',
          value: 'created_by'
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
        sorted_by: 'subject'
      };

      //The function that is responsible of fetching the result from the server and setting the grid to the new result
      $scope.fetchResult = function () {
        return BlackList.page($scope.filterCriteria).then(function (response) {
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
        $scope.filterCriteria.sort_dir = sortDir;
        $scope.filterCriteria.sorted_by = sortedBy;
        $scope.filterCriteria.page = 1;
        $scope.fetchResult().then(function () {
          //The request fires correctly but sometimes the ui doesn't update, that's a fix
          $scope.filterCriteria.page = 1;
        });
      };
      $scope.remove = function (client) {
        BlackList.delete(client).then(function (response) {
          $scope.fetchResult();
        }, function (response) {
        });
      };

      //manually select a page to trigger an ajax request to populate the grid on page load
      $scope.selectPage(1);
    }]);
