package ru.forxy.fraud.logic.blacklist;

import ru.forxy.fraud.db.dao.IBlackListDAO;
import ru.forxy.fraud.rest.v1.list.BlackListItem;

import java.util.List;

/**
 * Implementation class for BlackListService business logic
 */
public class BlackListManager implements IBlackListManager {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IBlackListDAO blackListDAO;

    public List<BlackListItem> getAllBlackLists() {
        return blackListDAO.getAll();
    }

    @Override
    public List<BlackListItem> getMoreItems(BlackListItem start) {
        return null;
    }

    @Override
    public List<BlackListItem> getMoreItems(BlackListItem start, int limit) {
        return null;
    }

    @Override
    public BlackListItem get(String type, String value) {
        return null;
    }

    @Override
    public boolean isInBlackList(String type, String value) {
        return false;
    }

    @Override
    public void add(BlackListItem item) {

    }

    @Override
    public void update(BlackListItem item) {

    }

    @Override
    public void delete(BlackListItem item) {

    }

    public void setBlackListDAO(final IBlackListDAO blackListDAO) {
        this.blackListDAO = blackListDAO;
    }
}
