package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.RiskKnowledgeEnquiry;

import java.io.Serializable;
import java.util.Date;

public class RiskKnowledgeEnquiryData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private ClientData client;

    @JsonProperty
    private String keywords;

    @JsonProperty
    private String description;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private EnquiryBidData enquiryBid;

    public RiskKnowledgeEnquiryData(RiskKnowledgeEnquiry riskKnowledgeEnquiry) {
        this(riskKnowledgeEnquiry, null);
    }

    public RiskKnowledgeEnquiryData(RiskKnowledgeEnquiry riskKnowledgeEnquiry,
                                    EnquiryBidData enquiryBid) {
        this.id = riskKnowledgeEnquiry.getId();
        if (riskKnowledgeEnquiry.getClient() != null) {
            this.client = new ClientData(riskKnowledgeEnquiry.getClient());
        }
        this.keywords = riskKnowledgeEnquiry.getKeywords();
        this.description = riskKnowledgeEnquiry.getDescription();
        this.timestamp = riskKnowledgeEnquiry.getTimestamp();
        this.enquiryBid = enquiryBid;
    }
}
