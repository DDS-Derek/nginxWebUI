package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.InitValue;
import cn.craccd.sqlHelper.config.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("备份文件")
@Table
public class Bak extends BaseModel {
	@ApiModelProperty("时间")
	String time;
	@ApiModelProperty("主文件内容")
	String content;
	@ApiModelProperty("版本号")
	String version;
	
	@ApiModelProperty("审批编号")
	String applyNumber;
	@ApiModelProperty("变更内容")
	String changeContent;
	@InitValue("0")
	@ApiModelProperty("状态 0审批中 1已通过 2未通过")
	Integer status;

	
	public String getChangeContent() {
		return changeContent;
	}

	public void setChangeContent(String changeContent) {
		this.changeContent = changeContent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
