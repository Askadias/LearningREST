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
    .factory('Auth', ['Restangular', '$sessionStorage', function (Restangular, $sessionStorage) {

        var accessLevels = routingConfig.accessLevels,
            userRoles = routingConfig.userRoles,
            currentUser = $sessionStorage.user || {
                    firstName: 'Guest',
                    roles: ['public'],
                    isLoggedIn: false
                };

        Restangular = Restangular.withConfig(function (RestangularConfigurer) {
            RestangularConfigurer.setBaseUrl('service/auth');
        });

        function changeUser(user) {
            angular.extend(currentUser, user);
            $sessionStorage.user = currentUser;
        }

        return {
            authorize: function (accessLevel, role) {
                var isAuthorized = false;
                if (role === undefined && !!currentUser.roles) {
                    for (var userRole in currentUser.roles) {
                        isAuthorized |= this.authorize(accessLevel, currentUser.roles[userRole])
                    }
                }
                else {
                    isAuthorized = accessLevel.bitMask & userRoles[role].bitMask;
                }
                return isAuthorized;
            },
            register: function (credentials, success, error) {
                return Restangular.all('register').post(credentials).then(function (response) {
                    user.isLoggedIn = true;
                    changeUser(response);
                    success();
                }, function (response) {
                    error(response);
                });
            },
            login: function (credentials, success, error) {
                return Restangular.all('login').post(credentials).then(function (user) {
                    user.isLoggedIn = true;
                    changeUser(user);
                    success();
                }, function (response) {
                    error(response);
                });
            },
            logout: function () {
                changeUser({
                    firstName: 'Guest',
                    roles: ['public'],
                    isLoggedIn: false
                })
            },
            isLoggedIn: function (user) {
                if (user === undefined) {
                    user = currentUser;
                }
                return user.isLoggedIn === true;
            },
            accessLevels: accessLevels,
            userRoles: userRoles,
            user: currentUser
        }
    }]);