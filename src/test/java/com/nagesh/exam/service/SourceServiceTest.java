package com.nagesh.exam.service;

import com.nagesh.exam.model.SourceRequest;
import com.nagesh.exam.model.SourceResponse;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class SourceServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SourceService service;


    private String URL = "https://source1.com/v1/api/account/validate";

    private String SOURCE = "source1";

    @Test
    public void processService() throws ExecutionException, InterruptedException {
        SourceRequest sourceRequest = new SourceRequest("123456");
        SourceResponse sourceResponse = new SourceResponse(SOURCE,true);
        ResponseEntity<SourceResponse> response = new ResponseEntity<>(sourceResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(URL,sourceRequest, SourceResponse.class)).thenReturn(response);
        CompletableFuture<SourceResponse> value = service.processSourceRequest(sourceRequest, URL, SOURCE);
        Assertions.assertNotNull(value);
        Assertions.assertNotNull(value.get());
        sourceResponse = value.get();
        Assertions.assertEquals(sourceResponse.getSource(),SOURCE);
        Assertions.assertTrue(sourceResponse.getIsValid());
    }

    @Test
    public void processService_1() throws ExecutionException, InterruptedException {
        SourceRequest sourceRequest = new SourceRequest("123456");
        SourceResponse sourceResponse = new SourceResponse(SOURCE,true);
        ResponseEntity<SourceResponse> response = new ResponseEntity<>(sourceResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(null,sourceRequest, SourceResponse.class)).thenReturn(response);
        CompletableFuture<SourceResponse> value = service.processSourceRequest(sourceRequest, URL, SOURCE);
        Assertions.assertNotNull(value);
        Assertions.assertNotNull(value.get());
        sourceResponse = value.get();
        Assertions.assertEquals(sourceResponse.getSource(),SOURCE);
        Assertions.assertFalse(sourceResponse.getIsValid());
    }

}
