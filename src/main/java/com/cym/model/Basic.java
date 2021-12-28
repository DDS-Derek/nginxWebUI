package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 基础参数
 */
@ApiModel("基础参数")
@Table
public class Basic extends BaseModel {
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
	@ApiModelProperty(hidden = true)
	Long seq;

	public Basic() {

	}

	public Basic(String name, String value, Long seq) {
		this.name = name;
		this.value = value;
		this.seq = seq;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

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

}
