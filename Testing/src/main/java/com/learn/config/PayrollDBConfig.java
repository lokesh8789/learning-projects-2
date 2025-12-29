package com.learn.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.learn.repo.payroll"},
        entityManagerFactoryRef = "payrollEntityManagerFactory",
        transactionManagerRef = "payrollTransactionManager")
public class PayrollDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "payroll.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("payrollDataSource")
    public DataSource payrollDataSource() {
        return dataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

//    @Bean("payrollJpaProperties")
//    @ConfigurationProperties(prefix = "payroll.jpa")
//    public JpaProperties payrollJpaProperties() {
//        return new JpaProperties();
//    }

    @Bean("payrollEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(EntityManagerFactoryBuilder builder,
             @Qualifier("payrollDataSource") DataSource dataSource) {
        //        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
//        hibernateJpaVendorAdapter.setGenerateDdl(true);
//        em.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        //em.setJpaProperties(hibernateProperties());
        return builder.dataSource(dataSource)
                .persistenceUnit("payroll-db")
                .properties(hibernateProperties())
                .packages("com.learn.entity.payroll")
                .build();
    }

    @Bean("payrollTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("payrollEntityManagerFactory") LocalContainerEntityManagerFactoryBean  entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        //jpaTransactionManager.setPersistenceUnitName("payroll-db");
        return jpaTransactionManager;
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.hbm2ddl.auto","update");
        return properties;
    }
}
