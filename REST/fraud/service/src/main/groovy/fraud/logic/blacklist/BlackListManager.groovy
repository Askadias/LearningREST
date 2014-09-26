package fraud.logic.blacklist

import common.exceptions.ServiceException
import fraud.db.dao.IBlackListDAO
import fraud.exceptions.FraudServiceEventLogId
import fraud.rest.v1.list.BlackListItem
import fraud.rest.v1.list.ListPartitionKey

/**
 * Implementation class for BlackListService business logic
 */
class BlackListManager implements IBlackListManager {

    private static final int DEFAULT_PAGE_SIZE = 10

    IBlackListDAO blackListDAO

    @Override
    List<BlackListItem> getMoreItemsFrom(final String type, final String value) {
        getMoreItemsFrom(type, value, DEFAULT_PAGE_SIZE)
    }

    @Override
    List<BlackListItem> getMoreItemsFrom(final String type, final String value, final int limit) {
        blackListDAO.getList(type, value, limit)
    }

    @Override
    BlackListItem get(String type, String value) {
        blackListDAO.get(new ListPartitionKey(type: type, value: value))
    }

    @Override
    boolean isInBlackList(String type, String value) {
        blackListDAO.isInBlackList(new ListPartitionKey(type: type, value: value))
    }

    @Override
    void add(BlackListItem item) {
        item.setCreateDate(new Date())
        BlackListItem existingItem = null//blackListDAO.get(item.getKey())
        if (existingItem == null) {
            blackListDAO.save(item)
        } else if (!existingItem.getIsActive()) {
            existingItem.setIsActive(true)
            existingItem.setUpdateDate(new Date())
            existingItem.setUpdatedBy(item.getUpdatedBy())
            blackListDAO.save(existingItem)
        } else {
            throw new ServiceException(FraudServiceEventLogId.IsInBlackListAlready,
                    item.getKey().getType(), item.getKey().getValue())
        }
    }

    @Override
    void update(BlackListItem item) {
        if (blackListDAO.get(item.getKey()) != null) {
            blackListDAO.save(item)
            item.setUpdateDate(new Date())
        } else {
            throw new ServiceException(FraudServiceEventLogId.BlackListItemIsNotExist,
                    item.getKey().getType(), item.getKey().getValue())
        }
    }

    @Override
    void delete(BlackListItem item) {

    }
}
