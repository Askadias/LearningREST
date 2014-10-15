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
      get: function (id) {
        return Restangular.one('velocity_config', id).get();
      },
      add: function (config) {
        return Restangular.all('velocity_config').post(config);
      },
      save: function (id, config) {
        return Restangular.one('velocity_config', id).put(config);
      },
      delete: function (id) {
        return Restangular.one('velocity_config', id).remove();
      }
    }
  }])

  .factory('Velocity', ['Restangular', function (Restangular) {
    return {
      metricsPage: function (startFrom) {
        return Restangular.all('velocity/metrics').getList(startFrom);
      },
      dataPage: function (startFrom) {
        return Restangular.all('velocity/data_list').getList(startFrom);
      },
      check: function (metrics) {
        return Restangular.all('velocity').post(metrics);
      }
    }
  }]);
