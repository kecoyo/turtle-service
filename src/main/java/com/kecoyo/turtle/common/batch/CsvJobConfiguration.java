package com.kecoyo.turtle.common.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.kecoyo.turtle.domain.Question;

// @Configuration
// @EnableBatchProcessing
public class CsvJobConfiguration {

    @Bean
    @StepScope
    public FlatFileItemReader<Question> itemReader() {
        return new FlatFileItemReaderBuilder<Question>()
                .name("itemReader")
                // .resource(new ClassPathResource("data/question-data.csv"))
                .resource(new FileSystemResource("data/question-data.csv"))
                .delimited()
                .names("stageName", "subjectName", "questionId", "mainId", "qstType", "difficulty", "knowledges")
                .targetType(Question.class)
                // .fieldSetMapper(new QuestionFieldSetMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Question> itemWriter() {
        return new FlatFileItemWriterBuilder<Question>()
                .name("itemWriter")
                .resource(new FileSystemResource("data/question-data2.csv"))
                .delimited()
                .names("stageName", "subjectName", "questionId", "mainId", "qstType", "difficulty", "knowledges")
                .headerCallback(writer -> writer
                        .write("stageName, subjectName, questionId, mainId, qstType, difficulty, knowledges"))
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, JdbcTransactionManager transactionManager,
            FlatFileItemReader<Question> itemReader, FlatFileItemWriter<Question> itemWriter) {
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
