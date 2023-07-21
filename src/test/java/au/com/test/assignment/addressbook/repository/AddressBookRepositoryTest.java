package au.com.test.assignment.addressbook.repository;

import au.com.test.assignment.addressbook.model.AddressBook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static au.com.test.assignment.addressbook.utils.TestUtils.getTestAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AddressBookRepositoryTest {

    @Autowired
    AddressBookRepository addressBookRepository;


    @Test
    public void savesNewAddressBook() {
        AddressBook addressBook = AddressBook.builder().name("test-address-book2").build();

        AddressBook savedAddressBook = addressBookRepository.save(addressBook);
        AddressBook retrieveAddressBook = addressBookRepository.findById(savedAddressBook.getId()).get();

        assertNotNull(savedAddressBook.getId());
        assertNotNull(savedAddressBook.getId());
        assertNotNull(retrieveAddressBook);
        assertEquals(savedAddressBook.getId(), retrieveAddressBook.getId());
    }

    @Test
    public void retrievesAddressBookById() {
        AddressBook savedAddressBook = addressBookRepository.save(getTestAddressBook());
        AddressBook addressBook = addressBookRepository.findById(savedAddressBook.getId()).get();
        assertNotNull(addressBook);
        assertEquals(addressBook.getId(), savedAddressBook.getId());
    }


    @AfterEach
    public void destroy() {
        addressBookRepository.deleteAll();
    }

}
