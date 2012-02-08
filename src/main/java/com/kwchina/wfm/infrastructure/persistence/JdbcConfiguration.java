package com.kwchina.wfm.infrastructure.persistence;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfiguration {
	
	@Value("${jdbc.platform}")
	private
	String platform;
	
	@Value("${jdbc.dialect}")
	private
	String dialect;
	
	@Value("${jdbc.url}")
	String url;

	@Value("${jdbc.username}")
	String username;

	@Value("${jdbc.password}")
	String password;
	
	@Value("${jdbc.driver}")
	String driver;
	
	public String getPlatform() {
		return platform;
	}
	
	public String getDialect() {
		return dialect;
	}

	@Bean(destroyMethod="close")
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driver);
		
		return ds;
	}
	
}
