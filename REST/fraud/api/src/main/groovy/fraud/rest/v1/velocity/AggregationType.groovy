package fraud.rest.v1.velocity
/**
 * Aggregation function type for velocity metrics calculation
 */
enum AggregationType {
    Max{
        @Override
        Double apply(List<VelocityData> data) {
            Double max = Double.MIN_VALUE;
            data.each { VelocityData metricData ->
                try {
                    double value = Double.parseDouble(metricData.relatedMetricValue);
                    if (value > max) {
                        max = value;
                    }
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
            return max;
        }
    },
    Min{
        @Override
        Double apply(List<VelocityData> data) {
            Double min = Double.MAX_VALUE;
            data.each { VelocityData metricData ->
                try {
                    double value = Double.parseDouble(metricData.relatedMetricValue);
                    if (value < min) {
                        min = value;
                    }
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
            return min;
        }
    },
    Avg{
        @Override
        Double apply(List<VelocityData> data) {
            Double sum = 0.0;
            if (data.size() > 0) {
                for (VelocityData metricData : data) {
                    try {
                        sum += Double.parseDouble(metricData.relatedMetricValue);
                    } catch (NumberFormatException ignored) {
                        return null;
                    }
                }
                return sum / data.size();
            } else {
                return 0.0;
            }
        }
    },
    Sum{
        @Override
        Double apply(List<VelocityData> data) {
            Double sum = 0.0;
            data.each { VelocityData metricData ->
                try {
                    sum += Double.parseDouble(metricData.relatedMetricValue);
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
            return sum;
        }
    },
    Count{
        @Override
        Double apply(List<VelocityData> data) {
            return (double) data.size();
        }
    },
    UniqueCount{
        @Override
        Double apply(List<VelocityData> data) {
            Set<String> noDupSet = new HashSet<>();
            data.each { VelocityData metricData ->
                noDupSet.add(metricData.relatedMetricValue);
            }
            return (double) noDupSet.size();
        }
    };

    abstract Double apply(List<VelocityData> data);
}
