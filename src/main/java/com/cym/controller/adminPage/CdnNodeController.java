package com.cym.controller.adminPage;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.cdn20180510.Client;
import com.aliyun.cdn20180510.models.SetCdnDomainSSLCertificateRequest;
import com.aliyun.cdn20180510.models.SetCdnDomainSSLCertificateResponse;
import com.cym.ext.CdnNodeExt;
import com.cym.model.CdnNode;
import com.cym.model.Cert;
import com.cym.service.CdnNodeService;
import com.cym.service.CertService;
import com.cym.sqlhelper.bean.Page;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

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

	@Mapping("deploy")
	public JsonResult deploy(String id) {
		try {
			CdnNode cdnNode = sqlHelper.findById(id, CdnNode.class);
			Cert cert = sqlHelper.findById(cdnNode.getCertId(), Cert.class);

			Client client = createClient(cdnNode.getAliKey(), cdnNode.getAliSecret());

			SetCdnDomainSSLCertificateRequest request = new SetCdnDomainSSLCertificateRequest();
			request.setDomainName(cdnNode.getDomain());
			request.setSSLProtocol("on");
//			request.setCertId(Long.parseLong(cert.getId()));
			request.setCertName(cert.getDomain());
			request.setCertType("upload");
			request.setSSLPub(FileUtil.readString(cert.getPem(), Charset.forName("utf-8")));
			request.setSSLPri(FileUtil.readString(cert.getKey(), Charset.forName("utf-8")));

			logger.info(JSONUtil.toJsonPrettyStr(request));
			
			SetCdnDomainSSLCertificateResponse response = client.setCdnDomainSSLCertificate(request);

			Map<String, Object> map = response.getBody().toMap();

			if (map.containsKey("Message")) {
				return renderError(map.get("Message").toString());
			} else {
				// 记录下部署时间
				cdnNode.setDeployTime(System.currentTimeMillis());
				sqlHelper.updateById(cdnNode);
				return renderSuccess();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			return renderError(e.getMessage());
		}

	}

	public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
		com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
				// 必填，您的 AccessKey ID
				.setAccessKeyId(accessKeyId)
				// 必填，您的 AccessKey Secret
				.setAccessKeySecret(accessKeySecret);
		// Endpoint 请参考 https://api.aliyun.com/product/Cdn
		config.endpoint = "cdn.aliyuncs.com";
		return new Client(config);
	}

}
