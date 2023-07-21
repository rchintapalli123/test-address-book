package au.com.test.assignment.addressbook.exception;

public class AddressBookNotFoundException extends RuntimeException {
    public AddressBookNotFoundException(String s) {
        super(s);
    }
}

