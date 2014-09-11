'use strict';

angular.module('services.velocity', ['restangular'])

  .factory('VelocityConfig', ['Restangular', function (Restangular) {
    return {
      all: function () {
        return Restangular.all('velocity_config').getList();
      },
      page: function (query) {
        return Restangular.one('velocity_config').get(query);
      },
      get: function (code) {
        return Restangular.one('velocity_config', code).get();
      },
      add: function (config) {
        return Restangular.all('velocity_config').post(config);
      },
      save: function (config) {
        return Restangular.one('velocity_config').put(config);
      },
      delete: function (config) {
        return Restangular.one('velocity_config', config.metric_type).remove();
      }
    }
  }]);
