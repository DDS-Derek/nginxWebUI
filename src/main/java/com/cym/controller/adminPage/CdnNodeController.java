package com.cym.controller.adminPage;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.model.CdnNode;
import com.cym.service.CdnNodeService;
import com.cym.sqlhelper.bean.Sort;
import com.cym.sqlhelper.bean.Sort.Direction;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@Mapping("/adminPage/cdnNode")
@Controller
public class CdnNodeController extends BaseController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	CdnNodeService cdnNodeService;

	@Mapping("")
	public ModelAndView index(ModelAndView modelAndView) {

		modelAndView.put("list", sqlHelper.findAll(new Sort("dir", Direction.ASC), CdnNode.class));
		modelAndView.view("/adminPage/cdnNode/index.html");
		return modelAndView;
	}

	@Mapping("addOver")
	public JsonResult addOver(CdnNode CdnNode) {
		sqlHelper.insertOrUpdate(CdnNode);
		return renderSuccess();
	}

	@Mapping("del")
	public JsonResult del(String id) {
		sqlHelper.deleteById(id, CdnNode.class);
		return renderSuccess();
	}

	@Mapping("detail")
	public JsonResult detail(String id) {
		CdnNode cdnNode = sqlHelper.findById(id, CdnNode.class);

		return renderSuccess(cdnNode);
	}

}
