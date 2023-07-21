package au.com.test.assignment.addressbook;

import au.com.test.assignment.addressbook.model.AddressBook;
import au.com.test.assignment.addressbook.model.Customer;
import au.com.test.assignment.addressbook.repository.AddressBookRepository;
import au.com.test.assignment.addressbook.service.AddressBookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static au.com.test.assignment.addressbook.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class AddressBookIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private AddressBookService addressBookService;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    public void cleanUp() {
        addressBookRepository.deleteAll();
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/v1/address-book/";
    }

    @Test
    public void createsNewAddressBook() {
        AddressBook addressBook = AddressBook.builder().name("test-address-book").build();

        HttpEntity<String> entity = new HttpEntity<>(asJsonString(addressBook), headers);
        ResponseEntity<AddressBook> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.POST, entity, new ParameterizedTypeReference<>(){});
        AddressBook savedAddressBook = response.getBody();
        assertNotNull(savedAddressBook);
        assertEquals(response.getStatusCode().value(), 200);
        assertNotNull(addressBookRepository.findById(savedAddressBook.getId()));
    }

    @Test
    public void retrievesAddressBook() {
        AddressBook addressBook = AddressBook.builder().name("test-address-book").build();
        AddressBook savedAddressBook = addressBookRepository.save(addressBook);
        String requestUrl = createURLWithPort() + savedAddressBook.getId();

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<AddressBook> response = restTemplate.exchange(
                requestUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>(){});

        AddressBook retrievedAddressBook = response.getBody();
        assertNotNull(retrievedAddressBook);
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(retrievedAddressBook.getId(), savedAddressBook.getId());
    }

    @Test
    public void addsNewCustomerToAddressBook() {
        AddressBook addressBook = AddressBook.builder().name("test-address-book").build();
        AddressBook savedAddressBook = addressBookRepository.save(addressBook);
        String requestUrl = createURLWithPort() + savedAddressBook.getId() + "/customer";

        HttpEntity<String> entity = new HttpEntity<>(asJsonString(getTestCustomer()), headers);
        ResponseEntity<Customer> response = restTemplate.exchange(
                requestUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>(){});

        Customer savedCustomer = response.getBody();

        AddressBook book = addressBookService.retrieveAddressBook(savedAddressBook.getId());

        assertNotNull(savedCustomer);
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(book.getCustomers().size(), 1);
        assertEquals(book.getCustomers().stream().findFirst().get().getId(), savedCustomer.getId());
    }

    @Test
    public void removesCustomerFromAddressBook() {
        AddressBook addressBook = getTestAddressBook();

        AddressBook savedAddressBook = addressBookRepository.save(addressBook);

        Long customerId = savedAddressBook.getCustomers().stream().findFirst().get().getId();

        String requestUrl = createURLWithPort() + savedAddressBook.getId() + "/customer/" + customerId;

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                requestUrl, HttpMethod.DELETE, entity, new ParameterizedTypeReference<>(){});

        AddressBook retrieveAddressBook = addressBookService.retrieveAddressBook(savedAddressBook.getId());

        assertNotNull(retrieveAddressBook);
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(retrieveAddressBook.getCustomers().size(), 0);
    }

    @Test
    public void retrievesCustomersFromAddressBook() {
        AddressBook addressBook = getTestAddressBook();

        AddressBook savedAddressBook = addressBookRepository.save(addressBook);

        String requestUrl = createURLWithPort() + savedAddressBook.getId() + "/customers";

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Set<Customer>> response = restTemplate.exchange(
                requestUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>(){});

        Set<Customer> customers = response.getBody();

        assertNotNull(customers);
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(customers.size(), 1);
    }

    @Test
    public void retrievesDistinctCustomersFromAllAddressBook() {

        AddressBook addressBook1 = AddressBook
                .builder()
                .name("test-address-book1")
                .customers(Set.of(getTestCustomer()))
                .build();

        AddressBook addressBook2 = AddressBook
                .builder()
                .name("test-address-book2")
                .customers(Set.of(getTestCustomer()))
                .build();

        AddressBook addressBook3 = AddressBook
                .builder()
                .name("test-address-book3")
                .customers(Set.of(getTestCustomer()))
                .build();

        addressBookService.createAddressBook(addressBook1);
        addressBookService.createAddressBook(addressBook2);
        addressBookService.createAddressBook(addressBook3);

        String requestUrl = createURLWithPort()  + "customers";

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Set<Customer>> response = restTemplate.exchange(
                requestUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>(){});

        Set<Customer> customers = response.getBody();

        assertNotNull(customers);
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(customers.size(), 1);
    }
}

