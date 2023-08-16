package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.InitValue;
import com.cym.sqlhelper.config.Table;

@Table
public class CdnNode extends BaseModel {
	// 部署域名
	String domain;
	// 部署证书id
	String certId;

	/**
	 * aliKey(阿里云需要的参数)
	 */
	String aliKey;
	/**
	 * aliSecret(阿里云需要的参数)
	 */
	String aliSecret;

	// 自动部署
	@InitValue("1")
	Integer autoDepley;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getAliKey() {
		return aliKey;
	}

	public void setAliKey(String aliKey) {
		this.aliKey = aliKey;
	}

	public String getAliSecret() {
		return aliSecret;
	}

	public void setAliSecret(String aliSecret) {
		this.aliSecret = aliSecret;
	}

	public Integer getAutoDepley() {
		return autoDepley;
	}

	public void setAutoDepley(Integer autoDepley) {
		this.autoDepley = autoDepley;
	}

}
