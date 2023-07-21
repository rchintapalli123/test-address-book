package au.com.test.assignment.addressbook.repository;

import au.com.test.assignment.addressbook.model.AddressBook;
import org.springframework.data.repository.CrudRepository;

public interface AddressBookRepository extends CrudRepository<AddressBook, Long> {

}
