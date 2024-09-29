package com.kecoyo.turtle.common.batch;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

// @Configuration
// @EnableBatchProcessing
public class Job2Configuration {

    private final Random random;

    public Job2Configuration() {
        this.random = new Random();
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("job", jobRepository).start(step1(jobRepository, transactionManager)).build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository).<Integer, Integer>chunk(3, transactionManager)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Integer> itemReader() {
        List<Integer> items = new LinkedList<>();
        // read a random number of items in each run
        for (int i = 0; i < random.nextInt(1000); i++) {
            items.add(i);
        }
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<Integer> itemWriter() {
        return items -> {
            for (Integer item : items) {
                int nextInt = random.nextInt(1000);
                Thread.sleep(nextInt);
                System.out.println("item = " + item);
            }
        };
    }

}
