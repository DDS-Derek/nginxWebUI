package com.cym.controller.api;

import java.io.IOException;
import java.util.List;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import com.cym.controller.adminPage.CertController;
import com.cym.model.Cert;
import com.cym.model.CertCode;
import com.cym.service.CertService;
import com.cym.sqlhelper.bean.Page;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 证书接口
 *
 */
@Api(tags = "证书接口")
@Mapping("/api/cert")
public class CertApiController extends BaseController {

	@Inject
	CertController certController;
	@Inject
	CertService certService;

	/**
	 * 获取证书分页列表
	 * @param current
	 * @param limit
	 * @param keywords
	 * @return
	 */
	@ApiOperation("获取证书分页列表")
	@Mapping("getPage")
	public JsonResult<Page<Cert>> getPage(@ApiParam("当前页数(从1开始)") Integer current, //
			@ApiParam("每页数量(默认为10)") Integer limit, //
			@ApiParam("查询关键字") String keywords) {
		Page page = new Page();
		page.setCurr(current);
		page.setLimit(limit);
		page = certService.getPage(keywords, page);

		return renderSuccess(page);
	}

	/**
	 * 添加或编辑证书
	 * @param cert
	 * @return
	 */
	@ApiOperation("添加或编辑证书")
	@Mapping("addOver")
	public JsonResult addOver(Cert cert) {
		if (StrUtil.isEmpty(cert.getDomain())) {
			return renderError("域名为空");
		}

		if (cert.getType() == 0) {
			if (StrUtil.isEmpty(cert.getDnsType())) {
				return renderError("dns提供商为空");
			}

			if (cert.getDnsType().equals("ali") && (StrUtil.isEmpty(cert.getAliKey()) || StrUtil.isEmpty(cert.getAliSecret()))) {
				return renderError("aliKey 或 aliSecret为空");
			}
			if (cert.getDnsType().equals("dp") && (StrUtil.isEmpty(cert.getDpId()) || StrUtil.isEmpty(cert.getDpKey()))) {
				return renderError("dpId 或 dpKey为空");
			}
			if (cert.getDnsType().equals("cf") && (StrUtil.isEmpty(cert.getCfEmail()) || StrUtil.isEmpty(cert.getCfKey()))) {
				return renderError("cfEmail 或 cfKey为空");
			}
			if (cert.getDnsType().equals("gd") && (StrUtil.isEmpty(cert.getGdKey()) || StrUtil.isEmpty(cert.getGdSecret()))) {
				return renderError("gdKey 或 gdSecret为空");
			}
		}
		return certController.addOver(cert, null, null, null);
	}

	/**
	 * 获取域名解析码
	 * @param certId
	 * @return
	 */
	@ApiOperation("获取域名解析码")
	@Mapping("getTxtValue")
	public JsonResult<List<CertCode>> getTxtValue(String certId) {
		List<CertCode> certCodes = certService.getCertCodes(certId);
		return renderSuccess(certCodes);
	}

	/**
	 * 设置证书自动续签
	 * @param id
	 * @param autoRenew
	 * @return
	 */
	@ApiOperation("设置证书自动续签")
	@Mapping("setAutoRenew")
	public JsonResult setAutoRenew(@ApiParam("主键id") String id, @ApiParam("是否自动续签:0否 1是") Integer autoRenew) {
		Cert cert = new Cert();
		cert.setId(id);
		cert.setAutoRenew(autoRenew);

		certController.setAutoRenew(cert);
		return renderSuccess();
	}

	/**
	 * 删除证书
	 * @param id
	 * @return
	 */
	@ApiOperation("删除证书")
	@Mapping("del")
	public JsonResult del(String id) {
		return certController.del(id);
	}

	/**
	 * 执行申请
	 * @param id
	 * @param type
	 * @return
	 */
	@ApiOperation("执行申请")
	@Mapping("apply")
	public JsonResult<List<CertCode>> apply(@ApiParam("主键id") String id, @ApiParam("申请类型 issue:申请 renew:续签") String type) {

		JsonResult jsonResult = certController.apply(id, type);

		if (jsonResult.isSuccess() && jsonResult.getObj() != null) {
			jsonResult.setMsg(m.get("certStr.dnsDescr"));
		}

		return jsonResult;
	}

	/**
	 * 下载证书文件
	 * @param id
	 * @throws IOException
	 */
	@ApiOperation("下载证书文件")
	@Mapping("download")
	public void download(@ApiParam("主键id") String id) throws IOException {
		certController.download(id);
	}
}
