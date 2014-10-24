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
  })
  .directive('datePagination', function () {
    return {
      restrict: 'E',
      templateUrl: 'views/components/date-pagination.html',
      scope: {
        //dpDate: '=',
        startDate: '=',
        endDate: '=',
        currentDate: '=',
        onRangeChange: '&onRangeChange',
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
          var cur = moment($scope.currentDate).subtract(8, 'd')._d;
          for (var i = 0; i < 17; i++) {
            $scope.days.push(moment(cur).add(i, 'd')._d);
          }
          $scope.onDateChange()
        };


        /*scope.$watch(scope.dpDate, function (value) {
         scope.currentDate = value
         });*/

        /*scope.$watch(scope.currentDate, function (value) {
         scope.updateDates();
         });*/

        $scope.$watch(function () {
          return $scope.currentDate;
        }, function () {
          $scope.updateDates();
        }, true);

        /*scope.$watch(scope.startDate, function (value) {
         if (scope.endDate) {
         scope.makeRange(value, scope.endDate);
         }
         });
         scope.$watch(scope.endDate, function (value) {
         if (scope.startDate) {
         scope.makeRange(scope.startDate, value);
         }
         });*/

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
          //return day == scope.currentDate
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
  })
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
