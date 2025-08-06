package net.zsoo.spring.mybatis;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/application-context.xml",
})
public class MybatisServiceTest {

    @Autowired
    private MybatisService mybatisService;

    @Test
    public void testGetAllRecords() {
        Record[] records = mybatisService.getAllRecords();

        assertNotNull(records, "Records should not be null");
        assertNotEquals(0, records.length, "Records should not be empty");
    }
}
