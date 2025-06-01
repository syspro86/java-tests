package net.zsoo.spring.batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = "net.zsoo.spring")
public class BatchMain {
    public static void main(String[] args) throws Exception {
        List<String> jobArgs = new ArrayList<>();
        jobArgs.add(BatchMain.class.getName());
        jobArgs.add("Batch2Sample");
        jobArgs.add("start_timestamp=" + System.currentTimeMillis());
        jobArgs.addAll(Arrays.asList(args));

        CommandLineJobRunner.main(jobArgs.toArray(new String[jobArgs.size()]));
    }
}
