"use strict";

angular.module("userServiceAdmin.services", ['restangular'])
    .factory("User", ["Restangular", function (Restangular) {
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
    .factory("Client", ["Restangular", function (Restangular) {
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
            }
        }
    }]);

/*
 .factory("User", ["$resource",
 function ($resource) {
 return $resource("service/rest/v1/users/:email", {}, {
 getList: {method: 'GET', isArray: true},
 get: {method: 'GET'},
 save: {method: 'POST'},
 add: {method: 'PUT'},
 delete: {method: 'DELETE'}
 });
 }]);*/
