package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * Stream参数
 *
 */
@ApiModel("Stream参数")
@Table
public class Stream extends BaseModel {
	/**
	 * 参数名
	 */
	@ApiModelProperty("参数名")
	String name;
	/**
	 * 参数值
	 */
	@ApiModelProperty("参数值")
	String value;
	/**
	 * @ignore
	 */
	@ApiModelProperty(hidden = true)
	Long seq;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}


}
