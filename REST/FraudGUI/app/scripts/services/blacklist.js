'use strict';

angular.module('services.blacklist', ['restangular'])

  .factory('BlackList', ['Restangular', function (Restangular) {
    return {
      page: function (startFrom) {
        return Restangular.all('blacklists').getList(startFrom);
      },
      get: function (type, value) {
        return Restangular.one('blacklist', type, value).get();
      },
      save: function (item) {
        return Restangular.all('blacklist').post(item);
      },
      delete: function (item) {
        return Restangular.one('blacklist', item.type, item.value).remove();
      }
    }
  }]);
