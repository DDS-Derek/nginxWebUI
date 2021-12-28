package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Http参数
 * @author CYM
 *
 */
@ApiModel("Http参数")
@Table
public class Http extends BaseModel {
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
	 * 参数单位
	 */
	@ApiModelProperty("参数单位")
	String unit;

	@ApiModelProperty(hidden = true)
	Long seq;

	public Http() {

	}

	public Http(String name, String value, Long seq) {
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
