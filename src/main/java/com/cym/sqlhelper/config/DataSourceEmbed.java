package com.cym.sqlhelper.config;

import javax.sql.DataSource;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;

import com.cym.config.HomeConfig;
import com.cym.sqlhelper.utils.ImportOrExportUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.hutool.core.io.FileUtil;

@Component
public class DataSourceEmbed {
	@Inject
	HomeConfig homeConfig;

	DataSource dataSource;

	@Init(index = 20)
	public void init() {
		// 创建dataSource
		String dbPath = homeConfig.home + "h2";
		HikariConfig dbConfig = new HikariConfig();
		dbConfig.setJdbcUrl(("jdbc:h2:" + dbPath));
		dbConfig.setUsername("sa");
		dbConfig.setPassword("");
		dbConfig.setMaximumPoolSize(1);
		dataSource = new HikariDataSource(dbConfig);

	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
