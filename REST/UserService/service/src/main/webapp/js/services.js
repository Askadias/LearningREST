'use strict';

angular.module('userServiceAdmin.services', ['restangular'])
    .factory('User', ['Restangular', function (Restangular) {
        return {
            all: function () {
                return Restangular.all('users').getList();
            },
            page: {
                search: function (query) {
                    return Restangular.one('users').get(query);
                }
            },
            get: function (email) {
                return Restangular.one('users', email).get();
            },
            add: function (user) {
                return Restangular.one('users', user).put();
            }
        }
    }])
    .factory('Client', ['Restangular', function (Restangular) {
        return {
            all: function () {
                return Restangular.all('clients').getList();
            },
            page: {
                search: function (query) {
                    return Restangular.one('clients').get(query);
                }
            },
            get: function (clientID) {
                return Restangular.one('clients', clientID).get();
            },
            add: function (client) {
                return Restangular.all('clients').post(client);
            },
            save: function (client) {
                return Restangular.one('clients', client.clientID).put(client);
            },
            delete: function (client) {
                return Restangular.one('clients', client.clientID).remove();
            }
        }
    }])
    .service('Session', [function () {
        this.create = function (sessionId, user) {
            this.id = sessionId;
            this.user = user;
        };
        this.destroy = function () {
            this.id = null;
            this.user = null;
        };
        return this;
    }])
    .factory('Auth', ['Restangular', 'Session', '$cookieStore', function (Restangular, Session, $cookieStore) {
        return {
            login: function (credentials) {
                return Restangular
                    .withConfig(function (RestangularConfigurer) {
                        RestangularConfigurer.setBaseUrl('service/auth');
                    })
                    .all('login').post(credentials);
            },
            register: function (credentials) {
                return Restangular
                    .withConfig(function (RestangularConfigurer) {
                        RestangularConfigurer.setBaseUrl('service/auth');
                    })
                    .all('register').post(credentials);
            },
            logout: function () {
                Session.destroy();
            },
            isAuthenticated: function () {
                return !!Session.user;
            },
            isAuthorized: function (authorizedRoles) {
                if (!authorizedRoles) return true;
                if (!Session.user || (!!authorizedRoles && !Session.user.roles)) return false;
                for (var role in Session.user.roles) {
                    if (authorizedRoles.indexOf(role) > -1) return true;
                }
            }
        }
    }]);

/*
 .factory('User', ['$resource',
 function ($resource) {
 return $resource('service/rest/v1/users/:email', {}, {
 getList: {method: 'GET', isArray: true},
 get: {method: 'GET'},
 save: {method: 'POST'},
 add: {method: 'PUT'},
 delete: {method: 'DELETE'}
 });
 }]);*/
