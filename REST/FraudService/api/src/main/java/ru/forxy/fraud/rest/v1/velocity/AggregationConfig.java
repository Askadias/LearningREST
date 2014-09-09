package ru.forxy.fraud.rest.v1.velocity;

import java.io.Serializable;

/**
 * Related metrics aggregation configuration
 */
public class AggregationConfig implements Serializable {

    private static final long serialVersionUID = -3187562484249396106L;

    private AggregationType type;
    private Long period;

    public AggregationType getType() {
        return type;
    }

    public void setType(AggregationType type) {
        this.type = type;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregationConfig)) return false;

        AggregationConfig that = (AggregationConfig) o;

        if (period != null ? !period.equals(that.period) : that.period != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AggregationConfig{" +
                "type=" + type +
                ", period=" + period +
                '}';
    }
}
