package com.wrisx.wrisxdapp.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Entity
@Table(name = "risk_knowledge_enquiry")
public class RiskKnowledgeEnquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    private String keywords;

    @NotNull
    private String description;

    @NotNull
    private Date timestamp;

    public RiskKnowledgeEnquiry() {
        //
    }

    public RiskKnowledgeEnquiry(Client client, String keywords, String description) {
        this.client = client;
        this.keywords = keywords;
        this.description = description;
        this.timestamp = new Date(System.currentTimeMillis());
    }
}
