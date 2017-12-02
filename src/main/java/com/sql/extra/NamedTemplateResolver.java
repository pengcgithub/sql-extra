package com.sql.extra;

import org.springframework.core.io.Resource;

import java.util.Iterator;

/**
 * <br/>
 *
 * @author pengc
 * @see com.sql.extra
 * @since 2017/11/26
 */
public interface NamedTemplateResolver {
	Iterator<Void> doInTemplateResource(Resource resource, final NamedTemplateCallback callback) throws Exception;
}
