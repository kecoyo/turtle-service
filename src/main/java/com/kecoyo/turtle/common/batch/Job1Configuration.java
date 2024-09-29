package com.kecoyo.turtle.common.batch;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

// @Configuration
// @EnableBatchProcessing
public class Job1Configuration {

    private final Random random;

    public Job1Configuration() {
        this.random = new Random();
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("job", jobRepository).start(step1(jobRepository, transactionManager))
                .next(step2(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository).tasklet((contribution, chunkContext) -> {
            System.out.println("hello");
            // simulate processing time
            Thread.sleep(random.nextInt(3000));
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository).tasklet((contribution, chunkContext) -> {
            System.out.println("world");
            // simulate step failure
            int nextInt = random.nextInt(3000);
            Thread.sleep(nextInt);
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

}
