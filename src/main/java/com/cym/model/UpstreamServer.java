package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.InitValue;
import com.cym.sqlhelper.config.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 负载节点server
 *
 */
@ApiModel("负载节点server")
@Table
public class UpstreamServer extends BaseModel {
	/**
	 * *负载均衡upstream的id
	 */
	@ApiModelProperty("*负载均衡upstream的id")
	String upstreamId;
	/**
	 * *负载节点ip (例:10.10.10.1)
	 */
	@ApiModelProperty("*负载节点ip (例:10.10.10.1)")
	String server;
	/**
	 * *负载节点端口 (例:8080)
	 */
	@ApiModelProperty("*负载节点端口 (例:8080)")
	Integer port;
	/**
	 * 负载节点权重
	 */
	@ApiModelProperty("负载节点权重")
	Integer weight;
	/**
	 * 失败等待时间,秒
	 */
	@ApiModelProperty("失败等待时间,秒")
	Integer failTimeout;
	/**
	 * 最大失败次数
	 */
	@ApiModelProperty("最大失败次数")
	Integer maxFails;
	/**
	 * 最大连接数
	 */
	@ApiModelProperty("最大连接数")
	Integer maxConns;
	/**
	 * 状态策略 'none':无(默认) 'down':停用 'backup':备用
	 */
	@ApiModelProperty("状态策略 'none':无(默认) 'down':停用 'backup':备用")
	@InitValue("none")
	String status;
	
	/**
	 * @ignore
	 */
	@ApiModelProperty(hidden = true, name = "监控状态 -1:未检测(默认) 0:不通 1:通")
	@InitValue("-1")
	Integer monitorStatus;

	public Integer getMaxConns() {
		return maxConns;
	}

	public void setMaxConns(Integer maxConns) {
		this.maxConns = maxConns;
	}

	public Integer getMonitorStatus() {
		return monitorStatus;
	}

	public void setMonitorStatus(Integer monitorStatus) {
		this.monitorStatus = monitorStatus;
	}

	public Integer getFailTimeout() {
		return failTimeout;
	}

	public void setFailTimeout(Integer failTimeout) {
		this.failTimeout = failTimeout;
	}

	public Integer getMaxFails() {
		return maxFails;
	}

	public void setMaxFails(Integer maxFails) {
		this.maxFails = maxFails;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
