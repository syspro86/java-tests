package net.zsoo.spring.batch.job2;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.zsoo.spring.batch.common.AbstractBatch;
import net.zsoo.spring.mybatis.Record;

@Component
@RequiredArgsConstructor
public class Batch2Sample extends AbstractBatch<Record, String> {

    @Override
    public String processItem(Record item) {
        return item.getRecordId();
    }

    @Override
    public ItemReader<Record> itemReader() {
        return createMybatisReader("net.zsoo.spring.mybatis.RecordMapper.select");
    }

    @Override
    public ItemWriter<String> itemWriter() {
        return items -> items.forEach(System.out::println);
    }

}
