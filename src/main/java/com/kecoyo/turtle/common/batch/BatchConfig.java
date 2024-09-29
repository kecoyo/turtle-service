package com.kecoyo.turtle.common.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.kecoyo.turtle.batch.QuestionItemReader;
import com.kecoyo.turtle.batch.QuestionItemWriter;
import com.kecoyo.turtle.domain.Question;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Question> csvItemReader() {
        return new FlatFileItemReaderBuilder<Question>()
                .name("csvItemReader")
                // .resource(new ClassPathResource("data/question-data.csv"))
                .resource(new FileSystemResource("data/小学数学.csv"))
                .delimited()
                .names("stageName", "subjectName", "questionId", "mainId", "qstType", "difficulty", "knowledges",
                        "status")
                .targetType(Question.class)
                // .fieldSetMapper(new QuestionFieldSetMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Question> jdbcItemReader(DataSource dataSource) {
        String sql = "select * from tk_question";
        return new JdbcCursorItemReaderBuilder<Question>().name("jdbcItemReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper((rs, rowNum) -> {
                    Question q = new Question();

                    q.setQuestionId(rs.getLong("question_id"));
                    q.setMainId(rs.getLong("main_id"));
                    q.setStageName(rs.getString("stage_name"));

                    return q;
                })
                .build();
    }

    @Bean
    public ItemWriter<Question> itemWriter() {
        return items -> {
            for (Question item : items) {
                System.out.println(Thread.currentThread() + ": writing item " + item);
            }
        };
    }

    @Bean
    public Step step(JobRepository jobRepository, JdbcTransactionManager transactionManager,
            ItemReader<Question> jdbcItemReader, ItemWriter<Question> itemWriter, TaskExecutor taskExecutor) {
        return new StepBuilder("step", jobRepository)
                .<Question, Question>chunk(10, transactionManager)
                .reader(jdbcItemReader)
                // .processor(null)
                .writer(itemWriter)
                .taskExecutor(taskExecutor) // 使用配置好的TaskExecutor
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository).start(step).build();
    }

    // 配置ThreadPoolTaskExecutor
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 核心线程数
        executor.setMaxPoolSize(8); // 最大线程数
        executor.setQueueCapacity(100); // 队列大小
        executor.setThreadNamePrefix("MyThread-"); // 线程名前缀
        executor.initialize();
        return executor;
    }
}
