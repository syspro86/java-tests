package net.zsoo.spring.hello;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

@SpringJUnitWebConfig(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/servlet-context.xml" })
public class HelloControllerTest {

    @Test
    public void testHello(@Autowired HelloController helloController) {
        String result = helloController.hello();
        assertEquals("Hello, World!", result);
    }
}
