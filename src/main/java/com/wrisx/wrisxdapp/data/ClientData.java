package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Client;

import java.io.Serializable;

public class ClientData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private String address;

    @JsonProperty
    private String name;

    public ClientData(Client client) {
        this.id = client.getId();
        this.address = client.getAddress();
        this.name = client.getName();
    }
}
