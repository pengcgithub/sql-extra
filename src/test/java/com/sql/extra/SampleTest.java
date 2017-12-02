package com.sql.extra;

import com.sql.utils.SqlTemplatesUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.HashMap;
import java.util.Map;

/**
 * <br/>
 *
 * @author pengc
 * @see com.sql.extra
 * @since 2017/12/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class SampleTest {

    @Test
    public void findByContent() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("content", "pengc");
        String sql = SqlTemplatesUtils.getQuerySql(this.getClass(), "findByContent", params);
        System.out.println(sql);
    }

}
