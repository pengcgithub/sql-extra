package com.sql.utils;

import com.sql.extra.ApplicationContextHelper;
import com.sql.extra.FreemarkerSqlTemplates;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL模板工具类<br/>
 *
 * @author pengc
 * @see com.sql.utils
 * @since 2017/11/26
 */
public class SqlTemplatesUtils {

    /**
     * 获取查询sql<br>  
     * @author pengc
     * @param classProperty 类属性
     * @param methodName 方法名称
     * @param params {@code Map<string, object> 查询条件}
     * @return 组装好的查询sql
     * @since 2017/12/1
     */
    public static String getQuerySql(
            Class classProperty, String methodName, Map<String, Object> params) {
        FreemarkerSqlTemplates messageSource = ApplicationContextHelper.getBean("freemarkerSqlTemplates");
        String sql = messageSource.process(classProperty.getSimpleName(), methodName, params);
        return sql;
    }

    /**
     * 获取查询sql，不传入查询条件。<br>
     * @author pengc
     * @param classProperty 类属性
     * @param methodName 方法名称
     * @return 组装好的查询sql
     * @since 2018/6/1
     */
    public static String getQuerySql(Class classProperty, String methodName) {
        FreemarkerSqlTemplates messageSource = ApplicationContextHelper.getBean("freemarkerSqlTemplates");
        String sql = messageSource.process(classProperty.getSimpleName(), methodName, new HashMap<>());
        return sql;
    }

}
