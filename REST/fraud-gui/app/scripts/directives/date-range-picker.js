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

        elem.datepicker({
          autoclose: true,
          format: 'yyyy-mm-dd'
        });
        elem.datepicker().on('hide', function($event){
          $scope.$apply(function () {
            $scope.startDate = new Date(angular.element('#date-range-start').val());
            $scope.endDate = new Date(angular.element('#date-range-end').val());
          });
        });

        /*elem.datetimepicker({
        });
        elem.datetimepicker().on('dp.hide', function($event){
          $scope.$apply(function () {
            $scope.startDate = new Date(angular.element('#date-range-start').val());
            $scope.endDate = new Date(angular.element('#date-range-end').val());
          });
        });*/

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
      }
    }
  });
