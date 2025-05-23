package net.zsoo.spring.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Hello!";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}
