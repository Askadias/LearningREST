package ru.forxy.common.logging.writer;

import ru.forxy.common.logging.filter.IFilter;
import ru.forxy.common.support.Context;

import java.util.List;

/**
 * Writer applies configured filters before logging data
 */
public class FilteringLogWriter extends LogWriter {

    private List<IFilter> filters;

    @Override
    public void log(Context.ContextData data) {
        Context.ContextData frame = data;
        for (IFilter filter: filters) {
            frame = filter.doFilter(frame);
        }
        super.log(frame);
    }

    public void setFilters(List<IFilter> filters) {
        this.filters = filters;
    }
}
