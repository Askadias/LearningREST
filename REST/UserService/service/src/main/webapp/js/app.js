'use strict';

var userServiceAdmin = angular.module('userServiceAdmin', [
    'ngRoute',
    'userServiceAdmin.controllers',
    'userServiceAdmin.services',
    'userServiceAdmin.directives',
    'restangular',
    'ngAnimate',
    'route-segment',
    'view-segment'
])
    .constant('ROLES', {
        admin: 'admin',
        reader: 'reader',
        writer: 'writer',
        guest: 'guest'
    })
    .constant('AUTH_EVENTS', {
        loginSuccess: 'auth-login-success',
        loginFailed: 'auth-login-failed',
        logoutSuccess: 'auth-logout-success',
        sessionTimeout: 'auth-session-timeout',
        notAuthenticated: 'auth-not-authenticated',
        notAuthorized: 'auth-not-authorized'
    })
    .config(['$routeProvider', '$routeSegmentProvider', 'RestangularProvider', 'ROLES',
        function ($routeProvider, $routeSegmentProvider, RestangularProvider, ROLES) {

            $routeSegmentProvider.options.autoLoadTemplates = true;

            RestangularProvider.setBaseUrl('service/rest/v1/');

            $routeSegmentProvider
                .when('/users', 'users')
                .when('/users/list', 'users.list')
                .when('/users/:email/:mode', 'users.details')

                .when('/clients', 'clients')
                .when('/clients/list', 'clients.list')
                .when('/clients/:clientID/:mode', 'clients.details')

                .segment('users', {
                    templateUrl: 'partials/users.html',
                    controller: 'MainCtrl'
                })
                .within()
                .segment('list', {
                    default: true,
                    templateUrl: 'partials/users/list.html',
                    controller: 'UsersListCtrl'
                })
                .segment('details', {
                    templateUrl: 'partials/users/details.html',
                    controller: 'UserDetailsCtrl',
                    dependencies: ['email', 'mode']
                })
                .up()

                .segment('clients', {
                    templateUrl: 'partials/clients.html',
                    controller: 'MainCtrl'
                })
                .within()
                .segment('list', {
                    default: true,
                    templateUrl: 'partials/clients/list.html',
                    controller: 'ClientsListCtrl'
                })
                .segment('details', {
                    templateUrl: 'partials/clients/details.html',
                    controller: 'ClientDetailsCtrl',
                    dependencies: ['clientID', 'mode']
                });
            $routeProvider.otherwise({redirectTo: '/users'});
        }])
    .run(function ($rootScope, AUTH_EVENTS, Auth) {
        $rootScope.$on('$routeChangeStart', function (event, next, $routeSegment) {
            var authorizedRoles = next.data.authorizedRoles;
            if (!Auth.isAuthorized(authorizedRoles)) {
                event.preventDefault();
                if (Auth.isAuthenticated()) {
                    // user is not allowed
                    $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
                } else {
                    // user is not logged in
                    $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
                }
            }
        });
    });