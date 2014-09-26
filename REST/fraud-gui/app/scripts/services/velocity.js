'use strict';

angular.module('services.velocity', ['restangular'])

  .factory('VelocityConfig', ['Restangular', function (Restangular) {
    return {
      all: function () {
        return Restangular.all('configs').getList();
      },
      page: function (query) {
        return Restangular.one('configs').get(query);
      },
      get: function (code) {
        return Restangular.one('configs', code).get();
      },
      add: function (config) {
        return Restangular.all('configs').post(config);
      },
      save: function (config) {
        return Restangular.one('configs').put(config);
      },
      delete: function (config) {
        return Restangular.one('configs', config.metric_type).remove();
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
