# spring-jdbc-tutorial

This tutorial code used to explain Spring JDBC concepts:

* add **spring-context** and **spring-jdbc** and **mysql-connector-java** dependencies
* add DAO model, repository interface and repo implementations
* add datasource bean definition with spring-resource loading configurations
* add bean for Spring wrappers over Java JDBC drivers same as **JdbcTemplate**, **NamedParameterJdbcTemplate**, **SimpleJdbcInsert**.
* test-case for search and insert new record with data access functionalities
* add CustomSqlErrorCode translator with Spring **SQLErrorCodeSQLExceptionTranslator** parent class.