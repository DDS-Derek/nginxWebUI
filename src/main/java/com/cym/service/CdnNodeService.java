package com.cym.service;

import org.noear.solon.annotation.Inject;
import org.noear.solon.aspect.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.sqlhelper.utils.SqlHelper;

@Service
public class CdnNodeService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	SqlHelper sqlHelper;
	
	
}
