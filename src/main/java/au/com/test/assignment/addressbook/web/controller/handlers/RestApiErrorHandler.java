package au.com.test.assignment.addressbook.web.controller.handlers;

import au.com.test.assignment.addressbook.exception.AddressBookException;
import au.com.test.assignment.addressbook.exception.AddressBookNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestApiErrorHandler {

    @ResponseBody
    @ExceptionHandler(AddressBookException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleAddressBookException(AddressBookException ex) {
        return new ApiResponse(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(AddressBookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleAddressBookNotFoundException(AddressBookNotFoundException ex) {
        return new ApiResponse(new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleException(RuntimeException ex) {
        return new ApiResponse(new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request"));
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List<String>>> validationExceptionHandler(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage).toList();
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
