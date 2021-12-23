package com.cym.controller.adminPage;

import javax.servlet.http.HttpSession;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cym.model.OperateLog;
import com.cym.service.OperateLogService;
import com.cym.sqlhelper.bean.Page;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@Controller
@Mapping("/adminPage/operateLog")
public class OperateLogController extends BaseController{
	@Inject
	OperateLogService operateLogService;
	
	@Mapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page) {
		page = operateLogService.search(page);
		
		modelAndView.addObject("page", page);
		
		modelAndView.setViewName("/adminPage/operatelog/index");
		return modelAndView;
	}
	
	
	@Mapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, OperateLog.class));
	}
}
