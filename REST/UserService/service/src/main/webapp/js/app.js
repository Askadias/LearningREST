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
    .config(['$routeProvider', '$routeSegmentProvider', 'RestangularProvider',
        function ($routeProvider, $routeSegmentProvider, RestangularProvider) {

            $routeSegmentProvider.options.autoLoadTemplates = true;

            RestangularProvider.setBaseUrl('service/rest/v1/');

            $routeSegmentProvider
                .when('/users', 'users')
                .when('/users/list', 'users.list')
                .when('/users/:email', 'users.details')

                .when('/clients', 'clients')
                .when('/clients/list', 'clients.list')
                .when('/clients/:clientID', 'clients.details')

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
                    dependencies: ['email']
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
                    dependencies: ['clientID']
                });
            $routeProvider.otherwise({redirectTo: '/users'});
        }]);