'use strict';

angular.module('services.client', ['restangular'])

  .factory('BlackList', ['Restangular', function (Restangular) {
    return {
      all: function () {
        return Restangular.all('blacklists').getList();
      },
      page: function (query) {
        return Restangular.one('blacklists').get(query);
      },
      get: function (client_id) {
        return Restangular.one('blacklists', client_id).get();
      },
      add: function (client) {
        return Restangular.all('blacklists').post(client);
      },
      save: function (client) {
        return Restangular.one('blacklists', client.client_id).put(client);
      },
      delete: function (client) {
        return Restangular.one('blacklists', client.client_id).remove();
      }
    }
  }]);
