package com.nagesh.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountValidationRequest {

    private String accountNumber;

    private List<String> sources;
}
