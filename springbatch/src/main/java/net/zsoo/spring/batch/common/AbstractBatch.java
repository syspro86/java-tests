package net.zsoo.spring.batch.common;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractBatch<InputType, OutputType> {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    protected Job createJob() throws DuplicateJobException {
        String name = getClass().getSimpleName();
        return createJob(name);
    }

    protected Job createJob(String name) throws DuplicateJobException {
        return jobBuilderFactory.get(name)
                .start(step())
                .build();
    }

    protected Step step() {
        String name = getClass().getSimpleName();
        return stepBuilderFactory.get(name)
                .<InputType, OutputType>chunk(10)
                .reader(itemReader())
                .processor((ItemProcessor<InputType, OutputType>) this::processItem)
                .writer(itemWriter())
                .build();
    }

    public abstract ItemReader<InputType> itemReader();

    public abstract OutputType processItem(InputType item);

    public abstract ItemWriter<OutputType> itemWriter();

    protected ItemReader<InputType> createFileReader(String path, LineMapper<InputType> mapper) {
        FlatFileItemReader<InputType> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(path));
        reader.setLineMapper(mapper);
        return reader;
    }
}
