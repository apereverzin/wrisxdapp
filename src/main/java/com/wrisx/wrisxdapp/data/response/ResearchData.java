package com.wrisx.wrisxdapp.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Purchase;
import com.wrisx.wrisxdapp.domain.Research;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

public class ResearchData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private String pdfUuid;

    @JsonProperty
    private String videoUuid;

    @JsonProperty
    private String title;

    @JsonProperty
    private String description;

    @Getter
    @JsonProperty
    private String keywords;

    @JsonProperty
    private String checksum;

    private String password;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private ExpertData expert;

    @JsonProperty
    @Setter
    private PurchaseData purchase;

    public ResearchData(Research research) {
        this(research, null);
    }

    public ResearchData(Research research, Purchase purchase) {
        this.id = research.getId();
        this.pdfUuid = research.getPdfUuid();
        this.videoUuid = research.getVideoUuid();
        this.title = research.getTitle();
        this.description = research.getDescription();
        this.keywords = research.getKeywords();
        this.checksum = research.getChecksum();
        this.password = research.getPassword();
        this.timestamp = research.getTimestamp();
        if (research.getExpert() != null) {
            this.expert = new ExpertData(research.getExpert());
        }
        if (purchase != null) {
            this.purchase = new PurchaseData(purchase);
        }
    }
}
