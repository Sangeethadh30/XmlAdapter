package com.adapter.config.readers;

import java.net.MalformedURLException;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.w3c.dom.Document;

import com.adapter.model.RequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
@PropertySource("classpath:application.properties")
public class ReadersConfig {
	
	@Value("${import.input.json}")
	private String jsonFile;
	
	/**
     * XML reader and parser.Uses Jaxb2Marshaller for map the values>
     * Its have to configure the model class to use it with JAXB
     * @return StaxEventItemReader Configured reader
     */
     /*@Bean
     public StaxEventItemReader<Document> readerXml()
     {
    	 StaxEventItemReader<Document> xmlFileReader = new StaxEventItemReader();
    	 
    	 try {
			xmlFileReader.setResource(new UrlResource("xmlUrl"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
         xmlFileReader.setFragmentRootElementName("document");
  
         Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
         xmlFileReader.setUnmarshaller(studentMarshaller);
    	 
    	 return xmlFileReader;	
    	 
     }*/
     
     /**
      * JSON reader 
      * @return JsonItemReader Configured reader
      */
	
	@Bean(destroyMethod="")
     public JsonItemReader<RequestModel> jsonFileItemReader(){
		try {
    	 ObjectMapper objectMapper = new ObjectMapper();
    	 JacksonJsonObjectReader<RequestModel> jsonObjectReader = 
    	            new JacksonJsonObjectReader<>(RequestModel.class);
    	 jsonObjectReader.setMapper(objectMapper);	
    	 System.out.println("entering reader");
		return new JsonItemReaderBuilder<RequestModel>()
			         .jsonObjectReader(jsonObjectReader)
			         .resource(new UrlResource("C:\\Sangeetha\\XMLJob\\XMLAdapter\\XmlAdapter\\src\\main\\resources\\input1.json"))
			         .name("jsonFileItemReader").build();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
     }
     
}
