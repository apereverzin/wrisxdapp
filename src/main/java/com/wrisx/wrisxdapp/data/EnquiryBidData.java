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
    private ResearchEnquiryData researchEnquiry;

    @JsonProperty
    private ExpertData expert;

    @JsonProperty
    private ResearchData research;

    public EnquiryBidData(EnquiryBid enquiryBid) {
        this.id = enquiryBid.getId();
        this.price = enquiryBid.getPrice();
        this.comment = enquiryBid.getComment();
        this.selected = enquiryBid.isSelected();
        this.timestamp = enquiryBid.getTimestamp();
        if (enquiryBid.getResearchEnquiry() != null) {
            this.researchEnquiry =
                    new ResearchEnquiryData(enquiryBid.getResearchEnquiry());
        }
        if (enquiryBid.getExpert() != null) {
            this.expert = new ExpertData(enquiryBid.getExpert());
        }
        if (enquiryBid.getResearch() != null) {
            this.research = new ResearchData(enquiryBid.getResearch());
        }
    }
}
