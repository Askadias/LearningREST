'use strict';

angular.module('services.config', [])
  .constant('config', {
    authEndpoint: 'http://localhost:11080/AuthService',
    fraudEndpoint: 'http://localhost:12080/FraudService/rest/v1'
  });
