package com.nagesh.exam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagesh.exam.model.AccountValidationRequest;
import com.nagesh.exam.model.AccountValidationResponse;
import com.nagesh.exam.service.AccountVerificationService;
import com.nagesh.exam.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountValidationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountValidationController.class);
    @Autowired
    AccountVerificationService service;

    @Autowired
    RequestValidator validator;

    @Autowired
    ObjectMapper mapper;


    @RequestMapping(method = RequestMethod.POST, path = "/account/validation")
    public AccountValidationResponse process(@RequestBody AccountValidationRequest validationRequest) throws Exception {
        LOGGER.info("Request received for account valiation ..!");
        LOGGER.info("Input request" + mapper.writeValueAsString(validationRequest));
        validateRequest(validationRequest);
        return service.validateAccount(validationRequest);
    }

    private void validateRequest(AccountValidationRequest validationRequest) throws Exception {

        List<String> errors = validator.validate(validationRequest);
        if(errors.size() > 0){
            LOGGER.error("Request validation failed" + errors);
            throw new Exception("Request validation failed -> " + errors);
        }
    }
}
