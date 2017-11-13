package com.wrisx.wrisxdapp.errorhandling;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorData {
    @JsonProperty(value = "errorMessage")
    private final String errorMessage;

    @JsonProperty(value = "errorCode")
    private final int errorCode;

    public ErrorData(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
