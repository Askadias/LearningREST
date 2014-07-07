'use strict';

var userService = angular.module('userService', ['ngResource']);

userService.factory('UserService', ['$resource',
    function ($resource) {
        return $resource('service/rest/v1/users/:userEmail', {}, {
            getUsers: {method: 'GET', isArray: true},
            addUser: {method: 'PUT'}
        });
    }]);