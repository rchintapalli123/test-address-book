package au.com.test.assignment.addressbook.web.controller.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ApiResponse {

    private ApiError error;

    public ApiResponse(ApiError apiError) {
        this.error = apiError;
    }
}
