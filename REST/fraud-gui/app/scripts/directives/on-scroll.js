'use strict';

angular.module('directives.on-scroll', [])

  .directive('onScroll', function () {
    return {
      link: function (scope, element, attrs) {
        var offset = parseInt(attrs.threshold) || 0;
        var e = element[0];

        element.bind('scroll', function () {
          if (e.scrollTop + e.offsetHeight >= e.scrollHeight - offset) {
            scope.$apply(attrs.onScroll);
          }
        });
      }
    };
  });
