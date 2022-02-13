package com.nagesh.exam.validator;

import com.nagesh.exam.model.AccountValidationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class RequestValidatorTest {

    @InjectMocks
    private RequestValidator validator;

    @Test
    public void testRequestData(){
        AccountValidationRequest validationRequest = new AccountValidationRequest("12345",null);
        List<String> errors = validator.validate(validationRequest);
        Assertions.assertNotNull(errors);
        Assertions.assertTrue(errors.size() == 0);
    }

    @Test
    public void testRequestData_error(){
        AccountValidationRequest validationRequest = new AccountValidationRequest(null,null);
        List<String> errors = validator.validate(validationRequest);
        Assertions.assertNotNull(errors);
        Assertions.assertTrue(errors.size() == 1);
    }

    @Test
    public void testRequestData_null(){
        AccountValidationRequest validationRequest = null;
        List<String> errors = validator.validate(validationRequest);
        Assertions.assertNotNull(errors);
        Assertions.assertTrue(errors.size() == 1);
    }

}
