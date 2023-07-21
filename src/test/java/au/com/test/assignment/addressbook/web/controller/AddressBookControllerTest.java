package au.com.test.assignment.addressbook.web.controller;

import au.com.test.assignment.addressbook.exception.AddressBookNotFoundException;
import au.com.test.assignment.addressbook.model.AddressBook;
import au.com.test.assignment.addressbook.model.Customer;
import au.com.test.assignment.addressbook.service.AddressBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static au.com.test.assignment.addressbook.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressBookController.class)
public class AddressBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressBookService addressBookService;

    @Test
    public void createsNewAddressBook() throws Exception {
        AddressBook addressBook = getTestAddressBook();
        when(addressBookService.createAddressBook(any(AddressBook.class))).thenReturn(addressBook);

        this.mockMvc.perform(
                post("/api/v1/address-book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addressBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(addressBook.getName()));

        verify(addressBookService, times(1)).createAddressBook(any(AddressBook.class));
    }

    @Test
    public void returnsInvalidRequestWhenAddressBookNameIsNotProvided() throws Exception {
        AddressBook addressBook = getTestAddressBook();
        addressBook.setName(null);
        when(addressBookService.createAddressBook(any(AddressBook.class))).thenReturn(addressBook);

        this.mockMvc.perform(
                        post("/api/v1/address-book/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(addressBook)))
                .andExpect(status().isBadRequest());

        verify(addressBookService, times(0)).createAddressBook(any(AddressBook.class));
    }

    @Test
    public void returnsInvalidRequestWhenAddressBookIsNotProvided() throws Exception {
        when(addressBookService.createAddressBook(any(AddressBook.class))).thenReturn(getTestAddressBook());

        this.mockMvc.perform(
                        post("/api/v1/address-book/")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(addressBookService, times(0)).createAddressBook(any(AddressBook.class));
    }

    @Test
    public void retrievesAddressBook() throws Exception {
        AddressBook addressBook = getTestAddressBook();
        when(addressBookService.retrieveAddressBook(any(Long.class))).thenReturn(addressBook);

        this.mockMvc.perform(
                        get("/api/v1/address-book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(addressBook.getName()));

        verify(addressBookService, times(1)).retrieveAddressBook(1L);
    }

    @Test
    public void returnsNotFoundWhenAddressBookIsNotValid() throws Exception {
        when(addressBookService.retrieveAddressBook(any(Long.class))).thenThrow(AddressBookNotFoundException.class);

        this.mockMvc.perform(
                        get("/api/v1/address-book/1"))
                .andExpect(status().isNotFound());

        verify(addressBookService, times(1)).retrieveAddressBook(1L);
    }

    @Test
    public void addsContactToAddressBook() throws Exception {
        Customer customer = getTestCustomer();
        when(addressBookService.addCustomerToAddressBook(any(Long.class), any(Customer.class)))
                .thenReturn(customer);

        this.mockMvc.perform(
                        post("/api/v1/address-book/1/customer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()));

        verify(addressBookService, times(1)).addCustomerToAddressBook(1L, customer);
    }

    @Test
    public void returnsBadRequestWhenSuppliedAddressBookForAddingCustomersDoesNotExist() throws Exception {
        Customer customer = getTestCustomer();
        when(addressBookService.addCustomerToAddressBook(any(Long.class), any(Customer.class)))
                .thenThrow(AddressBookNotFoundException.class);

        this.mockMvc.perform(
                        post("/api/v1/address-book/1/customer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(customer)))
                .andExpect(status().isNotFound());

        verify(addressBookService, times(1)).addCustomerToAddressBook(1L, customer);
    }

    @Test
    public void removesContactFromAddressBook() throws Exception {

        this.mockMvc.perform(
                        delete("/api/v1/address-book/1/customer/1"))
                .andExpect(status().isOk());

        verify(addressBookService, times(1)).removeCustomerFromAddressBook(1L, 1L);
    }

    @Test
    public void retrievesCustomersOfSpecifiedAddressBook() throws Exception {
        Set<Customer> customers = Set.of(getTestCustomer());

        when(addressBookService.retrieveCustomersOfAddressBook(any(Long.class))).thenReturn(customers);

        this.mockMvc.perform(
                        get("/api/v1/address-book/1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].firstName").value(getTestCustomer().getFirstName()));

        verify(addressBookService, times(1)).retrieveCustomersOfAddressBook(1L);
    }

    @Test
    public void retrievesDistinctCustomersAcrossAddressBooks() throws Exception {
        Set<Customer> customers = getTestCustomers();

        when(addressBookService.retrieveDistinctCustomersFromAllAddressBooks()).thenReturn(customers);

        this.mockMvc.perform(
                        get("/api/v1/address-book/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(addressBookService, times(1)).retrieveDistinctCustomersFromAllAddressBooks();
    }
}
