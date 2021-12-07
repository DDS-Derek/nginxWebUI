package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.InitValue;
import cn.craccd.sqlHelper.config.Table;
import io.swagger.annotations.ApiModelProperty;

@Table
public class Remote extends BaseModel{
	@ApiModelProperty("访问协议")
	String protocol;
	@ApiModelProperty("ip")
	String ip;
	@ApiModelProperty("端口")
	Integer port;
	@ApiModelProperty("状态 0 掉线 1在线")
	@InitValue("0")
	Integer status; // 0 掉线 1在线
	@ApiModelProperty("授权码")
	String creditKey;
	@ApiModelProperty("用户名")
	String name;
	@ApiModelProperty("密码")
	String pass;
	@ApiModelProperty("版本")
	String version;
	@ApiModelProperty("操作系统")
	String system;
	@ApiModelProperty("别名")
	String descr;
	@ApiModelProperty("是否监控")
	@InitValue("0")
	Integer monitor;
	@ApiModelProperty("父id")
	String parentId;
	@ApiModelProperty("类型 0 服务器 1分组")
	Integer type; //0 服务器 1分组
	@ApiModelProperty("nginx状态")
	Integer nginx; //0未运行 1在运行 2未知
	
	
	
	public Integer getMonitor() {
		return monitor;
	}

	public void setMonitor(Integer monitor) {
		this.monitor = monitor;
	}

	public Integer getNginx() {
		return nginx;
	}

	public void setNginx(Integer nginx) {
		this.nginx = nginx;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCreditKey() {
		return creditKey;
	}

	public void setCreditKey(String creditKey) {
		this.creditKey = creditKey;
	}


}
