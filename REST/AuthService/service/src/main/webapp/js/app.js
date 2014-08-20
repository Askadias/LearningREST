'use strict';

var authServiceAdmin = angular.module('authServiceAdmin', [
    'ngStorage',
    'authServiceAdmin.controllers.common',
    'authServiceAdmin.controllers.user',
    'authServiceAdmin.controllers.client',
    'authServiceAdmin.controllers.group',
    'authServiceAdmin.controllers.token',
    'authServiceAdmin.services',
    'authServiceAdmin.directives',
    'restangular',
    'ngAnimate',
    'ui.router'
])
    .config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', 'RestangularProvider', 'OAuthProvider',
        function ($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, RestangularProvider, OAuthProvider) {

            $httpProvider.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode('username:password');

            RestangularProvider.setDefaultHeaders({
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            });

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
                .state('users', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'partials/layout.html',
                    controller: 'MainCtrl',
                    data: {
                        access: access.public
                    }
                })
                .state('users.list', {
                    url: '/users/',
                    templateUrl: 'partials/users/list.html',
                    controller: 'UsersListCtrl',
                    data: {
                        access: access.user
                    }
                })
                .state('users.details', {
                    url: '/users/:login/:mode/',
                    templateUrl: 'partials/users/details.html',
                    controller: 'UserDetailsCtrl',
                    data: {
                        access: access.admin
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
                    url: '/clients/:client_id/:mode/',
                    templateUrl: 'partials/clients/details.html',
                    controller: 'ClientDetailsCtrl',
                    data: {
                        access: access.admin
                    }
                })
                .state('groups', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'partials/layout.html',
                    controller: 'MainCtrl',
                    data: {
                        access: access.user
                    }
                })
                .state('groups.list', {
                    url: '/groups/',
                    templateUrl: 'partials/groups/list.html',
                    controller: 'GroupsListCtrl',
                    data: {
                        access: access.user
                    }
                })
                .state('groups.details', {
                    url: '/groups/:code/:mode/',
                    templateUrl: 'partials/groups/details.html',
                    controller: 'GroupDetailsCtrl',
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
                    url: '/tokens/:token_key/:mode/',
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

                // Note: misnomer. This returns a query object, not a search string
                var path = $location.path(),
                    search = $location.search(),
                    params = [];

                // check to see if the path already ends in '/'
                if (path[path.length - 1] === '/') {
                    return;
                }

                // If there was no search string / query params, return with a `/`
                if (Object.keys(search).length === 0) {
                    return path + '/';
                }

                // Otherwise build the search string and return a `/?` prefix
                angular.forEach(search, function (v, k) {
                    params.push(k + '=' + v);
                });
                return path + '/?' + params.join('&');
            });

            $locationProvider.html5Mode(true);

            $httpProvider.interceptors.push('OAuthInterceptor');
            $httpProvider.interceptors.push(['$location', '$q', '$injector', function ($location, $q, $injector) {
                function success(response) {
                    return response;
                }

                function error(response) {
                    if (response.status === 401 || response.status === 403) {
                        $injector.get('$state').transitionTo('login');
                        return $q.reject(response);
                    }
                    else {
                        return $q.reject(response);
                    }
                }

                return function (promise) {
                    return promise.then(success, error);
                }
            }]);
        }])
    .run(['$rootScope', '$state', 'Auth', '$location', 'AlertMgr',
        function ($rootScope, $state, Auth, $location, AlertMgr) {
            $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {
                if (!Auth.authorize(toState.data.access)) {
                    AlertMgr.addAlert('danger', "Seems like you tried accessing a route you don`t have access to...");

                    event.preventDefault();

                    if (fromState.url === '^') {
                        if (Auth.isLoggedIn()) {
                            $rootScope.alerts = [];
                            $state.go('clients.list');
                        } else {
                            $state.transitionTo('login', {redirect_url: $location.url()});
                        }
                    }
                } else {
                    AlertMgr.clearAlerts();
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
                                $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
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
