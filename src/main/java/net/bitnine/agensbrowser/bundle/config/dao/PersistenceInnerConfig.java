//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.config.dao;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({"net.bitnine.agensbrowser.bundle.persistence.inner"})
@EnableJpaRepositories(
        entityManagerFactoryRef = "innerEntityManagerFactory",
        transactionManagerRef = "innerTransactionManager",
        basePackages = {"net.bitnine.agensbrowser.bundle.persistence.inner.dao"}
)
public class PersistenceInnerConfig {
    @Autowired
    private Environment env;

    public PersistenceInnerConfig() {
    }

    @Primary
    @Bean(
            name = {"innerEntityManagerFactory"}
    )
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(this.dataSource());
        em.setPackagesToScan(new String[]{"net.bitnine.agensbrowser.bundle.persistence.inner.model"});
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(this.additionalProperties());
        return em;
    }

    @Primary
    @Bean(
            name = {"innerDataSource"}
    )
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.env.getProperty("agens.inner.datasource.driverClassName"));
        dataSource.setUrl(this.env.getProperty("agens.inner.datasource.url"));
        dataSource.setUsername(this.env.getProperty("agens.inner.datasource.username"));
        dataSource.setPassword(this.env.getProperty("agens.inner.datasource.password"));
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("/inner-h2.sql"));
        populator.setContinueOnError(true);
        DatabasePopulatorUtils.execute(populator, dataSource);
        return dataSource;
    }

    @Primary
    @Bean(
            name = {"innerTransactionManager"}
    )
    public PlatformTransactionManager transactionManager(@Qualifier("innerEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Primary
    @Bean(
            name = {"innerExceptionTranslation"}
    )
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    final Properties additionalProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "false");
        return hibernateProperties;
    }
}
