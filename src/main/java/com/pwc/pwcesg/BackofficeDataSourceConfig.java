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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(value = "com.pwc.pwcesg",
        sqlSessionFactoryRef = "backofficeSqlSessionFactory")
public class BackofficeDataSourceConfig {

    @Primary
    @Bean(name = "backofficeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.backoffice")
    public DataSource backofficeDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "backofficeSqlSessionFactory")
    public SqlSessionFactory backofficeSqlSessionFactory(DataSource backofficeDataSource, ApplicationContext applicationContext) throws Exception{
    	final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(backofficeDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/**/*Mapper.xml"));
        sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        return sessionFactory.getObject();
    }

    @Primary
    @Bean(name = "backofficeSqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("backofficeSqlSessionFactory")SqlSessionFactory backofficeSqlSessionFactory){
		return new SqlSessionTemplate(backofficeSqlSessionFactory);
	}

    @Primary
    @Bean(name = "backofficeTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("backofficeDataSource") DataSource backofficeDataSource) {
        return new DataSourceTransactionManager(backofficeDataSource);
    }
}