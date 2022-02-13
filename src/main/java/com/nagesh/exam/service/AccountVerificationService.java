package com.nagesh.exam.service;

import com.nagesh.exam.constant.AccountValidationConstant;
import com.nagesh.exam.model.AccountValidationRequest;
import com.nagesh.exam.model.AccountValidationResponse;
import com.nagesh.exam.model.SourceRequest;
import com.nagesh.exam.model.SourceResponse;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AccountVerificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountVerificationService.class);

    @Value("${source1.service.url}")
    @Setter
    private String source1URL;

    @Value("${source2.service.url}")
    @Setter
    private String source2URL;

    @Autowired
    private SourceService service;


    public AccountValidationResponse validateAccount(AccountValidationRequest validationRequest) throws ExecutionException, InterruptedException {
        LOGGER.info("Processing validate account in service layer ");
        AccountValidationResponse response = new AccountValidationResponse(null);
        Map<String,String> sourceMap = new HashMap<>();
        if(Objects.isNull(validationRequest.getSources()) || validationRequest.getSources().size() == 0){
            sourceMap.put(AccountValidationConstant.SOURCE1_NAME,source1URL);
            sourceMap.put(AccountValidationConstant.SOURCE2_NAME,source2URL);
        }else {
            for (String source : validationRequest.getSources()) {
                if (AccountValidationConstant.SOURCE1_NAME.equalsIgnoreCase(source)) {
                    sourceMap.put(AccountValidationConstant.SOURCE1_NAME, source1URL);
                } else if (AccountValidationConstant.SOURCE2_NAME.equalsIgnoreCase(source)) {
                    sourceMap.put(AccountValidationConstant.SOURCE2_NAME, source2URL);
                }
            }
        }
        LOGGER.info("Validate account service processed  service layer ");
        return invokeServices(validationRequest.getAccountNumber(), sourceMap);
    }

    private AccountValidationResponse invokeServices(String accountNumber, Map<String, String> serviceURLs) throws ExecutionException, InterruptedException {
       List<CompletableFuture<SourceResponse>> completableFutures = new ArrayList<>();
      for(Map.Entry<String,String> entry : serviceURLs.entrySet()){
          SourceRequest sourceRequest = new SourceRequest(accountNumber);
          completableFutures.add(service.processSourceRequest(sourceRequest,entry.getValue(), entry.getKey()));
      }
      return  constructResponse(completableFutures);
    }

    private AccountValidationResponse constructResponse(List<CompletableFuture<SourceResponse>> completableFutures) {
        AccountValidationResponse response = new AccountValidationResponse(new ArrayList<>());
        completableFutures.forEach(feature -> {
            try {
                response.getResult().add(feature.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Error occured while parsing the response");
            }
        });
        return response;
    }


}
