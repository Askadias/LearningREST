package ru.forxy.fraud.rest.v1.velocity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Aggregation function type for velocity metrics calculation
 */
public enum AggregationType {
    Max {
        @Override
        public Double apply(List<VelocityData> data) {
            Double max = Double.MIN_VALUE;
            for (VelocityData metricData : data) {
                try {
                    double value = Double.parseDouble(metricData.getRelatedMetricValue());
                    if (value > max) {
                        max = value;
                    }
                } catch (NumberFormatException nfe) {
                    return null;
                }
            }
            return max;
        }
    },
    Min {
        @Override
        public Double apply(List<VelocityData> data) {
            Double min = Double.MAX_VALUE;
            for (VelocityData metricData : data) {
                try {
                    double value = Double.parseDouble(metricData.getRelatedMetricValue());
                    if (value < min) {
                        min = value;
                    }
                } catch (NumberFormatException nfe) {
                    return null;
                }
            }
            return min;
        }
    },
    Avg {
        @Override
        public Double apply(List<VelocityData> data) {
            Double sum = 0.0;
            if (data.size() > 0) {
                for (VelocityData metricData : data) {
                    try {
                        sum += Double.parseDouble(metricData.getRelatedMetricValue());
                    } catch (NumberFormatException nfe) {
                        return null;
                    }
                }
                return sum / data.size();
            } else {
                return 0.0;
            }
        }
    },
    Sum {
        @Override
        public Double apply(List<VelocityData> data) {
            Double sum = 0.0;
            for (VelocityData metricData : data) {
                try {
                    sum += Double.parseDouble(metricData.getRelatedMetricValue());
                } catch (NumberFormatException nfe) {
                    return null;
                }
            }
            return sum;
        }
    },
    Count {
        @Override
        public Double apply(List<VelocityData> data) {
            return (double) data.size();
        }
    },
    UniqueCount {
        @Override
        public Double apply(List<VelocityData> data) {
            Set<String> noDupSet = new HashSet<>();
            for (VelocityData metricData : data) {
                noDupSet.add(metricData.getRelatedMetricValue());
            }
            return (double) noDupSet.size();
        }
    };

    public abstract Double apply(List<VelocityData> data);
}
