'use strict';

angular.module('authServiceAdmin.directives', [])
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
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    element.blur();
                    event.preventDefault();
                }
            });
        };
    })
    .directive('sortBy', function () {
        return {
            templateUrl: 'components/sort-by.html',
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
                    if (scope.sortedby == scope.sortvalue)
                        scope.sortdir = scope.sortdir == 'ASC' ? 'DESC' : 'ASC';
                    else {
                        scope.sortedby = scope.sortvalue;
                        scope.sortdir = 'ASC';
                    }
                    scope.onsort(scope.sortedby, scope.sortdir);
                }
            }
        };
    })
    .directive('input', function () {
        return {
            priority: 0,
            require: '?ngModel',
            restrict: 'E',
            link: function ($scope, $element, $attrs, ngModelController) {
                var inputType = angular.lowercase($attrs.type);

                if (!ngModelController || inputType === 'radio' ||
                    inputType === 'checkbox') {
                    return;
                }

                ngModelController.$parsers.push(function (value) {
                    if ((ngModelController.$invalid && angular.isUndefined(value)) || value === '') {
                        return null;
                    } else {
                        return value;
                    }
                });
            }
        };
    });