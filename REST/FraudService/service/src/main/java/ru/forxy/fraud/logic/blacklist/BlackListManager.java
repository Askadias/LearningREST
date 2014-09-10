package ru.forxy.fraud.logic.blacklist;

import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.fraud.db.dao.IBlackListDAO;
import ru.forxy.fraud.exceptions.FraudServiceEventLogId;
import ru.forxy.fraud.rest.v1.list.BlackListItem;
import ru.forxy.fraud.rest.v1.list.ListPartitionKey;

import java.util.Date;
import java.util.List;

/**
 * Implementation class for BlackListService business logic
 */
public class BlackListManager implements IBlackListManager {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IBlackListDAO blackListDAO;

    @Override
    public List<BlackListItem> getMoreItemsFrom(final String type, final String value) {
        return getMoreItemsFrom(type, value, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<BlackListItem> getMoreItemsFrom(final String type, final String value, final int limit) {
        return blackListDAO.getList(type, value, limit);
    }

    @Override
    public BlackListItem get(String type, String value) {
        return blackListDAO.get(new ListPartitionKey(type, value));
    }

    @Override
    public boolean isInBlackList(String type, String value) {
        return blackListDAO.isInBlackList(new ListPartitionKey(type, value));
    }

    @Override
    public void add(BlackListItem item) {
        item.setCreateDate(new Date());
        BlackListItem existingItem = null;//blackListDAO.get(item.getKey());
        if (existingItem == null) {
            blackListDAO.save(item);
        } else if (!existingItem.getIsActive()) {
            existingItem.setIsActive(true);
            existingItem.setUpdateDate(new Date());
            existingItem.setUpdatedBy(item.getUpdatedBy());
            blackListDAO.save(existingItem);
        } else {
            throw new ServiceException(FraudServiceEventLogId.IsInBlackListAlready,
                    item.getKey().getType(), item.getKey().getValue());
        }
    }

    @Override
    public void update(BlackListItem item) {
        if (blackListDAO.get(item.getKey()) != null) {
            blackListDAO.save(item);
            item.setUpdateDate(new Date());
        } else {
            throw new ServiceException(FraudServiceEventLogId.BlackListItemIsNotExist,
                    item.getKey().getType(), item.getKey().getValue());
        }
    }

    @Override
    public void delete(BlackListItem item) {

    }

    public void setBlackListDAO(final IBlackListDAO blackListDAO) {
        this.blackListDAO = blackListDAO;
    }
}
