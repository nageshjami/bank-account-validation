package com.nagesh.exam.service;

import com.nagesh.exam.model.SourceRequest;
import com.nagesh.exam.model.SourceResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class SourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SourceService.class);

    @Autowired
    RestTemplate restTemplate;

    @Async
    public CompletableFuture<SourceResponse> processSourceRequest(SourceRequest sourceRequest,String url,String source){
        LOGGER.info("Compleatable future running -> " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        CompletableFuture<SourceResponse> value =  CompletableFuture.completedFuture(callService(sourceRequest,url,source));
        long end = System.currentTimeMillis();
        LOGGER.info("Total time taken for service call is -> " + (end - start));
        return  value;
    }

    private SourceResponse callService(SourceRequest sourceRequest, String url, String source) {
        LOGGER.info("Calling REST service for -->" + url);
        SourceResponse sourceResponse = new SourceResponse(source,false);
        try{
            ResponseEntity<SourceResponse> response = restTemplate.postForEntity(url,sourceRequest,SourceResponse.class);
            if(response.getStatusCodeValue() == HttpServletResponse.SC_OK){
                sourceResponse.setIsValid(response.getBody().getIsValid());
            }
        }catch (Exception exception){
            LOGGER.info("Exception occured while invoking the service");
        }
        return sourceResponse;
    }
}
