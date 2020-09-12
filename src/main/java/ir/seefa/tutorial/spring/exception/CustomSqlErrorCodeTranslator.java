package ir.seefa.tutorial.spring.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import java.sql.SQLException;

/**
 * @author Saman Delfani
 * @version 1.0
 * @since 10 Sep 2020 T 11:49:26
 */
// 1. Customize specific error code in Spring data access layer
public class CustomSqlErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {

    @Override
    protected DataAccessException customTranslate(String task, String sql, SQLException sqlEx) {
        if(sqlEx.getErrorCode() == 1062) {
            return new DuplicateKeyException("Custom SQL Error code translator. Duplicate exception raised", sqlEx);
        }
        return super.customTranslate(task, sql, sqlEx);
    }
}
