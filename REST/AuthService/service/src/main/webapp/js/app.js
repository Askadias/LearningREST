'use strict';

var authServiceAdmin = angular.module('authServiceAdmin', [
    'ngStorage',
    'authServiceAdmin.controllers',
    'authServiceAdmin.services',
    'authServiceAdmin.directives',
    'restangular',
    'ngAnimate',
    'ui.router'
])
    .config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', 'RestangularProvider', 'OAuthProvider',
        function ($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, RestangularProvider, OAuthProvider) {

            /*$httpProvider.defaults.useXDomain = true;
             delete $httpProvider.defaults.headers.common['X-Requested-With'];*/
            $httpProvider.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode('username:password');

            RestangularProvider.setDefaultHeaders({
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            });
            //RestangularProvider.setDefaultHttpFields({'withCredentials': true});

            OAuthProvider.extendConfig({
                authenticationEndpoint: 'http://localhost:11080/AuthService/app/login/',
                authorizationEndpoint: 'http://localhost:11080/AuthService/service/oauth/authorize/',
                client_id: '53cab7ca3004c4a709c985c3',
                client_secret: 'secret',
                scope: 'readClients writeClients readTokens updateTokens'
            });

            RestangularProvider.setBaseUrl('service/rest/v1');

            var access = routingConfig.accessLevels;

            $urlRouterProvider.otherwise('/app/clients/');

            $stateProvider
                .state('login', {
                    url: '/app/login/?redirect_url',
                    templateUrl: 'partials/login.html',
                    controller: 'LoginCtrl',
                    data: {
                        access: access.public
                    }
                })
                .state('authorize', {
                    url: '/app/authorize/',
                    templateUrl: 'partials/authorization.html',
                    controller: 'AuthController',
                    data: {
                        access: access.user
                    }
                })
                .state('auth', {
                    abstract: true,
                    url: '/app/auth/',
                    templateUrl: 'partials/auth.html',
                    controller: 'AuthController',
                    data: {
                        access: access.user
                    }
                })
                .state('auth.success', {
                    url: '/app/auth/success/',
                    templateUrl: 'partials/auth/success.html',
                    controller: 'AuthController',
                    data: {
                        access: access.user
                    }
                })
                .state('auth.failure', {
                    url: '/app/auth/failure/',
                    templateUrl: 'partials/auth/failure.html',
                    controller: 'AuthController',
                    data: {
                        access: access.user
                    }
                })
                .state('clients', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'partials/layout.html',
                    controller: 'MainCtrl',
                    data: {
                        access: access.user
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
                        access: access.user
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
            /*$httpProvider.interceptors.push(function ($q, $location, $state) {
                return {
                    'responseError': function (response) {
                        if (response.status === 401 || response.status === 403) {
                            $location.path('/AuthService/app/login/?redirect_url=' + $location.url());
                        }
                        return $q.reject(response);
                    }
                };
            });*/
        }])
    .run(['$rootScope', '$state', 'Auth', function ($rootScope, $state, Auth) {
        $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {
            if (!Auth.authorize(toState.data.access)) {
                $rootScope.alerts.push({
                    type: 'danger',
                    msg: "Seems like you tried accessing a route you don't have access to..."
                });

                event.preventDefault();

                if (fromState.url === '^') {
                    if (Auth.isLoggedIn()) {
                        $rootScope.alerts = [];
                        $state.go('clients.list');
                    } else {
                        $state.go('login');
                    }
                }
            } else {
                $rootScope.alerts = [];
            }
        });

    }])
    .run(['$rootScope', '$window', 'OAuth',
        function ($rootScope, $window, OAuth) {
            //$rootScope.session = sessionService;
            $window.app = {
                authState: function (state, user) {
                    $rootScope.$apply(function () {
                        switch (state) {
                            case 'success':
                                OAuth.verifyAsync(user);
                                break;
                            case 'failure':
                                //OAuth.authFailed();
                                alert('Authentication failed');
                                break;
                        }

                    });
                }
            };

            /*if ($window.user !== null) {
             alert('Authentication succeeded');
             //sessionService.authSuccess($window.user);
             }*/
        }]);