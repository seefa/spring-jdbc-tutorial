package ir.seefa.tutorial.spring;

import ir.seefa.tutorial.spring.config.ApplicationConfiguration;
import ir.seefa.tutorial.spring.entity.Contact;
import ir.seefa.tutorial.spring.repository.ContactDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * @author Saman Delfani
 * @version 1.0
 * @since 10 Sep 2020 T 00:34:19
 */
// 1. Read spring-core-tutorial before starting this project because primary annotations and logic explained there
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        // 2. Java-based solution to test data access functionalities
        ContactDao contactDao = (ContactDao) context.getBean("contactDaoImpl");

        // 3. load contact info from Database and iterate if list has more than one item at least
        List<Contact> contacts = contactDao.findAll();
        System.out.println("Contact List Size: " + contacts.size());
        if(contacts.size() > 0) {
            contacts.forEach(c -> {
                System.out.println(c.toString());
            });
        }

        // 4. fetch contact information with contact_id number one
        Contact contact = contactDao.findById(1);
        if (contact != null) {
            System.out.println("Contact ID is correct and contact info is => " + contact.toString());
        } else {
            System.out.println("Contact ID doesn't exist.");
        }

        // 5. search contact by  name and family
        List<Contact> searchContactByName = contactDao.findByNameAndFamily("Sam", "Delfani");
        System.out.println("Search Contact by name and family List Size: " + searchContactByName.size());

        // 6. search contact list with phone number
        List<Contact> searchContactByPhone = contactDao.findByPhone("09137390431");
        System.out.println("Search Contact by phone List Size: " + searchContactByPhone.size());

        // 7. add new contact with .query() function
        Contact newContact = new Contact();
        newContact.setContactId(2);
        newContact.setName("Hamed");
        newContact.setFamily("Delfani");
        newContact.setBirthday(new Date(Calendar.getInstance().getTime().getTime()));
        newContact.setPhone("02188453089");
        boolean addNewContactResult = contactDao.addContact(newContact);
        if (addNewContactResult) {
            System.out.println("Contact is added successfully.");
        } else {
            System.out.println("Add contact failed.");
        }

        // 8. search contact with named-parameter function
        List<Contact> searchContactsByPhoneWithNamedParameter = contactDao.findByPhoneWithNamedParameters("02188453089");
        System.out.println("Search Contact by phone List Size: " + searchContactsByPhoneWithNamedParameter.size());
        if(searchContactsByPhoneWithNamedParameter.size() > 0) {
            searchContactsByPhoneWithNamedParameter.forEach(c -> {
                System.out.println(c.toString());
            });
        }

        // 9. add new contact with SimpleJdbcInsert function
        Contact newContact2 = new Contact();
        newContact2.setContactId((int)(Math.random() * (1000 - 100 + 1) + 100));
        newContact2.setName("Hamid");
        newContact2.setFamily("Delfani");
        newContact2.setBirthday(new Date(Calendar.getInstance().getTime().getTime()));
        newContact2.setPhone("02188453089");

//        int addNewContactRowNumber = contactDao.addContactWithSimpleJdbcInsert(newContact2);
//        if (addNewContactRowNumber > 0) {
//            System.out.println("Contact is added successfully.");
//        } else {
//            System.out.println("Add contact failed.");
//        }

        int addNewContactId = contactDao.addContactWithSimpleJdbcInsertReturnNewContactId(newContact2);
        if (addNewContactId > 0) {
            System.out.println("Contact is added successfully. New added contact ID= " + addNewContactId);
        } else {
            System.out.println("Add contact failed.");
        }

    }
}
