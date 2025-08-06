package net.zsoo.spring.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class SampleRestController {

    @RequestMapping(path = "/hello", produces = "text/html; charset=UTF-8")
    public String hello(@RequestParam(name = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "! in rest";
    }
}
