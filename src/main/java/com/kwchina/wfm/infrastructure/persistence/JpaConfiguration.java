package com.kwchina.wfm.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.cache.HashtableCacheProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ImportResource("classpath:context-infrastructure-persistence.xml")
public class JpaConfiguration extends JdbcConfiguration {
	
	@Bean
	public Map<String, Object> jpaProperties() {
		// TODO: move Hibernate setting into hibernate configuration file.
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("hibernate.dialect", this.getDialect());
		props.put("hibernate.cache.provider_class", HashtableCacheProvider.class.getName());
		return props;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jva = new HibernateJpaVendorAdapter();
		jva.setShowSql(false);
		jva.setGenerateDdl(false);
		jva.setDatabasePlatform(this.getPlatform());
		return jva;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager( localContainerEntityManagerFactoryBean().getObject() );
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		lef.setDataSource(this.dataSource());
		lef.setJpaPropertyMap(this.jpaProperties());
		lef.setJpaProperties(new Properties());
		lef.setJpaVendorAdapter(this.jpaVendorAdapter());
		return lef;
	}
	
	// Since we're no longer using the JpaTemplate, we don't benefit from the
	// exception translation
	// so we need to turn this on explicitly
	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor = new PersistenceExceptionTranslationPostProcessor();
		return persistenceExceptionTranslationPostProcessor;
	}
	
}
