package com.ducph.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.ducph")
@PropertySource({"classpath:persistence-mysql.properties", "classpath:security-persistence-mysql.properties"})
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    // set up a logger for diagnostic
    private Logger logger = Logger.getLogger(getClass().getName());

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public DataSource myDataSource() {
        // create connection pool
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        // set jdbc driver
        try {
            dataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        // log db user and pass info
        logger.info(">>>jdbc.url=" + env.getProperty("jdbc.url"));
        logger.info(">>>jdbc.user=" + env.getProperty("jdbc.user"));

        // set db connection properties
        dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        dataSource.setUser(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        // set connection pool propeties
        dataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
        dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
        dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
        dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));

        return dataSource;
    }

    private Properties getHibernateProperties() {
        // set hibernate properties
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        // create session factory
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        // set the properties
        sessionFactory.setDataSource(myDataSource());
        sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }

    // define a bean for security data source
    @Bean
    public DataSource securityDataSource() {

        // create connection pool
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        // set the jdbc driver class
        try {

            securityDataSource.setDriverClass(env.getProperty("security.jdbc.driver"));
        } catch (PropertyVetoException exc) {
            throw new RuntimeException(exc);
        }

        // log the connection props
        // for sanity's sake, log this info
        // just to make sure we are REALLY reading data from properties file

        logger.info(">>> security.jdbc.url=" + env.getProperty("security.jdbc.url"));
        logger.info(">>> security.jdbc.user=" + env.getProperty("security.jdbc.user"));


        // set database connection props

        securityDataSource.setJdbcUrl(env.getProperty("security.jdbc.url"));
        securityDataSource.setUser(env.getProperty("security.jdbc.user"));

        securityDataSource.setPassword(env.getProperty("security.jdbc.password"));

        // set connection pool props

        securityDataSource.setInitialPoolSize(
                Integer.parseInt(env.getProperty("security.connection.pool.initialPoolSize")));
        securityDataSource.setMinPoolSize(
                Integer.parseInt(env.getProperty("security.connection.pool.minPoolSize")));
        securityDataSource.setMaxPoolSize(
                Integer.parseInt(env.getProperty("security.connection.pool.maxPoolSize")));
        securityDataSource.setMaxIdleTime(
                Integer.parseInt(env.getProperty("security.connection.pool.maxIdleTime")));

        return securityDataSource;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        // set up transaction manager based on session factory
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }
}
