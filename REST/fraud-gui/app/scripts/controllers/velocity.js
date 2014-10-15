'use strict';

angular.module('controllers.velocity', [])

  .controller('VelocityConfigsListCtrl', ['$scope', '$modal', '$stateParams', 'VelocityConfig',
    function ($scope, $modal, $stateParams, VelocityConfig) {
      $scope.totalPages = 0;
      $scope.configsCount = 0;
      $scope.headers = [
        {
          title: 'ID',
          value: 'id'
        },
        {
          title: 'Primary Metrics',
          value: 'primary_metrics'
        },
        {
          title: 'Period',
          value: 'period'
        },
        {
          title: 'Expires In',
          value: 'expires_in'
        },
        {
          title: 'Updated By',
          value: 'updated_by'
        },
        {
          title: 'Created By',
          value: 'created_by'
        },
        {
          title: 'Update Date',
          value: 'update_date'
        },
        {
          title: 'Create Date',
          value: 'create_date'
        }
      ];

      $scope.filterCriteria = {
        page: 1,
        sort_dir: 'ASC',
        sorted_by: 'name'
      };

      //The function that is responsible of fetching the result from the server and setting the grid to the new result
      $scope.fetchResult = function () {
        return VelocityConfig.page($scope.filterCriteria).then(function (response) {
          $scope.configs = response.content;
          $scope.totalPages = Math.ceil(response.total / response.size);
          $scope.configsCount = response.total;
        }, function () {
          $scope.configs = [];
          $scope.totalPages = 0;
          $scope.configsCount = 0;
        });
      };

      //called when navigate to another page in the pagination
      $scope.selectPage = function (page) {
        $scope.filterCriteria.page = page;
        $scope.fetchResult();
      };

      //Will be called when filtering the grid, will reset the page number to one
      $scope.filterResult = function () {
        $scope.filterCriteria.page = 1;
        $scope.fetchResult().then(function () {
          //The request fires correctly but sometimes the ui doesn't update, that's a fix
          $scope.filterCriteria.page = 1;
        });
      };

      //call back function that we passed to our custom directive sortBy, will be called when clicking on any field to sort
      $scope.onSort = function (sortedBy, sortDir) {
        $scope.filterCriteria.sort_dir = sortDir;
        $scope.filterCriteria.sorted_by = sortedBy;
        $scope.filterCriteria.page = 1;
        $scope.fetchResult().then(function () {
          //The request fires correctly but sometimes the ui doesn't update, that's a fix
          $scope.filterCriteria.page = 1;
        });
      };
      $scope.remove = function (velocity_config) {
        VelocityConfig.delete(velocity_config.id).then(function (response) {
          $scope.fetchResult();
        }, function (response) {
        });
      };

      $scope.expiresIn = function (ttl, create_date_str) {
        var create_date_millis = new Date(create_date_str).getTime();
        return Math.floor(create_date_millis + ttl - new Date().getTime());
      };

      //manually select a page to trigger an ajax request to populate the grid on page load
      $scope.selectPage(1);
    }])

  .controller('VelocityConfigDetailsCtrl', ['$scope', '$state', '$stateParams', 'VelocityConfig',
    function ($scope, $state, $stateParams, VelocityConfig) {
      $scope.mode = $stateParams.mode;
      $scope.metrics = [];
      $scope.metricsAutocomplete = [];

      $scope.velocity_config = {
        primary_metrics: [],
        period: 2592000000,       // 30 days in millis
        expires_in: 7776000000, // 90 days in seconds
        aggregation_configs: []
      };
      $scope.original = angular.copy($scope.velocity_config);

      if ($scope.mode === 'edit') {
        VelocityConfig.get($stateParams.config_id).then(function (response) {
          if (response) {
            $scope.velocity_config = response;
            $scope.velocity_config.aggregation_configs = $scope.velocity_config.aggregation_configs || {};
            $scope.original = angular.copy($scope.velocity_config);
          }
        }, function () {
        });
      }

      VelocityConfig.all().then(function (response) {
        var configs = response;
        for(var i = 0; i < configs.length; i++) {
          for (var j = 0; j <  configs[i].primary_metrics.length; j++) {
            var metric = configs[i].primary_metrics[j];
            if (!($scope.metrics.indexOf(metric) > -1)) {
              $scope.metrics.push(metric);
              $scope.metricsAutocomplete.push({name: metric})
            }
          }
          for (var j = 0; j <  configs[i].aggregation_configs.length; j++) {
            var metric = configs[i].aggregation_configs[j].secondary_metric;
            if (!($scope.metrics.indexOf(metric) > -1)) {
              $scope.metrics.push(metric);
              $scope.metricsAutocomplete.push({name: metric})
            }
          }
        }
      }, function () {
      });

      $scope.discard = function () {
        $scope.velocity_config = angular.copy($scope.original);
      };

      $scope.save = function () {
        $scope.original = angular.copy($scope.velocity_config);
        if ($scope.mode === 'new') {
          VelocityConfig.add($scope.velocity_config).then(function (response) {
            $state.go('velocity.config.list');
          }, function (error) {
            error.data.messages.forEach(function (item) {
            });
          });
        } else if ($scope.mode === 'edit') {
          $scope.velocity_config.save();
        }
      };

      $scope.removeMetric = function (metric) {
        delete $scope.velocity_config.aggregation_configs[metric];
      };


      $scope.isCancelDisabled = function () {
        return angular.equals($scope.original, $scope.velocity_config);
      };

      $scope.isSaveDisabled = function () {
        return $scope.velocity_config_form.$invalid || angular.equals($scope.original, $scope.velocity_config);
      };
      $scope.addNewMetric = function() {

      }
    }])

  .controller('VelocityMetricsCtrl', ['$scope', '$modal', '$stateParams', 'Velocity',
    function ($scope, $modal, $stateParams, Velocity) {
      $scope.startFrom = {metric_type: null, metric_value: null};
      $scope.metrics = [];

      $scope.loadFrom = function (startFrom) {
        Velocity.metricsPage(startFrom).then(function (response) {
          $scope.metrics.push.apply($scope.metrics, response);
        }, function () {
          $scope.metrics = [];
        });
      };

      $scope.loadFrom($scope.startFrom);
    }])

  .controller('VelocityDataCtrl', ['$scope', '$modal', '$stateParams', 'Velocity', 'VelocityConfig',
    function ($scope, $modal, $stateParams, Velocity, VelocityConfig) {
      $scope.startFrom = {
        key: {
          id: {metric_type: null, metric_value: null},
          related_metric_type: null,
          create_date: null
        }
      };
      $scope.data_list = [];
      $scope.configs = {};

      VelocityConfig.all().then(function (response) {
        var configs_list = response;
        for (var i in configs_list) {
          $scope.configs[configs_list[i].metric_type] = configs_list[i];
        }
      }, function () {
        $scope.configs = {};
      });

      $scope.loadFrom = function (startFrom) {
        Velocity.dataPage({
          metric_type : startFrom.key.id.metric_type,
          metric_value : startFrom.key.id.metric_value,
          related_metric_type : startFrom.key.related_metric_type,
          create_date : !startFrom.key.create_date ? null : new Date(startFrom.key.create_date).getTime()
        }).then(function (response) {
          $scope.data_list.push.apply($scope.data_list, response);
        }, function (error) {
          $scope.data_list = [];
        });
      };
      $scope.expiresIn = function (date, metric_type) {
        var ttl_sec = $scope.configs[metric_type].time_to_live;
        var now_millis = new Date().getTime();
        var create_date_millis = new Date(date).getTime();
        return msToTime(Math.floor(create_date_millis + (ttl_sec * 1000) - now_millis));
      };

      $scope.loadFrom($scope.startFrom);
    }])

  .controller('VelocityCheckCtrl', ['$scope', '$modal', '$stateParams', 'Velocity', 'VelocityConfig',
    function ($scope, $modal, $stateParams, Velocity, VelocityConfig) {
      $scope.test_data = {};
      $scope.metrics = [];


      VelocityConfig.all().then(function (response) {
        var configs = response;
        for(var i = 0; i < configs.length; i++) {
          for (var j = 0; j <  configs[i].primary_metrics.length; j++) {
            var metric = configs[i].primary_metrics[j];
            if (!($scope.test_data[metric] != undefined)) $scope.test_data[metric] = null;
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
          for (var j = 0; j <  configs[i].aggregation_configs.length; j++) {
            var metric = configs[i].aggregation_configs[j].secondary_metric;
            if (!($scope.test_data[metric] != undefined)) $scope.test_data[metric] = null;
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
        }
      }, function () {
      });

      $scope.check = function () {
        Velocity.check($scope.test_data).then(function (response) {
          $scope.metrics = response
        }, function () {
          $scope.metrics = null;
        });
      };
    }]);
