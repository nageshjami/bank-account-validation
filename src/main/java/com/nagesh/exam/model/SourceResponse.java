package com.nagesh.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SourceResponse {

    private String source;

    private Boolean isValid;
}
