'use strict';

var authServiceAdmin = angular.module('authServiceAdmin', [
    'ngStorage',
    'authServiceAdmin.controllers',
    'authServiceAdmin.services',
    'authServiceAdmin.directives',
    'restangular',
    'ngAnimate',
    'ui.router',
    'base64'
])
    .config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', 'RestangularProvider', 'OAuthProvider',
        function ($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, RestangularProvider, OAuthProvider) {

            OAuthProvider.extendConfig({
                authorizationEndpoint: 'http://localhost:10080/UserService/app/login/',
                client_id: '53cab7ca3004c4a709c985c3',
                client_secret: 'secret',
                scope: 'readClients writeClients readTokens updateTokens'
            });

            RestangularProvider.setBaseUrl('service/rest/v1');

            var access = routingConfig.accessLevels;

            $urlRouterProvider.otherwise('/app/clients/');

            $stateProvider
                .state('clients', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'partials/layout.html',
                    controller: 'MainCtrl',
                    data: {
                        access: access.public
                    }
                })
                .state('clients.list', {
                    url: '/clients/',
                    templateUrl: 'partials/clients/list.html',
                    controller: 'ClientsListCtrl',
                    data: {
                        access: access.user
                    }
                })
                .state('clients.details', {
                    url: '/clients/:clientID/:mode/',
                    templateUrl: 'partials/clients/details.html',
                    controller: 'ClientDetailsCtrl',
                    data: {
                        access: access.admin
                    }
                })
                .state('tokens', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'partials/layout.html',
                    controller: 'MainCtrl',
                    data: {
                        access: access.public
                    }
                })
                .state('tokens.list', {
                    url: '/tokens/',
                    templateUrl: 'partials/tokens/list.html',
                    controller: 'TokensListCtrl',
                    data: {
                        access: access.user
                    }
                })
                .state('tokens.details', {
                    url: '/tokens/:clientID/:mode/',
                    templateUrl: 'partials/tokens/details.html',
                    controller: 'TokenDetailsCtrl',
                    data: {
                        access: access.admin
                    }
                });

            // FIX for trailing slashes. Gracefully "borrowed" from https://github.com/angular-ui/ui-router/issues/50
            $urlRouterProvider.rule(function ($injector, $location) {
                if ($location.protocol() === 'file')
                    return;

                var path = $location.path()
                // Note: misnomer. This returns a query object, not a search string
                    , search = $location.search(), params
                    ;

                // check to see if the path already ends in '/'
                if (path[path.length - 1] === '/') {
                    return;
                }

                // If there was no search string / query params, return with a `/`
                if (Object.keys(search).length === 0) {
                    return path + '/';
                }

                // Otherwise build the search string and return a `/?` prefix
                params = [];
                angular.forEach(search, function (v, k) {
                    params.push(k + '=' + v);
                });
                return path + '/?' + params.join('&');
            });

            $locationProvider.html5Mode(true);

            $httpProvider.interceptors.push(function ($q, $location) {
                return {
                    'responseError': function (response) {
                        if (response.status === 401 || response.status === 403) {
                            //$location.url('http://localhost:10080/UserService/app/login/?redirect_url=' + $location.url());
                            return $q.reject(response);
                        }
                        else {
                            return $q.reject(response);
                        }
                    }
                }
            });
            $httpProvider.interceptors.push('OAuthInterceptor');
        }])
    .run(['$rootScope', '$state', 'OAuth', '$location', function ($rootScope, $state, OAuth, $location) {
        $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {
            /*if (toParams) {
             window.location.replace('http://localhost:10080/UserService/app/login/?redirect_url=' + $location.url());
             }*/
            /*if (!Auth.authorize(toState.data.access)) {
             $rootScope.error = "Seems like you tried accessing a route you don't have access to...";
             event.preventDefault();

             if (fromState.url === '^') {
             if (Auth.isLoggedIn())
             $state.go('clients.list');
             else {
             $rootScope.error = null;
             window.location.replace('http://localhost:10080/UserService/app/login/?redirect_url=' + $location.url());
             }
             }
             }*/
        });
    }]);