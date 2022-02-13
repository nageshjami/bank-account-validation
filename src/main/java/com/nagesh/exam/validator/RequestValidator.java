package com.nagesh.exam.validator;

import com.nagesh.exam.model.AccountValidationRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RequestValidator {

    public List<String> validate(AccountValidationRequest request){
        List<String> errors = new ArrayList<>();
        if(Objects.isNull(request)){
            errors.add("Request body is not present");
        }
        if(Objects.nonNull(request) && Objects.isNull(request.getAccountNumber())){
            errors.add("Mandatory Account Number is missing in request");
        }
        return errors;
    }
}
