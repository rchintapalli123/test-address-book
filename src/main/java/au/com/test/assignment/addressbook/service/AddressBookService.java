package au.com.test.assignment.addressbook.service;

import au.com.test.assignment.addressbook.exception.AddressBookException;
import au.com.test.assignment.addressbook.exception.AddressBookNotFoundException;
import au.com.test.assignment.addressbook.model.AddressBook;
import au.com.test.assignment.addressbook.model.Customer;
import au.com.test.assignment.addressbook.repository.AddressBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;

    public AddressBookService(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * Creates new AddressBook with specified details
     * @param addressBook to be created
     * @return  created AddressBook
     */
    public AddressBook createAddressBook(AddressBook addressBook) {
        try {
            return addressBookRepository.save(addressBook);
        } catch (Exception ex) {
            log.error("Error while creating address book", ex);
            throw new AddressBookException(ex.getMessage());
        }
    }

    /**
     * Adds Customer contact to  existing AddressBook
     * @param addressBookId id of the existing address book
     * @param customer customer contact to be added to existing address book
     * @return  Customer added to address book
     */
    public Customer addCustomerToAddressBook(Long addressBookId, Customer customer) {
        try {
            var addressBook = addressBookRepository.findById(addressBookId);

            if (addressBook.isPresent()) {
                var retrievedAddressBook = addressBook.get();
                retrievedAddressBook.getCustomers().add(customer);
                var updatedAddressBook = addressBookRepository.save(retrievedAddressBook);

                log.info("Customer successfully added to Address Book with id:{}", addressBookId);

                return updatedAddressBook.getCustomers()
                        .stream()
                        .filter(cust -> cust.equals(customer)).findFirst()
                        .orElse(null);
            } else {
                log.error("Address Book not found");
                throw new AddressBookNotFoundException("Address Book Not Found");
            }

        } catch(AddressBookNotFoundException addressBookNotFoundException) {
            throw addressBookNotFoundException;

        } catch (Exception ex) {
            log.error("Error while adding new customer to address book", ex);
            throw new AddressBookException(ex.getMessage());
        }
    }

    /**
     * Removes Customer contact from  existing AddressBook
     * @param addressBookId id of the existing address book
     * @param customerId the customer contactId to be removed from existing address book
     */
    public void removeCustomerFromAddressBook(Long addressBookId, final Long customerId) {
        try {
            var addressBook = addressBookRepository.findById(addressBookId);

            if (addressBook.isEmpty()) {
                throw new AddressBookNotFoundException("AddressBook not found");
            }

            addressBook.ifPresent(book -> {
                book.getCustomers()
                        .removeIf(addressBookCustomer -> customerId.equals(addressBookCustomer.getId()));

                addressBookRepository.save(book);
                log.info("Removed Customer with id:{} from AddressBook with id:{}", customerId, addressBookId);
            });

        } catch(AddressBookNotFoundException addressBookNotFoundException) {
            throw addressBookNotFoundException;

        } catch (Exception ex) {
            log.error("Error while removing customer from address book", ex);
            throw new AddressBookException(ex.getMessage());
        }
    }

    /**
     * Retrieves all Customers of specified AddressBook
     * @param addressBookId id of the existing address book
     * @return Set of customers in the address book
     */
    public Set<Customer> retrieveCustomersOfAddressBook(Long addressBookId) {
        try {
            var addressBook = addressBookRepository.findById(addressBookId);

            if (addressBook.isEmpty()) {
                throw new AddressBookNotFoundException("AddressBook not found");
            }

            return addressBook
                    .map(AddressBook::getCustomers)
                    .orElse(Collections.emptySet());

        } catch(AddressBookNotFoundException addressBookNotFoundException) {
            throw addressBookNotFoundException;

        } catch (Exception ex) {
            log.error("Error while retrieving all customer of address book", ex);
            throw new AddressBookException(ex.getMessage());
        }
    }

    /**
     * Retrieves specified AddressBook
     * @param addressBookId id of the address book to be retrieved
     * @return AddressBook requested
     */
    public AddressBook retrieveAddressBook(Long addressBookId) {
        try {
            return addressBookRepository.findById(addressBookId)
                    .orElseThrow(() -> new AddressBookNotFoundException("Address Book not found"));

        } catch(AddressBookNotFoundException addressBookNotFoundException) {
            throw addressBookNotFoundException;

        } catch (Exception ex) {
            log.error("Error while retrieving address book", ex);
            throw new AddressBookException(ex.getMessage());
        }
    }

    /**
     * Retrieves all AddressBooks from database and iterates through the list
     * of customers and removes duplicate customer contacts. A customer contact is
     * considered duplicate if customer firstname, customer lastname and customer phone number are same
     * @return Set of customers across AddressBooks
     */
    public Set<Customer> retrieveDistinctCustomersFromAllAddressBooks() {

        try {
            //get all address books from database
            var iterableAddressBooks = addressBookRepository.findAll();

            //convert the AddressBooks iterable into a list
            List<AddressBook> allAddressBooks = StreamSupport
                    .stream(iterableAddressBooks.spliterator(), false)
                    .toList();

            //stream the list and remove duplicate customer contacts.
            return  allAddressBooks
                    .stream()
                    .map(AddressBook::getCustomers)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());

        } catch(AddressBookNotFoundException addressBookNotFoundException) {
            throw addressBookNotFoundException;

        } catch (Exception ex) {
            log.error("Error while retrieving distinct customers of all address books", ex);
            throw new AddressBookException(ex.getMessage());
        }
    }
}
