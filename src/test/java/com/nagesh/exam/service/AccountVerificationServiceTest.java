package com.nagesh.exam.service;

import com.nagesh.exam.model.AccountValidationRequest;
import com.nagesh.exam.model.AccountValidationResponse;
import com.nagesh.exam.model.SourceRequest;
import com.nagesh.exam.model.SourceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class AccountVerificationServiceTest {

    @InjectMocks
    private AccountVerificationService accountVerificationService;

    @InjectMocks
    private SourceService service;

    @Mock
    private RestTemplate restTemplate;

    private String URL_1 = "https://source1.com/v1/api/account/validate";
    private String URL_2 = "https://source1.com/v1/api/account/validate";

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(accountVerificationService,"source1URL","https://source1.com/v1/api/account/validate");
        ReflectionTestUtils.setField(accountVerificationService,"source2URL","https://source2.com/v2/api/account/validate");
        ReflectionTestUtils.setField(accountVerificationService,"service",service);
    }

    @Test
    public void valiateAccount_source1() throws ExecutionException, InterruptedException {
        AccountValidationRequest validationRequest = new AccountValidationRequest("123456", Arrays.asList("source1"));
        SourceRequest sourceRequest = new SourceRequest("123456");
        SourceResponse sourceResponse = new SourceResponse("source1",true);
        ResponseEntity<SourceResponse> response = new ResponseEntity<>(sourceResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(URL_1,sourceRequest, SourceResponse.class)).thenReturn(response);
        AccountValidationResponse validationResponse = accountVerificationService.validateAccount(validationRequest);
        Assertions.assertNotNull(validationResponse);
        Assertions.assertNotNull(validationResponse.getResult());
        Assertions.assertTrue(validationResponse.getResult().size() == 1);
        Assertions.assertEquals("source1",validationResponse.getResult().get(0).getSource());
        Assertions.assertTrue(validationResponse.getResult().get(0).getIsValid());
    }

    @Test
    public void valiateAccount_source2() throws ExecutionException, InterruptedException {
        AccountValidationRequest validationRequest = new AccountValidationRequest("123456", Arrays.asList("source2"));
        SourceRequest sourceRequest = new SourceRequest("123456");
        SourceResponse sourceResponse = new SourceResponse("source2",false);
        ResponseEntity<SourceResponse> response = new ResponseEntity<>(sourceResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(URL_1,sourceRequest, SourceResponse.class)).thenReturn(response);
        AccountValidationResponse validationResponse = accountVerificationService.validateAccount(validationRequest);
        Assertions.assertNotNull(validationResponse);
        Assertions.assertNotNull(validationResponse.getResult());
        Assertions.assertTrue(validationResponse.getResult().size() == 1);
        Assertions.assertEquals("source2",validationResponse.getResult().get(0).getSource());
        Assertions.assertFalse(validationResponse.getResult().get(0).getIsValid());
    }

    @Test
    public void valiateAccount_two() throws ExecutionException, InterruptedException {
        AccountValidationRequest validationRequest = new AccountValidationRequest("123456", Arrays.asList("source1","source2"));
        SourceRequest sourceRequest = new SourceRequest("123456");
        ResponseEntity<SourceResponse> response = new ResponseEntity<>(new SourceResponse("source1",false), HttpStatus.OK);
        ResponseEntity<SourceResponse> response2 = new ResponseEntity<>(new SourceResponse("source2",true), HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(URL_1,sourceRequest, SourceResponse.class)).thenReturn(response);
        Mockito.when(restTemplate.postForEntity(URL_2,sourceRequest, SourceResponse.class)).thenReturn(response2);
        AccountValidationResponse validationResponse = accountVerificationService.validateAccount(validationRequest);
        Assertions.assertNotNull(validationResponse);
        Assertions.assertNotNull(validationResponse.getResult());
        Assertions.assertTrue(validationResponse.getResult().size() == 2);
        Assertions.assertEquals("source1",validationResponse.getResult().get(0).getSource());
        Assertions.assertTrue(validationResponse.getResult().get(0).getIsValid());
        Assertions.assertEquals("source2",validationResponse.getResult().get(1).getSource());
        Assertions.assertFalse(validationResponse.getResult().get(1).getIsValid());
    }

    @Test
    public void valiateAccount_empty() throws ExecutionException, InterruptedException {
        AccountValidationRequest validationRequest = new AccountValidationRequest("123456", null);
        SourceRequest sourceRequest = new SourceRequest("123456");
        ResponseEntity<SourceResponse> response = new ResponseEntity<>(new SourceResponse("source1",false), HttpStatus.OK);
        ResponseEntity<SourceResponse> response2 = new ResponseEntity<>(new SourceResponse("source2",true), HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(URL_1,sourceRequest, SourceResponse.class)).thenReturn(response);
        Mockito.when(restTemplate.postForEntity(URL_2,sourceRequest, SourceResponse.class)).thenReturn(response2);
        AccountValidationResponse validationResponse = accountVerificationService.validateAccount(validationRequest);
        Assertions.assertNotNull(validationResponse);
        Assertions.assertNotNull(validationResponse.getResult());
        Assertions.assertTrue(validationResponse.getResult().size() == 2);
        Assertions.assertEquals("source1",validationResponse.getResult().get(0).getSource());
        Assertions.assertTrue(validationResponse.getResult().get(0).getIsValid());
        Assertions.assertEquals("source2",validationResponse.getResult().get(1).getSource());
        Assertions.assertFalse(validationResponse.getResult().get(1).getIsValid());
    }


}
