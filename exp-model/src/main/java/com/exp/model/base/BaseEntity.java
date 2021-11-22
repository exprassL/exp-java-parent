package com.exp.model.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 */
public abstract class BaseEntity
        implements Serializable
{
    private static final long serialVersionUID = -6459450730018338418L;
    private String id;
    private Boolean logicDeleted;
    private String createBy;
    private Date createOn;
    private String lastUpdatedBy;
    private Date lastUpdatedOn;

    protected String getId()
    {
        return this.id;
    }

    protected void setId(String id) {
        this.id = id;
    }
    
    protected Boolean getLogicDeleted() {
        return logicDeleted;
    }
    
    protected void setLogicDeleted(Boolean logicDeleted) {
        this.logicDeleted = logicDeleted;
    }
    
    protected String getCreateBy() {
        return this.createBy;
    }

    protected void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    protected Date getCreateOn() {
        return this.createOn;
    }

    protected void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    protected String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    protected void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    protected Date getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    protected void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}