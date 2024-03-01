package com.cym.service;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.solon.aspect.annotation.Service;

import com.cym.sqlhelper.utils.SqlHelper;

@ProxyComponent
public class LocationService {
	@Inject
	SqlHelper sqlHelper;
}
