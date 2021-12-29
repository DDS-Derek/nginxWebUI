package com.cym.sqlhelper.bean;

import java.io.Serializable;

public class BaseModel implements Serializable {
	/**
	 * 主键
	 */
	String id;
	/**
	 * @ignore
	 */
	Long createTime;
	/**
	 * @ignore
	 */
	Long updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

}
