package com.wrisx.wrisxdapp.riskknowledge.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class RiskKnowledgeFile {
    @JsonProperty(value = "uuid")
    private final String uuid;

    @JsonProperty(value = "password")
    private final String password;

    @JsonProperty(value = "zipFileChecksumMD5")
    private final String zipFileChecksumMD5;

    public RiskKnowledgeFile(@NotNull String uuid, @NotNull String password,
                             @NotNull String zipFileChecksumMD5) {
        this.uuid = uuid;
        this.password = password;
        this.zipFileChecksumMD5 = zipFileChecksumMD5;
    }
}
