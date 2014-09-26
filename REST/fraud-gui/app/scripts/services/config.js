'use strict';

angular.module('services.config', [])
  .constant('config', {
    authEndpoint: 'http://localhost:11080/auth',
    fraudEndpoint: 'http://localhost:12080/fraud/rest/v1'
  });
