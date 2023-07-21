package au.com.test.assignment.addressbook.web.controller;


import au.com.test.assignment.addressbook.model.AddressBook;
import au.com.test.assignment.addressbook.model.Customer;
import au.com.test.assignment.addressbook.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @Operation(summary = "Create new Address Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Address Book created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressBook.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content)
    })
    @PostMapping("/api/v1/address-book/")
    public AddressBook createAddressBook( @Valid @RequestBody AddressBook addressBook) {
        return addressBookService.createAddressBook(addressBook);
    }

    @Operation(summary = "Get a Address Book by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved specified Address Book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressBook.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Address Book not found",
                    content = @Content) })
    @GetMapping("/api/v1/address-book/{addressBookId}")
    public AddressBook retrieveAddressBook(@PathVariable Long addressBookId) {
        return addressBookService.retrieveAddressBook(addressBookId);
    }

    @Operation(summary = "Create a new customer contact in Address Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added customer to specified Address Book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressBook.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content)
    })
    @PostMapping("/api/v1/address-book/{addressBookId}/customer")
    public Customer addContactToAddressBook(
            @PathVariable Long addressBookId, @Valid @RequestBody Customer customer) {

        return addressBookService.addCustomerToAddressBook(addressBookId, customer);
    }

    @Operation(summary = "Remove a customer contact from specified Address Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Removed customer from specified Address Book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressBook.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content)
    })

    @DeleteMapping("/api/v1/address-book/{addressBookId}/customer/{customerId}")
    public void removeCustomerFromAddressBook(@PathVariable Long addressBookId,
                                              @PathVariable Long customerId) {
        addressBookService.removeCustomerFromAddressBook(addressBookId, customerId);
    }

    @Operation(summary = "Retrieve all customers of specified Address Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All customers of the specified Address Book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressBook.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content)
    })
    @GetMapping("/api/v1/address-book/{addressBookId}/customers")
    public Set<Customer> retrieveCustomersFromAddressBook(@PathVariable Long addressBookId) {
        return addressBookService.retrieveCustomersOfAddressBook(addressBookId);
    }

    @Operation(summary = "Retrieves distinct customers from all Address Books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Distinct customers from all Address Book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressBook.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content)
    })
    @GetMapping("/api/v1/address-book/customers")
    public Set<Customer> retrieveDistinctCustomersFromAllAddressBooks() {
        return addressBookService.retrieveDistinctCustomersFromAllAddressBooks();
    }
}