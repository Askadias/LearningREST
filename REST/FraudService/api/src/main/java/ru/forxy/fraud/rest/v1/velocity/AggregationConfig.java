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
}
