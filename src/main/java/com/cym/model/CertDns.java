package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.Table;

/**
 * 
 * acme-dns需要的参数
 *
 */
@Table
public class CertDns extends BaseModel {
	// acme-dns需要的参数
	String username;
	String password;
	String fulldomain;
	String subdomain;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFulldomain() {
		return fulldomain;
	}

	public void setFulldomain(String fulldomain) {
		this.fulldomain = fulldomain;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

}
