package com.cym.controller.adminPage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cym.config.InitConfig;
import com.cym.model.Remote;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SystemTool;
import com.cym.utils.UpdateUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

@Mapping("")
@Controller
public class MainController extends BaseController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	UpdateUtils updateUtils;
	@Inject
	SettingService settingService;

	private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

	@Mapping("")
	public ModelAndView index(ModelAndView modelAndView, String keywords) {
		modelAndView.setViewName("/adminPage/index");
		return modelAndView;
	}

	@ResponseBody
	@Mapping("/adminPage/main/upload")
	public JsonResult upload(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
		try {
			File temp = new File(FileUtil.getTmpDir() + "/" + file.getOriginalFilename());
			file.transferTo(temp);

			// 移动文件
			File dest = new File(InitConfig.home + "cert/" + file.getOriginalFilename());
			FileUtil.move(temp, dest, true);

			String localType = (String) httpSession.getAttribute("localType");
			if ("remote".equals(localType)) {
				Remote remote = (Remote) httpSession.getAttribute("remote");

				HashMap<String, Object> paramMap = new HashMap<>();
				paramMap.put("file", dest);

				String rs = HttpUtil.post(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/upload", paramMap);
				JsonResult jsonResult = JSONUtil.toBean(rs, JsonResult.class);
				FileUtil.del(dest);
				return jsonResult;
			}

			return renderSuccess(dest.getPath().replace("\\", "/"));
		} catch (IllegalStateException | IOException e) {
			logger.error(e.getMessage(), e);
		}

		return renderError();
	}

	@ResponseBody
	@Mapping("/adminPage/main/autoUpdate")
	public JsonResult autoUpdate(String url) {
		if (!SystemTool.isLinux()) {
			return renderError(m.get("commonStr.updateTips"));
		}

		ApplicationHome home = new ApplicationHome(getClass());
		File jar = home.getSource();
		String path = jar.getParent() + "/nginxWebUI.jar.update";
		LOG.info("download:" + path);
		HttpUtil.downloadFile(url, path);
		updateUtils.run(path);
		return renderSuccess();
	}
	
	
	@ResponseBody
	@Mapping("/adminPage/main/changeLang")
	public JsonResult changeLang() {
		if (settingService.get("lang") != null && settingService.get("lang").equals("en_US")) {
			settingService.set("lang", "");
		} else {
			settingService.set("lang", "en_US");
		}

		return renderSuccess();
	}

}