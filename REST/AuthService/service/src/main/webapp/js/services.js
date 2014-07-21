'use strict';

angular.module('authServiceAdmin.services', ['restangular'])
    .factory('Token', ['Restangular', function (Restangular) {
        return {
            all: function () {
                return Restangular.all('tokens').getList();
            },
            page: {
                search: function (query) {
                    return Restangular.one('tokens').get(query);
                }
            },
            get: function (tokenKey) {
                return Restangular.one('tokens', tokenKey).get();
            },
            add: function (token) {
                return Restangular.all('tokens').post(token);
            },
            save: function (token) {
                return Restangular.one('tokens', token.tokenKey).put(token);
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
    .provider('OAuth', function () {

        var MUST_OVERRIDE = {};

        var config = {
            authorizationEndpoint: MUST_OVERRIDE,
            client_id: MUST_OVERRIDE,
            client_secret: MUST_OVERRIDE,
            audience: '',
            redirect_uri: MUST_OVERRIDE,
            scope: MUST_OVERRIDE,
            state: '',
            token_type: 'code',
            verifyFunc: MUST_OVERRIDE
        };

        this.extendConfig = function (configExtension) {
            config = angular.extend(config, configExtension);
        };

        var objectToQueryString = function (obj) {
            var str = [];
            angular.forEach(obj, function (value, key) {
                str.push(encodeURIComponent(key) + "=" + encodeURIComponent(value));
            });
            return str.join("&");
        };

        this.$get = function ($q, $http, $window, $rootScope, Restangular, $sessionStorage, $location) {

            Restangular = Restangular.withConfig(function (RestangularConfigurer) {
                RestangularConfigurer.setBaseUrl('service/oauth/');
            });

            var accessCode = $sessionStorage.accessCode;

            return {
                verifyAsync: function (accessToken) {
                    return config.verifyFunc(config, accessToken);
                },
                getTokenByPopup: function (extraParams, popupOptions) {
                    popupOptions = angular.extend({
                        name: 'AuthPopup',
                        openParams: {
                            width: 650,
                            height: 300,
                            resizable: true,
                            scrollbars: true,
                            status: true
                        }
                    }, popupOptions);

                    var deferred = $q.defer(),
                        params = angular.extend(angular.extend(config, {redirect_url: $location.url()}, extraParams)),
                        url = config.authorizationEndpoint + '?' + objectToQueryString(params),
                        resolved = false;

                    var formatPopupOptions = function (options) {
                        var pairs = [];
                        angular.forEach(options, function (value, key) {
                            if (value || value === 0) {
                                value = value === true ? 'yes' : value;
                                pairs.push(key + '=' + value);
                            }
                        });
                        return pairs.join(',');
                    };

                    var popup = window.open(url, popupOptions.name, formatPopupOptions(popupOptions.openParams));

                    // TODO: binding occurs for each reauthentication, leading to leaks for long-running apps.

                    angular.element($window).bind('message', function (event) {
                        // Use JQuery originalEvent if present
                        event = event.originalEvent || event;
                        if (event.source == popup && event.origin == window.location.origin) {
                            $rootScope.$apply(function () {
                                if (event.data.access_token) {
                                    deferred.resolve(event.data)
                                } else {
                                    deferred.reject(event.data)
                                }
                            })
                        }
                    });

                    // TODO: reject deferred if the popup was closed without a message being delivered + maybe offer a timeout

                    return deferred.promise;
                },
                authorize: function (authorizationData, success, error) {
                    Restangular.one('authorize').customPOST(
                        config,
                        'token',
                        {},
                        {
                            Authorization: 'Basic ' + Base64.encode(
                                'askadais@gmail.com' + ':' +
                                'ab518f2267abc8bac15c1af41fe0472a62bf36de27ebca8e62b5fad23e6edd6caec6058cd6e8a186'),
                            ContentType: 'application/x-www-form-urlencoded'
                        }
                    ).then(function (response) {
                            accessCode = response;
                            $sessionStorage.accessCode = accessCode;
                            success(response);
                        }, function (response) {
                            error(response);
                        });
                }
            }
        }
    })
    .factory('OAuthInterceptor', ['$rootScope', '$q', '$sessionStorage', '$location',
        function ($rootScope, $q, $sessionStorage, $location) {

            var service = {};

            service.request = function (config) {
                var token = $sessionStorage.token;
                var user = $sessionStorage.user;
                if (user === undefined) {
                    //window.location.replace('http://localhost:10080/UserService/app/login/?redirect_url=' + $location.absUrl());
                    /*OAuth.getTokenByPopup().then(function (params) {
                     Auth.verifyAsync(params.access_token).then(function (data) {
                     $sessionStorage.token = params.access_token;
                     $sessionStorage.expires_in = params.expires_in;
                     })
                     })*/
                }

                if (token)
                    config.headers.Authorization = 'Bearer ' + token.access_token;

                if (token && expired(token))
                    $rootScope.$broadcast('oauth:expired', token);

                return config;
            };

            var expired = function (token) {
                return (token && token.expires_at && new Date(token.expires_at) < new Date())
            };

            return service;
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
