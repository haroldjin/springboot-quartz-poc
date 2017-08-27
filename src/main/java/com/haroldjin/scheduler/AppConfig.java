package com.haroldjin.scheduler;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig implements ApplicationContextAware{

	private static ApplicationContext ctx;

	// data source
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	// org.mybatis.spring.mapper.MapperScannerConfigurer
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer msc =  new MapperScannerConfigurer();
		msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
		msc.setBasePackage("com.haroldjin.scheduler.dao");
		return msc;
	}

	// org.mybatis.spring.SqlSessionFactoryBean
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource)
			throws IOException {
		SqlSessionFactoryBean sqsfb = new SqlSessionFactoryBean();
		Resource mybatis = ctx.getResource("classpath:config/MyBatisConfiguration.xml");
		Resource[] mapperLocList = (ctx.getResources("classpath:mappers/**/*.xml"));
		sqsfb.setMapperLocations(mapperLocList);
		sqsfb.setConfigLocation(mybatis);
		sqsfb.setDataSource(dataSource);
		return sqsfb;
	}

	// org.springframework.scheduling.quartz.SchedulerFactoryBean
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		return new SchedulerFactoryBean();
	}
	
	// org.springframework.jdbc.datasource.DataSourceTransactionManager
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource){
		DataSourceTransactionManager dstm = new DataSourceTransactionManager();
		dstm.setDataSource(dataSource);
		return dstm;
	}

	// Convert http message to json
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonConverter.setObjectMapper(objectMapper);
		return jsonConverter;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;  
	}
}
