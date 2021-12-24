package com.cym.controller.adminPage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.config.AdminInterceptor;
import com.cym.config.ScheduleTask;
import com.cym.model.Log;
import com.cym.service.LogService;
import com.cym.service.SettingService;
import com.cym.sqlhelper.bean.Page;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SystemTool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

@Controller
@Mapping("/adminPage/log")
public class LogController extends BaseController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	SettingService settingService;
	@Inject
	LogService logService;
	@Inject
	ScheduleTask scheduleTask;

	@Mapping("")
	public ModelAndView index( ModelAndView modelAndView, Page page) {
		page = logService.search(page);
		modelAndView.put("page", page);

		modelAndView.put("isLinux", SystemTool.isLinux());
		modelAndView.view("/adminPage/log/index");
		return modelAndView;
	}

	@Mapping("addOver")
	
	public JsonResult addOver(Log log) {
		if (logService.hasDir(log.getPath(), log.getId())) {
			return renderError(m.get("logStr.sameDir"));
		}

		if (FileUtil.isDirectory(log.getPath())) {
			return renderError(m.get("logStr.notFile"));
		}

		sqlHelper.insertOrUpdate(log);
		return renderSuccess();
	}

	@Mapping("detail")
	
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Log.class));
	}

	@Mapping("del")
	
	public JsonResult del(String id) {
		sqlHelper.deleteById(id, Log.class);
		return renderSuccess();
	}

	@Mapping("tail")
	public ModelAndView tail(ModelAndView modelAndView, String id, String protocol) {
		modelAndView.put("id", id);
		// 获取远程机器的协议
		if (StrUtil.isNotEmpty(protocol)) {
			if (protocol.equals("https")) {
				modelAndView.put("protocol", "wss:");
			}
			if (protocol.equals("http")) {
				modelAndView.put("protocol", "ws:");
			}
		}

		String httpHost = Context.current().header("X-Forwarded-Host");
		String realPort = Context.current().header("X-Forwarded-Port");
		String host = Context.current().header("Host");

		String ctxWs = AdminInterceptor.getCtx(httpHost, host, realPort);
		modelAndView.put("ctxWs", ctxWs);
		
		modelAndView.view("/adminPage/log/tail");
		return modelAndView;
	}

	
	@Mapping("down")
	public void down(ModelAndView modelAndView, String id) {
		Log log = sqlHelper.findById(id, Log.class);
		outputStream(new File(log.getPath()));
	}

	private void outputStream(File file) {
		try {
			Context.current().contentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=" + URLUtil.encode(file.getName());
			Context.current().header(headerKey, headerValue);

			InputStream inputStream = new FileInputStream(file);
			IOUtils.copy(inputStream, Context.current().outputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}
}
