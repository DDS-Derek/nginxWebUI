package com.cym.controller.api;

import java.util.List;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import com.cym.model.Location;
import com.cym.model.Server;
import com.cym.service.ParamService;
import com.cym.service.ServerService;
import com.cym.sqlhelper.bean.Page;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SnowFlakeUtils;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
/**
 * 反向代理(server)接口
 */
@Api(tags = "反向代理(server)接口")
@Mapping("/api/server")
public class ServerApiController extends BaseController {
	@Inject
	ServerService serverService;
	@Inject
	ParamService paramService;

	/**
	 * 获取server分页列表
	 * @param current
	 * @param limit
	 * @param keywords
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation("获取server分页列表")
	@Mapping("getPage")
	public JsonResult<Page<Server>> getPage(@ApiParam("当前页数(从1开始)") Integer current, //
			@ApiParam("每页数量(默认为10)") Integer limit, //
			@ApiParam("查询关键字") String keywords) {
		Page page = new Page();
		page.setCurr(current);
		page.setLimit(limit);
		page = serverService.search(page, keywords);

		return renderSuccess(page);
	}

	/**
	 * 添加或编辑server
	 * @param server
	 * @return
	 */
	@ApiOperation("添加或编辑server")
	@Mapping("insertOrUpdate")
	public JsonResult<?> insertOrUpdate(Server server) {
		if (StrUtil.isEmpty(server.getListen())) {
			return renderError("listen" + m.get("apiStr.notFill"));
		}

		if (StrUtil.isEmpty(server.getId())) {
			server.setSeq(SnowFlakeUtils.getId());
		}
		sqlHelper.insertOrUpdate(server);
		return renderSuccess(server);
	}

	/**
	 * 删除server
	 * @param id
	 * @return
	 */
	@ApiOperation("删除server")
	@Mapping("delete")
	public JsonResult<?> delete(String id) {
		serverService.deleteById(id);

		return renderSuccess();
	}

	/**
	 * 根据serverId获取location列表
	 * @param serverId
	 * @return
	 */
	@ApiOperation("根据serverId获取location列表")
	@Mapping("getLocationByServerId")
	public JsonResult<List<Location>> getLocationByServerId(String serverId) {
		List<Location> locationList = serverService.getLocationByServerId(serverId);
		for (Location location : locationList) {
			String json = paramService.getJsonByTypeId(location.getId(), "location");
			location.setLocationParamJson(json != null ? json : null);
		}
		return renderSuccess(locationList);
	}

	/**
	 * 添加或编辑location
	 * @param location
	 * @return
	 */
	@ApiOperation("添加或编辑location")
	@Mapping("insertOrUpdateLocation")
	public JsonResult<?> insertOrUpdateLocation(Location location) {
		if (StrUtil.isEmpty(location.getServerId())) {
			return renderError("serverId" + m.get("apiStr.notFill"));
		}
		if (StrUtil.isEmpty(location.getPath())) {
			return renderError("path" + m.get("apiStr.notFill"));
		}
		sqlHelper.insertOrUpdate(location);
		return renderSuccess(location);
	}

	/**
	 * 删除location
	 * @param id
	 * @return
	 */
	@ApiOperation("删除location")
	@Mapping("deleteLocation")
	public JsonResult<?> deleteLocation(String id) {
		sqlHelper.deleteById(id, Location.class);

		return renderSuccess();
	}


}
