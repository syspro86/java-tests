package net.zsoo.spring.batch.job2;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.zsoo.spring.batch.common.AbstractBatch;
import net.zsoo.spring.mybatis.Record;

@Component
@RequiredArgsConstructor
public class Batch2Sample extends AbstractBatch<Record, String> {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job batch2SampleJob() throws DuplicateJobException {
        return super.createJob();
    }

    @Override
    public String processItem(Record item) {
        return item.getRecordId();
    }

    @Override
    public ItemReader<Record> itemReader() {
        MyBatisCursorItemReader<Record> reader = new MyBatisCursorItemReader<>();
        reader.setQueryId("net.zsoo.spring.mybatis.RecordMapper.select");
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValuesSupplier(() -> new HashMap<>());
        return reader;
    }

    @Override
    public ItemWriter<String> itemWriter() {
        return items -> items.forEach(System.out::println);
    }

}
