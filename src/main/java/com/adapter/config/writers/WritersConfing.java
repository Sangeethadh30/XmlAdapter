package com.adapter.config.writers;

import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.adapter.model.ResponseTagModel;

@Configuration
public class WritersConfing {
	
	@Bean
	public JsonFileItemWriter<ResponseTagModel> jsonFileItemWriter(@Value("#{jobParameters['file']}") String file) {
	   return new JsonFileItemWriterBuilder<ResponseTagModel>()
	                 .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
	                 .resource(new ClassPathResource(file))
	                 .name("JsonFileItemWriter")
	                 .build();
	}

}
