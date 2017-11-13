package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Purchase;
import com.wrisx.wrisxdapp.domain.RiskKnowledge;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

public class RiskKnowledgeData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private String uuid;

    @JsonProperty
    private int price;

    @JsonProperty
    private String title;

    @JsonProperty
    private String description;

    @Getter
    @JsonProperty
    private String keywords;

    @JsonProperty
    private String checksum;

    @JsonProperty
    private String password;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private RiskExpertData riskExpert;

    @JsonProperty
    @Setter
    private PurchaseData purchase;

    public RiskKnowledgeData(RiskKnowledge riskKnowledge) {
        this(riskKnowledge, null);
    }

    public RiskKnowledgeData(RiskKnowledge riskKnowledge, Purchase purchase) {
        this.id = riskKnowledge.getId();
        this.uuid = riskKnowledge.getUuid();
        this.price = riskKnowledge.getPrice();
        this.title = riskKnowledge.getTitle();
        this.description = riskKnowledge.getDescription();
        this.keywords = riskKnowledge.getKeywords();
        this.checksum = riskKnowledge.getChecksum();
        this.password = riskKnowledge.getPassword();
        this.timestamp = riskKnowledge.getTimestamp();
        if (riskKnowledge.getRiskExpert() != null) {
            this.riskExpert = new RiskExpertData(riskKnowledge.getRiskExpert());
        }
        if (purchase != null) {
            this.purchase = new PurchaseData(purchase);
        }
    }
}
