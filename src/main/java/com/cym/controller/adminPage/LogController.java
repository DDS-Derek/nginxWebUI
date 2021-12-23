package com.cym.controller.adminPage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page) {
		page = logService.search(page);
		modelAndView.addObject("page", page);

		modelAndView.addObject("isLinux", SystemTool.isLinux());
		modelAndView.setViewName("/adminPage/log/index");
		return modelAndView;
	}

	@Mapping("addOver")
	@ResponseBody
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
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Log.class));
	}

	@Mapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		sqlHelper.deleteById(id, Log.class);
		return renderSuccess();
	}

	@Mapping("tail")
	public ModelAndView tail(ModelAndView modelAndView, String id, String protocol, HttpServletRequest request) {
		modelAndView.addObject("id", id);
		// 获取远程机器的协议
		if (StrUtil.isNotEmpty(protocol)) {
			if (protocol.equals("https")) {
				modelAndView.addObject("protocol", "wss:");
			}
			if (protocol.equals("http")) {
				modelAndView.addObject("protocol", "ws:");
			}
		}

		String httpHost = request.getHeader("X-Forwarded-Host");
		String realPort = request.getHeader("X-Forwarded-Port");
		String host = request.getHeader("Host");

		String ctxWs = AdminInterceptor.getCtx(httpHost, host, realPort);
		modelAndView.addObject("ctxWs", ctxWs);
		
		modelAndView.setViewName("/adminPage/log/tail");
		return modelAndView;
	}

	@ResponseBody
	@Mapping("down")
	public void down(ModelAndView modelAndView, String id, HttpServletResponse response) {
		Log log = sqlHelper.findById(id, Log.class);
		outputStream(new File(log.getPath()), response);
	}

	private void outputStream(File file, HttpServletResponse response) {
		try {
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=" + URLUtil.encode(file.getName());
			response.setHeader(headerKey, headerValue);

			InputStream inputStream = new FileInputStream(file);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}
}
