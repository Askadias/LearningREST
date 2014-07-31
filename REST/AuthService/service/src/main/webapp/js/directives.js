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
    })
    .directive('autoFillSync', function ($timeout) {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ngModel) {
                var origVal = elem.val();
                $timeout(function () {
                    var newVal = elem.val();
                    if (ngModel.$pristine && origVal !== newVal) {
                        ngModel.$setViewValue(newVal);
                    }
                }, 100);
            }
        }
    })
    .directive('shakeThat', ['$animate', function ($animate) {
        return {
            require: '^form',
            scope: {
                submit: '&',
                submitted: '='
            },
            link: function (scope, element, attrs, form) {
                // listen on submit event
                element.on('submit', function () {
                    // tell angular to update scope
                    scope.$apply(function () {
                        // everything ok -> call submit fn from controller
                        if (form.$valid) return scope.submit();
                        // show error messages on submit
                        scope.submitted = true;
                        // shake that form
                        $animate.addClass(element, 'shake', function () {
                            $animate.removeClass(element, 'shake');
                        });
                    });
                });
            }
        };
    }]);