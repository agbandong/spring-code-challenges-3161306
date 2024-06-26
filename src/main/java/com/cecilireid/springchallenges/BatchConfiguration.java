package com.cecilireid.springchallenges;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

//todo: Note not working check how make work
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    //Note: use constructor injector
    //https://www.baeldung.com/java-spring-field-injection-cons
    //https://www.baeldung.com/spring-injection-lombok
    /*@Autowired
    public CateringJobRepository repository;*/

    private final CateringJobRepository cateringJobRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfiguration(CateringJobRepository cateringJobRepository, JobRepository jobRepository, PlatformTransactionManager transactionManager){
        this.cateringJobRepository = cateringJobRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

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
        reader.setResource(new ClassPathResource("upload.csv"));
        reader.setLinesToSkip(1);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        //Find way to automate set tokenizer
        tokenizer.setNames("id", "customerName", "phoneNumber", "email", "menu", "noOfGuests", "status");
        DefaultLineMapper<CateringJob> lineMapper = new DefaultLineMapper<>();
        //DelimitedLineTokenizer defaults to comma as its delimiter
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new CateringJobMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public RepositoryItemWriter<CateringJob> writer() {
        RepositoryItemWriter<CateringJob> writer = new RepositoryItemWriter<>();
        writer.setRepository(cateringJobRepository);
        return writer;
    }

    @Bean
    public Step step() {
        return new StepBuilder("step", jobRepository)
                .<CateringJob, CateringJob>chunk(10, transactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job uploadCateringJob() {
        return new JobBuilder("uploadCateringJob", jobRepository)
                .start(step())
                .build();
    }
}
