package velocity

enum Aggregation {
    Max{
        @Override
        Double apply(List<String> data) {
            Double max = Double.MIN_VALUE;
            data.each {
                try {
                    double value = Double.parseDouble(it);
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
        Double apply(List<String> data) {
            Double min = Double.MAX_VALUE;
            data.each {
                try {
                    double value = Double.parseDouble(it);
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
        Double apply(List<String> data) {
            Double sum = 0.0;
            if (data.size() > 0) {
                data.each {
                    try {
                        sum += Double.parseDouble(it);
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
        Double apply(List<String> data) {
            Double sum = 0.0;
            data.each {
                try {
                    sum += Double.parseDouble(it);
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
            return sum;
        }
    },
    Count{
        @Override
        Double apply(List<String> data) {
            return (double) data.size();
        }
    },
    UniqueCount{
        @Override
        Double apply(List<String> data) {
            Set<String> noDupSet = new HashSet<>();
            data.each {
                noDupSet.add(it);
            }
            return (double) noDupSet.size();
        }
    };

    abstract Double apply(List<String> data);
}
