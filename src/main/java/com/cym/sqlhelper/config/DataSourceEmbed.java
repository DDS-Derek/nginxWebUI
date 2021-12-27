package com.cym.sqlhelper.config;

import java.io.File;

import javax.sql.DataSource;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;

import com.cym.config.HomeConfig;
import com.cym.config.InitConfig;

import cn.hutool.db.ds.pooled.DbConfig;
import cn.hutool.db.ds.pooled.PooledDataSource;

@Component
public class DataSourceEmbed {
	@Inject
	HomeConfig homeConfig;
	
	DataSource dataSource;
	@Init(index = 20)
	public void init() {
		String dbPath = homeConfig.home  + "h2";

		if (dataSource == null) {
			DbConfig dbConfig = new DbConfig();
			dbConfig.setUrl("jdbc:h2:" + dbPath);
			dbConfig.setUser("sa");
			dbConfig.setPass("");
			dbConfig.setMaxActive(1);
			dataSource = new PooledDataSource(dbConfig);
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}
