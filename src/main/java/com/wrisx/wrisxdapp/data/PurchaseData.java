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
    private RiskExpertData riskExpert;

    @JsonProperty
    private RiskKnowledgeData riskKnowledge;

    public PurchaseData(Purchase purchase) {
        this.id = purchase.getId();
        this.price = purchase.getPrice();
        this.timestamp = purchase.getTimestamp();
        if (purchase.getClient() != null) {
            this.client = new ClientData(purchase.getClient());
        }
        if (purchase.getRiskExpert() != null) {
            this.riskExpert = new RiskExpertData(purchase.getRiskExpert());
        }
        if (purchase.getRiskKnowledge() != null) {
            this.riskKnowledge = new RiskKnowledgeData(purchase.getRiskKnowledge());
        }
    }
}
