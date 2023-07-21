package au.com.test.assignment.addressbook.web.controller.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiError {
    private Integer statusCode;
    private String message;
}
