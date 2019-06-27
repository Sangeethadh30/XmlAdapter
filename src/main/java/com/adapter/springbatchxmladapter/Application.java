package com.adapter.springbatchxmladapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.adapter.util.FileReadWriteUtil;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class Application {
	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("Running successfully>>>>>>>>>>");
		FileReadWriteUtil fileUtil = new FileReadWriteUtil();
		fileUtil.readJson();
		fileUtil.readXMl();
	}
}
