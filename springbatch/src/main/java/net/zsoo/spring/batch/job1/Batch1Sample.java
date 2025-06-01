package net.zsoo.spring.batch.job1;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import net.zsoo.spring.batch.common.AbstractBatch;

@Component
public class Batch1Sample extends AbstractBatch<String, String> {

    @Override
    public String processItem(String item) {
        return item.toUpperCase();
    }

    @Override
    public ItemReader<String> itemReader() {
        return createFileReader("springbatch/pom.xml", (line, no) -> line);
    }

    @Override
    public ItemWriter<String> itemWriter() {
        return items -> items.forEach(System.out::println);
    }

}
