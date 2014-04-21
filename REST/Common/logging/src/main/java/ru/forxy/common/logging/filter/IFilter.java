package ru.forxy.common.logging.filter;

import ru.forxy.common.support.Context;

/**
 * Filter interface to exclude some sensitive data from writing
 */
public interface IFilter {

    Context.ContextData doFilter(Context.ContextData data);
}
