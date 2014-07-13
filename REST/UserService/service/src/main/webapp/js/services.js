"use strict";

var userService = angular.module("userService", ['restangular']);


userService.factory("User", ["Restangular",
    function (Restangular) {
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
    }]);

/*
 userService.factory("User", ["$resource",
 function ($resource) {
 return $resource("service/rest/v1/users/:email", {}, {
 getList: {method: 'GET', isArray: true},
 get: {method: 'GET'},
 save: {method: 'POST'},
 add: {method: 'PUT'},
 delete: {method: 'DELETE'}
 });
 }]);*/
