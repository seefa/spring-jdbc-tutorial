package ir.seefa.tutorial.spring.repository;

import ir.seefa.tutorial.spring.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Saman Delfani
 * @version 1.0
 * @since 10 Sep 2020 T 00:14:25
 */
// 1. Repository bean for implementation of data access services
@Repository
public class ContactDaoImpl implements ContactDao {

    // 2. wiring repository bean to data access prerequisites
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    SimpleJdbcInsert simpleJdbcInsert2;

    // 3. implementing data access functions with different Spring JDBC wrapper solutions
    // 4. using .query() method for run pure SQL commands
    public List<Contact> findAll() {
        String sql = "select * from Contact c";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    public Contact findById(int contactId) {
        String sql = "select * from Contact c where c.contact_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{contactId}, new ContactRowMapper());
    }

    public List<Contact> findByNameAndFamily(String name, String family) {
        String sql = "select * from Contact c where c.name=? and c.family=?";
        return jdbcTemplate.query(sql, new Object[]{name, family}, new ContactRowMapper());
    }

    public List<Contact> findByPhone(String phone) {
        String sql = "select * from Contact c where c.phone=?";
        return jdbcTemplate.query(sql, new Object[]{phone}, new ContactRowMapper());
    }

    // 5. using named-parameters to bind data and query information
    public List<Contact> findByPhoneWithNamedParameters(String phone) {
        String sql = "select * from Contact c where c.phone=:phone";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("phone", phone);
        return namedParameterJdbcTemplate.query(sql, namedParameters, new ContactRowMapper());
    }

    public boolean addContact(Contact contact) {
        String sql = "insert into contact values(?,?,?,?,?)";
        try {
            int insertResult = jdbcTemplate.update(sql, contact.getContactId(), contact.getName(), contact.getFamily(), contact.getBirthday(), contact.getPhone());
            return insertResult > 0;
        } catch (Exception e) {
            System.out.println("Exception raised. " + e.getMessage());
            return false;
        }
    }

    private Map<String, Object> getMapForSimpleJdbcInsert(Contact contact) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("contact_id", contact.getContactId());
        parameters.put("name", contact.getName());
        parameters.put("family", contact.getFamily());
        parameters.put("birthday", contact.getBirthday());
        parameters.put("phone", contact.getPhone());
        return parameters;
    }

    // 6. using .execute() method from SimpleJdbcInsert Spring data access wrapper
    public int addContactWithSimpleJdbcInsert(Contact contact) {
        simpleJdbcInsert.withTableName("contact");
        return simpleJdbcInsert.execute(getMapForSimpleJdbcInsert(contact));
    }

    public int addContactWithSimpleJdbcInsertReturnNewContactId(Contact contact) {
        simpleJdbcInsert2.withTableName("contact").usingColumns("name", "family", "birthday", "phone").usingGeneratedKeyColumns("contact_id");
        return simpleJdbcInsert2.executeAndReturnKey(getMapForSimpleJdbcInsert(contact)).intValue();
    }
}

// 7. define Custom RowMapper object for convert database info to Java objects
class ContactRowMapper implements RowMapper<Contact> {

    public Contact mapRow(ResultSet rs, int i) throws SQLException {
        Contact contact = new Contact();
        contact.setContactId(rs.getInt("contact_id"));
        contact.setName(rs.getString("name"));
        contact.setFamily(rs.getString("family"));
        contact.setBirthday(rs.getDate("birthday"));
        contact.setPhone(rs.getString("phone"));

        return contact;
    }
}
