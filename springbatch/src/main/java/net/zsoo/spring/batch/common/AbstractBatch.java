package net.zsoo.spring.batch.common;

import java.util.Map;
import java.util.function.Supplier;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractBatch<InputType, OutputType> implements InitializingBean {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private JobRegistry jobRegistry;

    private Job job;

    public void afterPropertiesSet() throws DuplicateJobException {
        if (job == null) {
            job = createJob();
        }
        jobRegistry.register(new ReferenceJobFactory(job));
    }

    protected String getJobName() {
        return getClass().getSimpleName();
    }

    protected String getStepName() {
        return getJobName() + "Step";
    }

    protected Job createJob() throws DuplicateJobException {
        String name = getJobName();
        return createJob(name);
    }

    protected Job createJob(String name) throws DuplicateJobException {
        return jobBuilderFactory.get(name)
                .start(step())
                .build();
    }

    protected Step step() {
        return stepBuilderFactory.get(getStepName())
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

    protected Map<String, Object> getMybatisParameterValues() {
        return null;
    }

    protected ItemReader<InputType> createMybatisReader(String sqlid) {
        return createMybatisReader(sqlid, this::getMybatisParameterValues);
    }

    protected ItemReader<InputType> createMybatisReader(String sqlid, Supplier<Map<String, Object>> parameterValues) {
        MyBatisCursorItemReader<InputType> reader = new MyBatisCursorItemReader<>();
        reader.setQueryId("net.zsoo.spring.mybatis.RecordMapper.select");
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValuesSupplier(parameterValues);
        return reader;
    }
}
