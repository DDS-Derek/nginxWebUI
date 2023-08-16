package com.cym.controller.adminPage;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.ext.CdnNodeExt;
import com.cym.model.CdnNode;
import com.cym.model.Cert;
import com.cym.service.CdnNodeService;
import com.cym.service.CertService;
import com.cym.sqlhelper.bean.Page;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@Mapping("/adminPage/cdnNode")
@Controller
public class CdnNodeController extends BaseController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	CdnNodeService cdnNodeService;
	@Inject
	CertService certService;

	@Mapping("")
	public ModelAndView index(ModelAndView modelAndView, Page page) {
		page = cdnNodeService.search(page);
		
		List<CdnNodeExt> exts = new ArrayList<>();
		for (CdnNode cdnNode : (List<CdnNode>) page.getRecords()) {
			CdnNodeExt cdnNodeExt = new CdnNodeExt();
			cdnNodeExt.setCdnNode(cdnNode);
			
			Cert cert = sqlHelper.findById(cdnNode.getCertId(), Cert.class);
			cdnNodeExt.setCert(cert); 

			exts.add(cdnNodeExt);
		}
		page.setRecords(exts);
		
		modelAndView.put("page", page);
		
		modelAndView.put("certList", certService.getIssueList());
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
