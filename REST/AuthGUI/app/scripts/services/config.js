'use strict';

angular.module('services.config', [])
  .constant('configuration', {
    authEndpoint: 'http://localhost:11080/AuthService'
  });
