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
@Table(name = "risk_knowledge")
public class RiskKnowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String uuid;

    @NotNull
    private int price;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String keywords;

    @NotNull
    private String checksum;

    @NotNull
    private String password;

    @NotNull
    private Date timestamp;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "risk_expert_id")
    private RiskExpert riskExpert;

    public RiskKnowledge() {
        //
    }

    public RiskKnowledge(String uuid, int price, String title, String description,
                         String keywords, String checksum, String password,
                         RiskExpert riskExpert) {
        this.uuid = uuid;
        this.price = price;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.checksum = checksum;
        this.password = password;
        this.riskExpert = riskExpert;
        this.timestamp = new Date(System.currentTimeMillis());
    }
}
