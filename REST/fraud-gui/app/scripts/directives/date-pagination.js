'use strict';

angular.module('directives.date-pagination', [])

  .directive('datePagination', function () {
    return {
      restrict: 'E',
      templateUrl: 'views/components/date-pagination.html',
      scope: {
        currentDate: '=',
        onDateChange: '&onDateChange'
      },
      replace: true,
      link: function ($scope, elem, attr) {

        $scope.openDatePicker = function () {
          var el = angular.element('#page-date-picker');
          el.datepicker({
            autoclose: true,
            format: 'yyyy-mm-dd'
          });
          el.datepicker('update', moment($scope.currentDate).format("YYYY-MM-DD"));
          el.bind('changeDate', function ($event) {
            $scope.$apply(function () {
              $scope.currentDate = $event.date || $scope.currentDate;
            });
          });
          el.datepicker('show');
        };


        $scope.updateDates = function () {
          $scope.days = [];
          var cur = moment($scope.currentDate).subtract(9, 'd')._d;
          for (var i = 0; i < 19; i++) {
            $scope.days.push(moment(cur).add(i, 'd')._d);
          }
          $scope.onDateChange()
        };

        $scope.$watch(function () {
          return $scope.currentDate;
        }, function () {
          $scope.updateDates();
        }, true);

        $scope.setCurrent = function (date) {
          $scope.currentDate = date;
        };

        $scope.prevNDay = function (num) {
          $scope.currentDate = moment($scope.currentDate).subtract(num, 'd')._d;
        };

        $scope.nextNDay = function (num) {
          $scope.currentDate = moment($scope.currentDate).add(num, 'd')._d;
        };

        $scope.isCurrent = function (day) {
          return $scope.currentDate.getDate() == day.getDate();
        };

        $scope.isWeekend = function (day) {
          return moment(day).weekday() == 0 || moment(day).weekday() == 6;
        };

        $scope.format = function (date, format) {
          return moment(date).format(format)
        };

        $scope.scroll = function ($event, $delta, $deltaX, $deltaY) {
          $event.preventDefault();
          if ($deltaY > 0) {
            $scope.prevNDay(Math.floor($deltaY / 10) || 1);
          }
          if ($deltaY < 0) {
            $scope.nextNDay(Math.floor(-$deltaY / 10) || 1);
          }
        }
      }
    }
  });
