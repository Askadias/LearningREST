package ru.forxy.fraud.rest.v1.list;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import java.io.Serializable;
import java.util.Date;

/**
 * Base class for blacklist and whitelist items
 */
public class BaseListItem implements Serializable {

    private static final long serialVersionUID = -1587124267609051544L;

    @EmbeddedId
    private ListPartitionKey key;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "updated_by")
    private String updatedBy;

    public BaseListItem() {
    }

    public BaseListItem(String type, String value) {
        this.key = new ListPartitionKey(type, value);
        this.createDate = new Date();
        this.updateDate = new Date();
    }

    public ListPartitionKey getKey() {
        return key;
    }

    public void setKey(ListPartitionKey key) {
        this.key = key;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
