package com.cym.service;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

import com.cym.model.DenyAllow;
import com.cym.sqlhelper.bean.Page;
import com.cym.sqlhelper.utils.SqlHelper;

@Component
public class DenyAllowService {
	@Inject
	SqlHelper sqlHelper;
	

	public Page search(Page page) {
		page = sqlHelper.findPage(page, DenyAllow.class);

		return page;
	}

}
