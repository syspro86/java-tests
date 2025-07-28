package net.zsoo.spring.mybatis;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MybatisController {

    private final MybatisService service;

    public MybatisController(MybatisService service) {
        this.service = service;
    }

    @RequestMapping(path = "/mybatis", produces = "text/html; charset=UTF-8")
    public String index() {
        Record[] records = service.getAllRecords();
        return Stream.of(records).map(r -> r.getRecordId())
                .collect(Collectors.joining("<br>"));
    }
}
