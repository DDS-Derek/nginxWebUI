package com.cym.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cym.controller.adminPage.ConfController;
import com.cym.model.Admin;
import com.cym.service.AdminService;
import com.cym.service.BakService;
import com.cym.service.ConfService;
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
@RestController
@RequestMapping("/api/nginx")
public class NginxApiController extends BaseController {
	@Autowired
	ConfController confController;
	@Autowired
	AdminService adminService;
	@Autowired
	SettingService settingService;
	@Autowired
	ConfService confService;
	@Autowired
	BakService bakService;

	@ApiOperation("获取nginx状态")
	@PostMapping("nginxStatus")
	public JsonResult<?> nginxStatus() {
		if (NginxUtils.isRun()) {
			return renderSuccess(m.get("confStr.running"));
		} else {
			return renderError(m.get("confStr.stopped"));
		}
	}

	@ApiOperation("下发审批结果")
	@PostMapping("applyResult")
	public JsonResult<?> applyResult(@RequestHeader String token, HttpServletRequest request, //
			@RequestParam @ApiParam("审批编号") String applyNumber, //
			@RequestParam @ApiParam("审批结果 1已通过 2未通过") Integer status, //
			@RequestParam @ApiParam("机器别名（不填为本地）") String remoteName//
	) {
		if (!bakService.isApplying()) {
			return renderError("当前没有申请中的更改");
		}

		Admin admin = getAdmin(request);
		confService.replaceApplyOver(applyNumber, status, admin.getName(), remoteName);

		return renderSuccess("下发成功");
	}

//	@ApiOperation("替换conf文件")
//	@PostMapping("replace")
//	public JsonResult<?> replace(@RequestHeader String token, HttpServletRequest request) {
//		JsonResult jsonResult = confController.replace(confController.getReplaceJson(), request, null);
//		if (jsonResult.isSuccess()) {
//			return renderSuccess("替换成功");
//		} else {
//			return renderError("替换失败");
//		}
//	}

	@ApiOperation("效验conf文件")
	@PostMapping("check")
	public JsonResult<?> checkBase() {
		JsonResult jsonResult = confController.checkBase();
		if (jsonResult.isSuccess()) {
			return renderSuccess("效验成功");
		} else {
			return renderError("效验失败");
		}
	}

	@ApiOperation("重载conf文件")
	@PostMapping("reload")
	public synchronized JsonResult<?> reload() {
		JsonResult jsonResult = confController.reload(null, null, null);
		if (jsonResult.isSuccess()) {
			return renderSuccess("重载成功");
		} else {
			return renderError("重载失败");
		}
	}

	@ApiOperation("获取nginx启动命令")
	@PostMapping("getNginxStartCmd")
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

	@ApiOperation("获取nginx停止命令")
	@PostMapping("getNginxStopCmd")
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

	@ApiOperation("执行nginx命令")
	@PostMapping("runNginxCmd")
	public JsonResult<?> runNginxCmd(@RequestParam @ApiParam("执行命令") String cmd) {

		JsonResult jsonResult = confController.runCmd(cmd, null);
		jsonResult.setObj(HtmlUtil.cleanHtmlTag(jsonResult.getObj().toString()));
		return jsonResult;
	}

}
