package ir.seefa.tutorial.spring.config;

import ir.seefa.tutorial.spring.exception.CustomSqlErrorCodeTranslator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author Saman Delfani
 * @version 1.0
 * @since 09 Sep 2020 T 23:54:03
 */
// 1. Read spring-core-tutorial before starting this project because primary annotations explained there
@Configuration
@ComponentScan(basePackages = "ir.seefa.tutorial.spring")
@PropertySource(value = "application.properties", ignoreResourceNotFound = true)
public class ApplicationConfiguration {

    // 2. Read database configuration from application.properties file(Driver classname, JDBC Url, user/pass)
    // 3. change user/pass and datasource configuration based on your env features
    @Value("${jdbc.driver.class}")
    private String jdbcClassDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    // 4. add dataSource as bean for data access layer
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(jdbcClassDriver);
        ds.setUrl(jdbcUrl);
        ds.setUsername(jdbcUsername);
        ds.setPassword(jdbcPassword);

        return ds;
    }

    // 5. singleton bean to access Spring jdbcTemplate wrapper for data access layer usage
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(dataSource());
        template.setExceptionTranslator(new CustomSqlErrorCodeTranslator());
        return template;
    }

    // 6. A bean to access Named-Parameter Spring solutions in data access layer
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    // 7.  A bean to access SimpleJdbcInsert Spring solutions in data access layer for DML wrapper
    @Bean
    public SimpleJdbcInsert simpleJdbcInsert() {
        SimpleJdbcInsert template = new SimpleJdbcInsert(dataSource());
        return template;
    }

}
