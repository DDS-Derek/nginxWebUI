package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 额外参数
 *
 */
@ApiModel("额外参数")
@Table
public class Param extends BaseModel {
	/**
	 * 所属反向代理id
	 */
	@ApiModelProperty("所属反向代理id")
	String serverId;
	/**
	 * 所属代理目标id
	 */
	@ApiModelProperty("所属代理目标id")
	String locationId;
	/**
	 * 所属负载均衡id
	 */
	@ApiModelProperty("所属负载均衡id")
	String upstreamId;
	
	/**
	 * @ignore
	 */
	@ApiModelProperty(hidden = true)
	String templateId;
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
	String templateValue;
	/**
	 * @ignore
	 */
	@ApiModelProperty(hidden = true)
	String templateName;
	
	
	public String getTemplateValue() {
		return templateValue;
	}

	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
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
