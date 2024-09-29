package com.kecoyo.turtle.common.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.kecoyo.turtle.domain.Question;

// @Configuration
// @EnableBatchProcessing
public class JsonJobConfiguration {

    @Bean
    @StepScope
    public JsonItemReader<Question> itemReader() {
        return new JsonItemReaderBuilder<Question>()
                .name("itemReader")
                // .resource(new ClassPathResource("data/question-data.json"))
                .resource(new FileSystemResource("data/question-data.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Question.class))
                .build();
    }

    @Bean
    @StepScope
    public JsonFileItemWriter<Question> itemWriter() {
        return new JsonFileItemWriterBuilder<Question>()
                .name("itemWriter")
                .resource(new FileSystemResource("data/question-data2.json"))
                .lineSeparator("\n")
                .encoding("GBK")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .shouldDeleteIfExists(true)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, JdbcTransactionManager transactionManager,
            JsonItemReader<Question> itemReader, JsonFileItemWriter<Question> itemWriter) {
        return new StepBuilder("step", jobRepository).<Question, Question>chunk(2, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository).start(step).build();
    }

}
