package com.adapter.springbatchxmladapter;

import java.util.List;
import java.util.Scanner;

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
		System.out.println("Application Running successfully>>>>>>>>>>");
		FileReadWriteUtil fileUtil = new FileReadWriteUtil();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter configuration file absolute path:");
		String jsonFilePath = scanner.nextLine();
		System.out.println("Enter XML URL:");
		String xmlUrl = scanner.nextLine();
		List<RequestModel> reqList = fileUtil.readJson(jsonFilePath);
		fileUtil.readXML(reqList,xmlUrl);
	}
}
