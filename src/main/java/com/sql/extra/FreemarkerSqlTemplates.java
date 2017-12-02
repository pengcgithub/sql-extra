package com.sql.extra;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO use exist configuration.
 * <br/>
 *
 * @author pengc
 * @see com.sql.extra
 * @since 2017/11/26
 */
public class FreemarkerSqlTemplates implements ResourceLoaderAware {

	private static Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

	private static StringTemplateLoader sqlTemplateLoader = new StringTemplateLoader();

	static {
		cfg.setTemplateLoader(sqlTemplateLoader);
	}

	protected final Logger logger = LoggerFactory.getLogger(FreemarkerSqlTemplates.class);

	private String encoding = "UTF-8";

	private Map<String, Long> lastModifiedCache = new ConcurrentHashMap<String, Long>();

	private Map<String, Resource> sqlResources = new ConcurrentHashMap<String, Resource>();

	private String templateLocation = "classpath:/sqls";

	private String templateBasePackage = "**";

	private ResourceLoader resourceLoader;

	private String suffix = ".sftl";

	private Map<String, NamedTemplateResolver> suffixResolvers = new HashMap<String, NamedTemplateResolver>();

	{
		suffixResolvers.put(".sftl", new SftlNamedTemplateResolver());
	}

	public String process(String entityName, String methodName, Map<String, Object> model) {
		reloadIfPossible(entityName);
		try {
			StringWriter writer = new StringWriter();
			cfg.getTemplate(getTemplateKey(entityName, methodName), encoding).process(model, writer);
			return writer.toString();
		}
		catch (Exception e) {
			logger.error("process template error. Entity name: " + entityName + " methodName:" + methodName, e);
			return StringUtils.EMPTY;
		}
	}

	private String getTemplateKey(String entityName, String methodName) {
		return entityName + ":" + methodName;
	}

	private void reloadIfPossible(final String entityName) {
		try {
			this.afterPropertiesSet(entityName);
			Long lastModified = lastModifiedCache.get(entityName);
			Resource resource = sqlResources.get(entityName);
			long newLastModified = resource.lastModified();
			if (lastModified == null || newLastModified > lastModified) {
				Iterator<Void> iterator = suffixResolvers.get(suffix)
						.doInTemplateResource(resource, new NamedTemplateCallback() {
							@Override
							public void process(String templateName, String content) {
								sqlTemplateLoader
										.putTemplate(getTemplateKey(entityName, templateName), content);
							}
						});
				while (iterator.hasNext()) {
					iterator.next();
				}
				lastModifiedCache.put(entityName, newLastModified);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		XmlNamedTemplateResolver xmlNamedTemplateResolver = new XmlNamedTemplateResolver(resourceLoader);
		xmlNamedTemplateResolver.setEncoding(encoding);
		this.suffixResolvers.put(".xml", xmlNamedTemplateResolver);
	}

	private void afterPropertiesSet(String name) throws Exception {
		Set<String> names = new HashSet<String>();
		names.add(name);

		String suffixPattern = "/**/"+ name + suffix;
//		String suffixPattern = "/sqls/**/"+ name + suffix;

		if (!names.isEmpty()) {
			String pattern;
			if (StringUtils.isNotBlank(templateBasePackage)) {
				pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
						ClassUtils.convertClassNameToResourcePath(templateBasePackage) + suffixPattern;
			}
			else {
				pattern = templateLocation.contains(suffix) ? templateLocation : templateLocation + suffixPattern;
			}

			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(pattern);

			for (Resource resource : resources) {
				String resourceName = resource.getFilename().replace(suffix, "");
				if (names.contains(resourceName)) {
					sqlResources.put(resourceName, resource);
				}
			}
		}
	}

	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	public void setTemplateBasePackage(String templateBasePackage) {
		this.templateBasePackage = templateBasePackage;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
