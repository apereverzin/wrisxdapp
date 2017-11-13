package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.EnquiryBid;

import java.io.Serializable;
import java.util.Date;

public class EnquiryBidData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private int price;

    @JsonProperty
    private String comment;

    @JsonProperty
    private boolean selected;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private RiskKnowledgeEnquiryData riskKnowledgeEnquiry;

    @JsonProperty
    private RiskExpertData riskExpert;

    @JsonProperty
    private RiskKnowledgeData riskKnowledge;

    public EnquiryBidData(EnquiryBid enquiryBid) {
        this.id = enquiryBid.getId();
        this.price = enquiryBid.getPrice();
        this.comment = enquiryBid.getComment();
        this.selected = enquiryBid.isSelected();
        this.timestamp = enquiryBid.getTimestamp();
        if (enquiryBid.getRiskKnowledgeEnquiry() != null) {
            this.riskKnowledgeEnquiry =
                    new RiskKnowledgeEnquiryData(enquiryBid.getRiskKnowledgeEnquiry());
        }
        if (enquiryBid.getRiskExpert() != null) {
            this.riskExpert = new RiskExpertData(enquiryBid.getRiskExpert());
        }
        if (enquiryBid.getRiskKnowledge() != null) {
            this.riskKnowledge = new RiskKnowledgeData(enquiryBid.getRiskKnowledge());
        }
    }
}
