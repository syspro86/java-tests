package net.zsoo.spring.mybatis;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MybatisController {

    private final RecordMapper mapper;

    @RequestMapping("/mybatis")
    public String index() {
        Record[] records = mapper.select();
        return Stream.of(records).map(r -> r.getRecordId())
                .collect(Collectors.joining("<br>"));
    }
}
