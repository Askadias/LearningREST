package ru.forxy.fraud.logic.blacklist;

import ru.forxy.fraud.rest.v1.list.BlackListItem;

import java.util.List;

/**
 * Black lists manipulation logic API
 */
public interface IBlackListManager {

    List<BlackListItem> getMoreItemsFrom(final String type, final String value);

    List<BlackListItem> getMoreItemsFrom(final String type, final String value, final int limit);

    BlackListItem get(final String type, final String value);

    boolean isInBlackList(final String type, final String value);

    void add(final BlackListItem item);

    void update(final BlackListItem item);

    void delete(final BlackListItem item);
}