package com.adapter.config.batch;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * 
 * @author shoe011
 * DataBases configuration class
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
public class DbConfig 
{
	
	@Value("${db.mem.script}")
	private String scriptInMem;
	
	/**
	 * Datasource for write process
	 * @return Datasource configured datasource
	 */
	@Bean(name="dsDestino")
    @Qualifier("dsDestino")
    @Primary
	public DataSource dataSourceDestino() 
	{	
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.HSQL) //.H2 or .DERBY
			.addScript(scriptInMem)
			.build();
		return db;
	}
}
