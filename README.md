# sql-extra

## Introduction
JPA 或则 JDBC 都是将sql语句直接写在代码中，这样与代码的耦合性较强，不直观。 
`sql-extra`项目类似于mybatis，可以将代码与sql分离，并且使用更加简单。

## Example

- XML中添加配置

```config
<bean id="freemarkerSqlTemplates" class="com.sql.extra.FreemarkerSqlTemplates">
    <property name="suffix" value=".sftl" />
    <property name="templateBasePackage" value="/sqls/**" />
</bean>

<bean id="applicationContextHelper" class="com.sql.extra.ApplicationContextHelper" lazy-init="false"></bean>
```

- 按照默认配置，在`classpath:/sqls`下创建`.sftl`文件

```sql
--findByContent
SELECT * FROM t_sample WHERE 1=1
<#if content??>
AND content LIKE '${content}'
</#if>

--findCustomVO
SELECT id,name as viewName FROM t_sample WHERE id='${id}'
```

- 调用 `SqlTemplatesUtils.getQuerySql` 静态方法返回SQL

```class

Map<String, Object> params = new HashMap<String, Object>();
params.put("content", "pengc");
String sql = SqlTemplatesUtils.getQuerySql(this.getClass(), "findByContent", params);
System.out.println(sql);

```

## TODO

- 防止sql注入

## References
- [spring-data-jpa-extra](https://github.com/slyak/spring-data-jpa-extra)

