'use strict';

angular.module('directives', [])

  .directive('onBlurChange', ['$parse', function ($parse) {
    return function (scope, element, attr) {
      var fn = $parse(attr['onBlurChange']);
      var hasChanged = false;
      element.on('change', function (event) {
        hasChanged = true;
      });

      element.on('blur', function (event) {
        if (hasChanged) {
          scope.$apply(function () {
            fn(scope, {$event: event});
          });
          hasChanged = false;
        }
      });
    };
  }])

  .directive('onEnterBlur', function () {
    return function (scope, element, attrs) {
      element.bind('keydown keypress', function (event) {
        if (event.which === 13) {
          element.blur();
          event.preventDefault();
        }
      });
    };
  })

  .directive('sortBy', function () {
    return {
      templateUrl: 'views/components/sort-by.html',
      restrict: 'E',
      transclude: true,
      replace: true,
      scope: {
        sortdir: '=',
        sortedby: '=',
        sortvalue: '@',
        onsort: '='
      },
      link: function (scope, element, attrs) {
        scope.sort = function () {
          if (scope.sortedby === scope.sortvalue) {
            scope.sortdir = scope.sortdir === 'ASC' ? 'DESC' : 'ASC';
          } else {
            scope.sortedby = scope.sortvalue;
            scope.sortdir = 'ASC';
          }
          scope.onsort(scope.sortedby, scope.sortdir);
        };
      }
    };
  })

  .directive('formAutofillFix', function () {
    return function (scope, elem, attrs) {
      // Fixes Chrome bug: https://groups.google.com/forum/#!topic/angular/6NlucSskQjY
      elem.prop('method', 'POST');

      // Fix autofill issues where Angular doesn't know about autofilled inputs
      if (attrs.ngSubmit) {
        setTimeout(function () {
          elem.unbind('submit').submit(function (e) {
            e.preventDefault();
            elem.find('input, textarea, select').trigger('input').trigger('change').trigger('keydown');
            scope.$apply(attrs.ngSubmit);
          });
        }, 0);
      }
    };
  })

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
  })
  .directive('onTagAdd', function () {
    return function (scope, element, attrs) {
      element.bind("keydown keypress", function (event) {
        if (event.which === 13 || event.which === 188) {
          scope.$apply(function () {
            scope.$eval(attrs.onTagAdd);
          });

          event.preventDefault();
        }
      });
    };
  })
  .directive('onTagRemove', function () {
    return function (scope, element, attrs) {
      element.bind("keydown keypress", function (event) {
        if (event.which === 8 && !this.value) {
          scope.$apply(function () {
            scope.$eval(attrs.onTagRemove);
          });

          event.preventDefault();
        }
      });
    };
  });
