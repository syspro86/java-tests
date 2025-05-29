package net.zsoo.spring.hello;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HelloControllerTest {

    @Test
    public void testHello(@Autowired HelloController helloController) {
        String result = helloController.hello();
        assertEquals("Hello, World!", result);
    }
}
