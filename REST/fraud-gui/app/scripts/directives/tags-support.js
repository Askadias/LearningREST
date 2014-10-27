'use strict';

angular.module('directives.tags-support', [])

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
