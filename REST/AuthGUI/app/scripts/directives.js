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
  });
