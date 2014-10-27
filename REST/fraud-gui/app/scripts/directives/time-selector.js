'use strict';

angular.module('directives.time-selector', [])

  .directive('timeSelector', function () {
    return {
      restrict: 'E',
      templateUrl: 'views/components/time-selector.html',
      scope: {
        showDays: '@',
        showHours: '@',
        showMinutes: '@',
        showSeconds: '@',
        timestamp: '='
      },
      replace: true,
      link: function (scope, elem, attr) {

        scope.$watch(scope.timestamp, function (value) {
          scope.seconds = Math.abs((scope.timestamp / 1000) % 60);
          scope.minutes = Math.abs((scope.timestamp / 60000) % 60);
          scope.hours = Math.abs((scope.timestamp / 3600000) % 24);
          scope.days = Math.abs((scope.timestamp / 86400000));
        });

        scope.increaseDays = function () {
          scope.days = scope.days < 999 ? scope.days + 1 : scope.days;
          scope.updateTimestamp();
        };

        scope.decreaseDays = function () {
          scope.days = scope.days > 0 ? scope.days - 1 : scope.days;
          scope.updateTimestamp();
        };

        scope.increaseHours = function () {
          scope.hours = scope.hours < 23 ? scope.hours + 1 : 0;
          scope.updateTimestamp();
        };

        scope.decreaseHours = function () {
          scope.hours = scope.hours > 0 ? scope.hours - 1 : 23;
          scope.updateTimestamp();
        };

        scope.increaseMinutes = function () {
          scope.minutes = scope.minutes < 59 ? scope.minutes + 1 : scope.minutes = 0;
          scope.updateTimestamp();
        };

        scope.decreaseMinutes = function () {
          scope.minutes = scope.minutes > 0 ? scope.minutes - 1 : 59;
          scope.updateTimestamp();
        };

        scope.increaseSeconds = function () {
          scope.seconds = scope.seconds < 59 ? scope.seconds + 1 : scope.seconds = 0;
          scope.updateTimestamp();
        };

        scope.decreaseSeconds = function () {
          scope.seconds = scope.seconds > 0 ? scope.seconds - 1 : 59;
          scope.updateTimestamp();
        };

        scope.displayDays = function () {
          return scope.days < 100 ? '0' + (scope.days < 10 ? '0' + scope.days : scope.days) : scope.days;
        };

        scope.displayHours = function () {
          return scope.hours < 10 ? '0' + scope.hours : scope.hours;
        };

        scope.displayMinutes = function () {
          return scope.minutes < 10 ? '0' + scope.minutes : scope.minutes;
        };

        scope.displaySeconds = function () {
          return scope.seconds < 10 ? '0' + scope.seconds : scope.seconds;
        };

        scope.updateTimestamp = function () {
          scope.timestamp =
            scope.days * 86400000 + // 24 * 60 * 60 * 1000
            scope.hours * 3600000 + // 60 * 60 * 1000
            scope.minutes * 60000 + // 60 * 1000 +
            scope.seconds * 1000
        };
      }
    }
  });
