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
            $scope.velocity_config.aggregation_configs = $scope.velocity_config.aggregation_configs || [];
            $scope.original = angular.copy($scope.velocity_config);
          }
        }, function () {
        });
      }

      VelocityConfig.all().then(function (response) {
        var configs = response;
        for (var i = 0; i < configs.length; i++) {
          for (var j = 0; j < configs[i].primary_metrics.length; j++) {
            var metric = configs[i].primary_metrics[j];
            if (!($scope.metrics.indexOf(metric) > -1)) {
              $scope.metrics.push(metric);
              $scope.metricsAutocomplete.push({name: metric})
            }
          }
          for (var j = 0; j < configs[i].aggregation_configs.length; j++) {
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
        $scope.velocity_config.updated_by = 'admin';
        $scope.velocity_config.update_date = new Date();
        if ($scope.mode === 'new') {
          $scope.velocity_config.created_by = 'admin';
          $scope.velocity_config.create_date = new Date();
          VelocityConfig.add($scope.velocity_config).then(function (response) {
            $state.go('velocity.config.list');
          }, function (error) {
            error.data.messages.forEach(function (item) {
            });
          });
        } else if ($scope.mode === 'edit') {
          $scope.velocity_config.save();
          $state.go('velocity.config.list');
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
      $scope.addNewMetric = function () {

      }
    }])

  .controller('VelocityMetricsCtrl', ['$scope', '$modal', '$stateParams', 'Velocity', 'VelocityConfig',
    function ($scope, $modal, $stateParams, Velocity, VelocityConfig) {
      $scope.filter = {};
      $scope.metrics = [];
      $scope.velocity_metrics = null;

      VelocityConfig.all().then(function (response) {
        var configs = response;
        for (var i = 0; i < configs.length; i++) {
          for (var j = 0; j < configs[i].primary_metrics.length; j++) {
            var metric = configs[i].primary_metrics[j];
            if (!($scope.filter[metric] != undefined)) $scope.filter[metric] = null;
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
          for (var j = 0; j < configs[i].aggregation_configs.length; j++) {
            var metric = configs[i].aggregation_configs[j].secondary_metric;
            if (!($scope.filter[metric] != undefined)) $scope.filter[metric] = null;
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
        }
      }, function () {
      });

      $scope.isMetric = function (data) {
        return data instanceof Object;
      };

      $scope.search = function () {
        Velocity.metrics($scope.filter).then(function (response) {
          $scope.velocity_metrics = response
        }, function () {
          $scope.velocity_metrics = null;
        });
      };
    }])

  .controller('VelocityDataCtrl', ['$scope', '$modal', '$stateParams', 'Velocity', 'VelocityConfig', '$state',
    function ($scope, $modal, $stateParams, Velocity, VelocityConfig, $state) {
      $scope.filter = {};
      $scope.metrics = [];
      $scope.transactions = null;
      $scope.currentDate = $stateParams.start_date ? new Date($stateParams.start_date) : new Date();
      $scope.dates = {startDate: $scope.currentDate, endDate: null};
      $scope.loading = false;

      VelocityConfig.all().then(function (response) {
        var configs = response;
        for (var i = 0; i < configs.length; i++) {
          for (var j = 0; j < configs[i].primary_metrics.length; j++) {
            var metric = configs[i].primary_metrics[j];
            if (!($scope.filter[metric] != undefined)) $scope.filter[metric] = null;
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
          for (var j = 0; j < configs[i].aggregation_configs.length; j++) {
            var metric = configs[i].aggregation_configs[j].secondary_metric;
            if (!($scope.filter[metric] != undefined)) $scope.filter[metric] = null;
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
        }
      }, function () {
      });

      $scope.search = function () {
        if ($scope.dates.startDate && $scope.dates.endDate) {
          $scope.filter['start_date'] = $scope.dates.startDate;
          $scope.filter['end_date'] = $scope.dates.endDate;
        }
        $scope.loading = true;
        Velocity.history($scope.filter).then(function (response) {
          $scope.transactions = response;
          $scope.loading = false;
        }, function () {
          $scope.transactions = null;
          $scope.loading = false;
        });
      };

      $scope.openPage = function (day) {
        $scope.openRange(day, moment(day).add(1, 'd')._d);
      };

      $scope.openRange = function (startDate, endDate) {
        $scope.dates.startDate = startDate;
        $scope.dates.endDate = endDate;
        $scope.search();
      };

      $scope.moreData = function() {
        if (!$scope.loading && $scope.transactions && $scope.transactions.length > 0) {
          var lastTransaction = $scope.transactions[$scope.transactions.length - 1];
          var filterExt = angular.copy($scope.filter);
          filterExt['start_date'] = lastTransaction.create_date;
          filterExt['start_id'] = lastTransaction.id;
          $scope.loading = true;

          Velocity.history(filterExt).then(function (response) {
            if (response && response.length > 0) {
              $scope.transactions = $scope.transactions.concat(response)
            }
            $scope.loading = false;
          }, function () {
            $scope.loading = false;
          });
        }
      };

      $scope.getDate = function(dateMs) {
        if ($scope.currentDate.getDate() == new Date(dateMs).getDate()) {
          return moment(dateMs).format('hh:mm:ss a');
        } else {
          return moment(dateMs).format('Do MMMM GGGG, hh:mm:ss a');
        }
      };

      $scope.openPage($scope.currentDate)
    }])

  .controller('VelocityCheckCtrl', ['$scope', '$modal', '$stateParams', 'Velocity', 'VelocityConfig',
    function ($scope, $modal, $stateParams, Velocity, VelocityConfig) {
      $scope.test_data = {};
      $scope.metrics = [];
      $scope.velocity_metrics = null;


      VelocityConfig.all().then(function (response) {
        var configs = response;
        for (var i = 0; i < configs.length; i++) {
          for (var j = 0; j < configs[i].primary_metrics.length; j++) {
            var metric = configs[i].primary_metrics[j];
            if (!($scope.test_data[metric] != undefined)) $scope.test_data[metric] = [];
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
          for (var j = 0; j < configs[i].aggregation_configs.length; j++) {
            var metric = configs[i].aggregation_configs[j].secondary_metric;
            if (!($scope.test_data[metric] != undefined)) $scope.test_data[metric] = [];
            if (!($scope.metrics.indexOf(metric) > -1)) $scope.metrics.push(metric);
          }
        }
      }, function () {
      });

      $scope.isMetric = function (data) {
        return data instanceof Object;
      };

      $scope.check = function () {
        Velocity.check($scope.test_data).then(function (response) {
          $scope.velocity_metrics = response
        }, function () {
          $scope.velocity_metrics = null;
        });
      };
    }]);
