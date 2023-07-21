package au.com.test.assignment.addressbook.utils;

import au.com.test.assignment.addressbook.model.AddressBook;
import au.com.test.assignment.addressbook.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestUtils {

   public static Customer getTestCustomer() {
        return Customer.builder()
                .firstName("test")
                .lastName("testing")
                .phoneNumber("123")
                .build();
    }
    public static Set<Customer> getTestCustomers() {
        return new HashSet<>(Collections.singletonList(
                Customer.builder()
                        .firstName("test2")
                        .lastName("testing2")
                        .phoneNumber("456")
                        .build()));
    }

    public static AddressBook getTestAddressBook() {
        return AddressBook.builder()
                .name("test-address-book")
                .customers(getTestCustomers())
                .build();

    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return "";
        }
    }
}
