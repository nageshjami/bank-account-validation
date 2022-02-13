package com.nagesh.exam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagesh.exam.model.AccountValidationRequest;
import com.nagesh.exam.model.AccountValidationResponse;
import com.nagesh.exam.model.SourceRequest;
import com.nagesh.exam.model.SourceResponse;
import com.nagesh.exam.service.AccountVerificationService;
import com.nagesh.exam.service.SourceService;
import com.nagesh.exam.validator.RequestValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class AccountValidationControllerTest {
    @InjectMocks
    AccountValidationController validationController;

    @InjectMocks
    AccountVerificationService accountVerificationService;

    @InjectMocks
    SourceService sourceService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ObjectMapper mapper;

    @Mock
    RequestValidator validator;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(accountVerificationService,"source1URL","https://source1.com/v1/api/account/validate");
        ReflectionTestUtils.setField(accountVerificationService,"source2URL","https://source2.com/v2/api/account/validate");
        ReflectionTestUtils.setField(accountVerificationService,"service",sourceService);
        ReflectionTestUtils.setField(validationController,"service",accountVerificationService);
    }

    @Test
    public void processRequest() throws Exception {
        AccountValidationRequest validationRequest = new AccountValidationRequest("12345",null);
        Mockito.when(validator.validate(validationRequest)).thenReturn(new ArrayList<>());
        AccountValidationResponse response = validationController.process(validationRequest);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getResult());
        Assertions.assertEquals(2,response.getResult().size());
    }

    @Test
    public void processRequest_source() throws Exception {
        AccountValidationRequest validationRequest = new AccountValidationRequest("12345",Arrays.asList("source1"));
        Mockito.when(validator.validate(validationRequest)).thenReturn(new ArrayList<>());
        SourceRequest sourceRequest = new SourceRequest("123456");
        SourceResponse sourceResponse = new SourceResponse("source1",true);
        ResponseEntity<SourceResponse> response = new ResponseEntity<>(sourceResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity("https://source1.com/v1/api/account/validate",sourceRequest, SourceResponse.class)).thenReturn(response);

        AccountValidationResponse accountValidationResponse = validationController.process(validationRequest);
        Assertions.assertNotNull(accountValidationResponse);
        Assertions.assertNotNull(accountValidationResponse.getResult());
        Assertions.assertEquals(1,accountValidationResponse.getResult().size());
    }
}
