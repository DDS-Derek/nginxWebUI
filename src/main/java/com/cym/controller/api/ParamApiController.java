package com.cym.controller.api;

import java.io.IOException;
import java.util.List;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import com.cym.model.Param;
import com.cym.service.ParamService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "额外参数接口")
@Mapping("/api/param")
public class ParamApiController extends BaseController {

	@Inject
	ParamService paramService;

	/**
	 * 根据项目获取参数列表
	 * @param serverId
	 * @param locationId
	 * @param upstreamId
	 * @return
	 */
	@ApiOperation("根据项目获取参数列表")
	@Mapping("getList")
	public JsonResult<List<Param>> getList(@ApiParam("所属反向代理id") String serverId, //
			@ApiParam("所属代理目标id") String locationId, //
			@ApiParam("所属负载均衡id") String upstreamId) {
		if (StrUtil.isEmpty(serverId) && StrUtil.isEmpty(locationId) && StrUtil.isEmpty(upstreamId)) {
			return renderError(m.get("apiStr.paramError"));
		}

		List<Param> list = paramService.getList(serverId, locationId, upstreamId);
		return renderSuccess(list);
	}

	/**
	 * 添加或编辑参数
	 * @param param
	 * @return
	 * @throws IOException
	 */
	@ApiOperation("添加或编辑参数")
	@Mapping("insertOrUpdate")
	public JsonResult<?> insertOrUpdate(Param param) throws IOException {
		Integer count = 0;
		if (StrUtil.isNotEmpty(param.getLocationId())) {
			count++;
		}
		if (StrUtil.isNotEmpty(param.getServerId())) {
			count++;
		}
		if (StrUtil.isNotEmpty(param.getUpstreamId())) {
			count++;
		}

		if (count != 1) {
			return renderError(m.get("apiStr.paramError"));
		}

		sqlHelper.insertOrUpdate(param);

		return renderSuccess(param);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ApiOperation("删除")
	@Mapping("del")
	public JsonResult<?> del(String id) {
		sqlHelper.deleteById(id, Param.class);

		return renderSuccess();
	}

}
