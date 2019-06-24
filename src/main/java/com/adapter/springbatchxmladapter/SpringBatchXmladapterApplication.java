package com.adapter.springbatchxmladapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SpringBatchXmladapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchXmladapterApplication.class, args);
		System.out.println("Running successfully>>>>>>>>>>");
	}
}
