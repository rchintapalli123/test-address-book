package au.com.test.assignment.addressbook.service;

import au.com.test.assignment.addressbook.model.AddressBook;
import au.com.test.assignment.addressbook.model.Customer;
import au.com.test.assignment.addressbook.repository.AddressBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static au.com.test.assignment.addressbook.utils.TestUtils.getTestAddressBook;
import static au.com.test.assignment.addressbook.utils.TestUtils.getTestCustomer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {

    @Mock
    private AddressBookRepository addressBookRepository;

    @InjectMocks
    AddressBookService addressBookService;

    @Test
    public void createsAddressBook() {
        AddressBook addressBook = getTestAddressBook();
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(getTestAddressBook());

        addressBookService.createAddressBook(addressBook);

        verify(addressBookRepository, times(1)).save(addressBook);
    }

    @Test
    public void addCustomerToAddressBook() {
        when(addressBookRepository.findById(any(Long.class))).thenReturn(Optional.of(getTestAddressBook()));
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(getTestAddressBook());

        addressBookService.addCustomerToAddressBook(1L, getTestCustomer());

        verify(addressBookRepository, times(1)).findById(1L);
        verify(addressBookRepository, times(1)).save(any(AddressBook.class));
    }

    @Test
    public void removeCustomerFromAddressBook() {
        AddressBook addressBook = getTestAddressBook();
        Customer customer = addressBook.getCustomers().stream().findFirst().get();
        customer.setId(1L);
        addressBook.setId(1L);

        when(addressBookRepository.findById(any(Long.class))).thenReturn(Optional.of(addressBook));
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(addressBook);

        addressBookService.removeCustomerFromAddressBook(1L, 1L);

        verify(addressBookRepository, times(1)).findById(1L);
        verify(addressBookRepository, times(1)).save(any(AddressBook.class));
    }

    @Test
    public void retrieveCustomersOfAddressBook() {
        when(addressBookRepository.findById(any(Long.class))).thenReturn(Optional.of(getTestAddressBook()));

        addressBookService.retrieveCustomersOfAddressBook(1L);

        verify(addressBookRepository, times(1)).findById(1L);
    }

    @Test
    public void retrieveAddressBook() {
        when(addressBookRepository.findById(any(Long.class))).thenReturn(Optional.of(getTestAddressBook()));

        addressBookService.retrieveAddressBook(1L);

        verify(addressBookRepository, times(1)).findById(1L);
    }

    @Test
    public void retrieveDistinctCustomersFromAllAddressBooks() {

        addressBookService.retrieveDistinctCustomersFromAllAddressBooks();

        verify(addressBookRepository, times(1)).findAll();

    }
}
