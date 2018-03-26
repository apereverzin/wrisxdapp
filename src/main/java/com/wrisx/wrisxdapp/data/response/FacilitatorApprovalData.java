package com.wrisx.wrisxdapp.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.FacilitatorApproval;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
public class FacilitatorApprovalData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private String comment;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private FacilitatorData facilitator;

    @JsonProperty
    private ResearchData research;

    public FacilitatorApprovalData(FacilitatorApproval facilitatorApproval) {
        this.id = facilitatorApproval.getId();
        this.comment = facilitatorApproval.getComment();
        this.timestamp = facilitatorApproval.getTimestamp();
        if (facilitatorApproval.getFacilitator() != null) {
            this.facilitator =
                    new FacilitatorData(facilitatorApproval.getFacilitator());
        }
        if (facilitatorApproval.getResearch() != null) {
            this.research = new ResearchData(facilitatorApproval.getResearch());
        }
    }
}
