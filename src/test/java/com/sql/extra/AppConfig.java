package com.sql.extra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <br/>
 *
 * @author pengc
 * @see com.sql.extra
 * @since 2017/12/2
 */
@Configuration
@ComponentScan("com.sql")
class AppConfig {

	@Bean
	public ApplicationContextHelper applicationContextHelper() {
		ApplicationContextHelper applicationContextHelper = new ApplicationContextHelper();
		return applicationContextHelper;
	}

	@Bean
	public FreemarkerSqlTemplates freemarkerSqlTemplates() {
		FreemarkerSqlTemplates templates = new FreemarkerSqlTemplates();
		templates.setTemplateLocation("classpath:/sqls");
		templates.setSuffix(".sftl");
		templates.setTemplateBasePackage("/sqls/**");
		return templates;
	}

}
