package com.cecilireid.springchallenges;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    //Note: use constructor injector
    //https://www.baeldung.com/java-spring-field-injection-cons
    //https://www.baeldung.com/spring-injection-lombok
    @Autowired
    public CateringJobRepository repository;

    /*
    Depreciated
    See
    https://stackoverflow.com/questions/75508267/how-to-solve-deprecation-warning-of-jobbuilderfactory-and-stepbuilderfactory
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    */
    @Bean
    @StepScope
    public FlatFileItemReader<CateringJob> reader() {
        FlatFileItemReader<CateringJob> reader = new FlatFileItemReader<>();
        DefaultLineMapper<CateringJob> lineMapper = new DefaultLineMapper<>();
        return reader;
    }

    @Bean
    public RepositoryItemWriter<CateringJob> writer() {
//        RepositoryItemWriter<CateringJob> writer = new RepositoryItemWriter<>();
        return null;
    }

    @Bean
    public Job uploadCateringJob() {
        return null;
    }

    @Bean
    public Step step() {
        return null;
    }
}
