package com.pwc.pwcesg;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(value = "com.pwc.pwcesg",
        sqlSessionFactoryRef = "frontofficeSqlSessionFactory")
public class FrontofficeDataSourceConfig {
 
    @Bean(name = "frontofficeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.frontoffice")
    public DataSource frontofficeDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "frontofficeSqlSessionFactory")
    public SqlSessionFactory frontofficeSqlSessionFactory(@Qualifier("frontofficeDataSource") DataSource frontofficeDataSource, ApplicationContext applicationContext) throws Exception{
    	final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(frontofficeDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/**/*Mapper.xml"));
        sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        return sessionFactory.getObject();
    }
    
    @Bean(name = "frontofficeSqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("frontofficeSqlSessionFactory")SqlSessionFactory frontofficeSqlSessionFactory){
		return new SqlSessionTemplate(frontofficeSqlSessionFactory);
	}
	
    @Bean(name = "frontofficeTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("frontofficeDataSource") DataSource frontofficeDataSource) {
        return new DataSourceTransactionManager(frontofficeDataSource);
    }
}