package com.cym.sqlhelper.config;

import com.cym.config.SQLConstants;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;

import com.cym.config.HomeConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class DataSourceEmbed {
	@Inject
	HomeConfig homeConfig;

	@Inject("${spring.database.type}")
	String databaseType;
	@Inject("${spring.datasource.url}")
	String url;
	@Inject("${spring.datasource.username}")
	String username;
	@Inject("${spring.datasource.password}")
	String password;

	@Inject("${spring.database.tablePrefix:`}")
	String TABLE_PREFIX = "`";
	@Inject("${spring.database.tableSuffix:`}")
	String TABLE_SUFFIX = "`";

	@Inject("${spring.database.columnPrefix:`}")
	String COLUMN_PREFIX = "`";
	@Inject("${spring.database.columnSuffix:`}")
	String COLUMN_SUFFIX = "`";

	@Inject("${spring.database.idColumn:`id`}")
	String ID_COLUMN = "`id`";

	@Inject("${spring.database.orderTypeInt:SIGNED}")
	String ORDER_TYPE_INT = "SIGNED";

	@Inject("${spring.database.limitScript:(function() { return \" LIMIT \" + offset + \",\" + limit;})();}")
	String LIMIT_SCRIPT = "(function() { return \" LIMIT \" + offset + \",\" + limit;})();";

	HikariDataSource dataSource;

	@Init
	public void init() {
		SQLConstants.TABLE_SUFFIX=TABLE_SUFFIX;
		SQLConstants.TABLE_PREFIX=TABLE_PREFIX;
		SQLConstants.COLUMN_SUFFIX=COLUMN_SUFFIX;
		SQLConstants.COLUMN_PREFIX=COLUMN_PREFIX;
		SQLConstants.ID_COLUMN=ID_COLUMN;
		SQLConstants.ORDER_TYPE_INT=ORDER_TYPE_INT;
		SQLConstants.LIMIT_SCRIPT=LIMIT_SCRIPT;
		// 创建dataSource
		if (databaseType.equalsIgnoreCase("sqlite") || databaseType.equalsIgnoreCase("h2")) {
			// 建立新的sqlite数据源
			HikariConfig dbConfig = new HikariConfig();
			dbConfig.setJdbcUrl("jdbc:sqlite:" + homeConfig.home + "sqlite.db");
			dbConfig.setUsername("");
			dbConfig.setPassword("");
			dbConfig.setMaximumPoolSize(1);
			dbConfig.setDriverClassName("org.sqlite.JDBC");
			dataSource = new HikariDataSource(dbConfig);
		} else if (databaseType.equalsIgnoreCase("mysql")) {
			HikariConfig dbConfig = new HikariConfig();
			dbConfig.setJdbcUrl(url);
			dbConfig.setUsername(username);
			dbConfig.setPassword(password);
			dbConfig.setMaximumPoolSize(1);
			dbConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource = new HikariDataSource(dbConfig);
		}else {
			HikariConfig dbConfig = new HikariConfig();
			dbConfig.setJdbcUrl(url);
			dbConfig.setUsername(username);
			dbConfig.setPassword(password);
			dbConfig.setMaximumPoolSize(1);
			dbConfig.setDriverClassName(databaseType);
			dataSource = new HikariDataSource(dbConfig);
		}
	}

	public HikariDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(HikariDataSource dataSource) {
		this.dataSource = dataSource;
	}

}
