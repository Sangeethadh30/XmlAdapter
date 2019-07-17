package com.adapter.config.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.json.JsonItemReader;

import com.adapter.config.readers.ReadersConfig;
import com.adapter.config.writers.WritersConfing;
import com.adapter.model.RequestModel;
import com.adapter.model.ResponseTagModel;
import com.adapter.notification.JobCompletionNotificationListener;
import com.adapter.process.XmlProcessor;

/**  
* BatchConfiguration.java - Configuration class for batch jobs 
* @author  Vikas Singh
* @version 1.0 
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
	 
	 @Bean
	    public XmlProcessor processor() {
	        return new XmlProcessor();
	    }
	 @Bean
	    public JobExecutionListener listener() {
	        return new JobCompletionNotificationListener();
	    }
	 
	 @Bean
	    public Job extraxtXML() {
	        return jobBuilderFactory.get("extraxtXML")
	        		.listener(listener())
	                .start(step(null))
	                .build();
	    }
	 
	 @Bean
	    public Step step(JsonItemReader<RequestModel> reader) {
	        return stepBuilderFactory.get("step")
	                .<RequestModel, ResponseTagModel>chunk(10)
	                .reader(readers.jsonFileItemReader())
	                .processor(processor())
	                .writer(writers.jsonFileItemWriter("output"))
	                .build();
	    }

}
