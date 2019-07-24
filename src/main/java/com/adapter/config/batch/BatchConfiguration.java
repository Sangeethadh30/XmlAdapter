package com.adapter.config.batch;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.adapter.config.readers.ReadersConfig;
import com.adapter.config.writers.WritersConfing;
import com.adapter.model.RequestModel;
import com.adapter.model.ResponseTagModel;
import com.adapter.notification.JobCompletionNotificationListener;
import com.adapter.process.XmlProcessor;


/**
 * 
 * @author shoe011
 * Configuration class for batch jobs and default datasource
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private ReadersConfig readers;

    @Autowired
    private WritersConfing writers;
    
    @Resource(name="dsDestino")
    private DataSource dsDestino;
    
    /**
     * Configure default datasource
     * @param dataSource
     * @return
     */
    @Bean
    BatchConfigurer configurer(DataSource dataSource){
      return new DefaultBatchConfigurer(dsDestino);
    }
    
    @Bean
    public XmlProcessor processor() {
        return new XmlProcessor();
    }
   
   
    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<RequestModel, ResponseTagModel> chunk(10)
                .reader(readers.jsonFileItemReader())
                .processor(processor())
                .writer(writers.jsonFileItemWriter())
                .build();
    }
   
}
