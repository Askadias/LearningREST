'use strict';

angular.module('authServiceAdmin.services', ['restangular'])
    .factory('Token', ['Restangular', function (Restangular) {
        return {
            all: function () {
                return Restangular.all('auths').getList();
            },
            page: {
                search: function (query) {
                    return Restangular.one('auths').get(query);
                }
            },
            get: function (email) {
                return Restangular.one('auths', email).get();
            },
            add: function (auth) {
                return Restangular.one('auths', auth).put();
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
        this.create = function (sessionId, auth) {
            this.id = sessionId;
            this.auth = auth;
        };
        this.destroy = function () {
            this.id = null;
            this.auth = null;
        };
        return this;
    }])
    .factory('Token', ['Restangular', 'Session', '$cookieStore', function (Restangular, Session, $cookieStore) {
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
                return !!Session.auth;
            },
            isAuthorized: function (authorizedRoles) {
                if (!authorizedRoles) return true;
                if (!Session.auth || (!!authorizedRoles && !Session.auth.roles)) return false;
                for (var role in Session.auth.roles) {
                    if (authorizedRoles.indexOf(role) > -1) return true;
                }
            }
        }
    }]);

/*
 .factory('Token', ['$resource',
 function ($resource) {
 return $resource('service/rest/v1/auths/:email', {}, {
 getList: {method: 'GET', isArray: true},
 get: {method: 'GET'},
 save: {method: 'POST'},
 add: {method: 'PUT'},
 delete: {method: 'DELETE'}
 });
 }]);*/
