package ru.forxy.common.logging.filter;

import ru.forxy.common.support.Context;

/**
 * Created by Uladzislau_Prykhodzk on 11/15/13.
 */
public interface IFilter {

    Context.ContextData doFilter(Context.ContextData data);
}
