package net.zsoo.spring.mybatis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

@SpringJUnitWebConfig(locations = {
        // "file:src/main/webapp/WEB-INF/spring/servlet-context.xml",
        "classpath:properties/application-context.xml",
})
public class MybatisServiceTest {

    @Autowired
    private MybatisService mybatisService;

    @Test
    public void testGetAllRecords() {
        Record[] records = mybatisService.getAllRecords();
        // Add assertions to verify the records
        assert records != null : "Records should not be null";
        assert records.length > 0 : "There should be at least one record";
        // Further assertions can be added based on expected data
    }
}
