'use strict';

angular.module('directives.date-range-picker', [])

  .directive('dateRangePicker', function () {
    return {
      restrict: 'E',
      templateUrl: 'views/components/date-range-picker.html',
      scope: {
        startDate: '=',
        endDate: '=',
        onRangeChange: '&onRangeChange'
      },
      replace: true,
      link: function ($scope, elem, attr) {

        angular.element('#date-range-start').datetimepicker();
        angular.element('#date-range-end').datetimepicker();
        angular.element('#date-range-start').on("dp.change",function (e) {
          angular.element('#date-range-end').data("DateTimePicker").setMinDate(e.date);
          $scope.startDate = new Date(e.date);
        });
        angular.element('#date-range-end').on("dp.change",function (e) {
          angular.element('#date-range-start').data("DateTimePicker").setMaxDate(e.date);
          $scope.endDate = new Date(e.date);
        });

        $scope.$watch(function () {
          return $scope.startDate;
        }, function () {
          $scope.onRangeChange();
        }, true);
        $scope.$watch(function () {
          return $scope.endDate;
        }, function () {
          $scope.onRangeChange();
        }, true);

        $scope.openDateRangePicker = function() {

        }
      }
    }
  });
