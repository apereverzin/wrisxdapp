package com.wrisx.wrisxdapp.domain;

import lombok.Getter;
import lombok.Setter;

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
@Table(name = "enquiry_bid")
public class EnquiryBid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private int price;

    @NotNull
    private String comment;

    @NotNull
    @Setter
    private boolean selected;

    @NotNull
    private Date timestamp;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "enquiry_id")
    private RiskKnowledgeEnquiry riskKnowledgeEnquiry;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "risk_expert_id")
    private RiskExpert riskExpert;

    @Setter
    @ManyToOne
    @JoinColumn(name = "risk_knowledge_id")
    private RiskKnowledge riskKnowledge;

    public EnquiryBid() {
        //
    }

    public EnquiryBid(RiskKnowledgeEnquiry riskKnowledgeEnquiry, RiskExpert riskExpert,
                      int price, String comment) {
        this.riskKnowledgeEnquiry = riskKnowledgeEnquiry;
        this.riskExpert = riskExpert;
        this.price = price;
        this.comment = comment;
        this.selected = false;
        this.timestamp = new Date(System.currentTimeMillis());
    }
}
