package com.cym.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import com.cym.controller.adminPage.ConfController;
import com.cym.service.AdminService;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.NginxUtils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "nginx接口")

@Mapping("/api/nginx")
public class NginxApiController extends BaseController {
	@Inject
	ConfController confController;
	@Inject
	AdminService adminService;
	@Inject
	SettingService settingService;

	/**
	 * 获取nginx状态
	 * @return
	 */
	@ApiOperation("获取nginx状态")
	@Mapping("nginxStatus")
	public JsonResult<?> nginxStatus() {
		if (NginxUtils.isRun()) {
			return renderSuccess(m.get("confStr.running"));
		} else {
			return renderError(m.get("confStr.stopped"));
		}
	}

	/**
	 * 替换conf文件
	 * @return
	 */
	@ApiOperation("替换conf文件")
	@Mapping("replace")
	public JsonResult<?> replace() {
		JsonResult jsonResult = confController.replace(confController.getReplaceJson(), null);
		if (jsonResult.isSuccess()) {
			return renderSuccess("替换成功");
		} else {
			return renderError("替换失败");
		}
	}

	/**
	 * 效验conf文件
	 * @return
	 */
	@ApiOperation("效验conf文件")
	@Mapping("check")
	public JsonResult<?> checkBase() {
		JsonResult jsonResult = confController.checkBase();
		if (jsonResult.isSuccess()) {
			return renderSuccess("效验成功");
		} else {
			return renderError("效验失败");
		}
	}

	/**
	 * 重载conf文件
	 * @return
	 */
	@ApiOperation("重载conf文件")
	@Mapping("reload")
	public synchronized JsonResult<?> reload() {
		JsonResult jsonResult = confController.reload(null, null, null);
		if (jsonResult.isSuccess()) {
			return renderSuccess("重载成功");
		} else {
			return renderError("重载失败");
		}
	}

	/**
	 * 获取nginx启动命令
	 * @return
	 */
	@ApiOperation("获取nginx启动命令")
	@Mapping("getNginxStartCmd")
	public JsonResult<List<String>> getNginxStartCmd() {
		String nginxExe = settingService.get("nginxExe");
		String nginxPath = settingService.get("nginxPath");
		String nginxDir = settingService.get("nginxDir");

		if (StrUtil.isNotEmpty(nginxDir)) {
			nginxDir = " -p " + nginxDir;
		}

		List<String> list = new ArrayList<>();
		list.add(nginxExe + " -c " + nginxPath + nginxDir);
		list.add("systemctl start nginx");
		list.add("service nginx start");

		return renderSuccess(list);
	}

	/**
	 * 获取nginx停止命令
	 * @return
	 */
	@ApiOperation("获取nginx停止命令")
	@Mapping("getNginxStopCmd")
	public JsonResult<List<String>> getNginxStopCmd() {
		String nginxExe = settingService.get("nginxExe");
		String nginxDir = settingService.get("nginxDir");

		if (StrUtil.isNotEmpty(nginxDir)) {
			nginxDir = " -p " + nginxDir;
		}

		List<String> list = new ArrayList<>();
		list.add(nginxExe + "  -s stop " + nginxDir);
		list.add("pkill nginx");
		list.add("taskkill /f /im nginx.exe");
		list.add("systemctl stop nginx");
		list.add("service nginx stop");

		return renderSuccess(list);
	}

	/**
	 * 执行nginx命令
	 * @param cmd
	 * @return
	 */
	@ApiOperation("执行nginx命令")
	@Mapping("runNginxCmd")
	public JsonResult<?> runNginxCmd(@ApiParam("执行命令") String cmd) {

		JsonResult jsonResult = confController.runCmd(cmd, null);
		jsonResult.setObj(HtmlUtil.cleanHtmlTag(jsonResult.getObj().toString()));
		return jsonResult;
	}

}
