package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Purchase;

import java.util.Date;

public class PurchaseData {
    @JsonProperty
    private long id;

    @JsonProperty
    private int price;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private ClientData client;

    @JsonProperty
    private ExpertData expert;

    @JsonProperty
    private ResearchData research;

    public PurchaseData(Purchase purchase) {
        this.id = purchase.getId();
        this.price = purchase.getPrice();
        this.timestamp = purchase.getTimestamp();
        if (purchase.getClient() != null) {
            this.client = new ClientData(purchase.getClient());
        }
        if (purchase.getExpert() != null) {
            this.expert = new ExpertData(purchase.getExpert());
        }
        if (purchase.getResearch() != null) {
            this.research = new ResearchData(purchase.getResearch());
        }
    }
}
