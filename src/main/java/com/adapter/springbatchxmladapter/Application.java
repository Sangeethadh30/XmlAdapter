package com.adapter.springbatchxmladapter;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.adapter.model.RequestModel;
import com.adapter.util.FileReadWriteUtil;

/**  
* Application.java -  to launch the application  
* @author  Vikas Singh
* @version 1.0 
*/ 
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class Application {
	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("Running successfully>>>>>>>>>>");
		FileReadWriteUtil fileUtil = new FileReadWriteUtil();
		List<RequestModel> reqList = fileUtil.readJson();
		fileUtil.readAndExtractXML(reqList);
	}
}
