package com.cym.controller.api;

import java.io.IOException;
import java.util.List;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import com.cym.controller.adminPage.PasswordController;
import com.cym.model.Password;
import com.cym.service.PasswordService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "密码文件接口")

@Mapping("/api/password")
public class PasswordApiController extends BaseController{
	@Inject
	PasswordService passwordService;
	@Inject
	PasswordController passwordController;
	
	/**
	 * 获取全部密码文件列表
	 * @return
	 */
	@ApiOperation("获取全部密码文件列表")
	@Mapping("getList")
	public JsonResult<List<Password>> getList() {
		List<Password> list = sqlHelper.findAll(Password.class);
		return renderSuccess(list);
	}

	/**
	 * 添加或编辑密码文件
	 * @param password
	 * @return
	 * @throws IOException
	 */
	@ApiOperation("添加或编辑密码文件")
	@Mapping("insertOrUpdate")
	public JsonResult<?> insertOrUpdate(Password password) throws IOException {
		return renderSuccess(passwordController.addOver(password));
	}

	/**
	 * 删除密码文件
	 * @param id
	 * @return
	 */
	@ApiOperation("删除密码文件")
	@Mapping("del")
	public JsonResult<?> del(String id) {
		Password password = sqlHelper.findById(id, Password.class);
		sqlHelper.deleteById(id, Password.class);
		FileUtil.del(password.getPath());

		return renderSuccess();
	}

}
