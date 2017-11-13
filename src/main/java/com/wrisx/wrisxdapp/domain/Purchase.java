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
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private int price;

    @NotNull
    private Date timestamp;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "risk_expert_id")
    private RiskExpert riskExpert;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "risk_knowledge_id")
    private RiskKnowledge riskKnowledge;

    public Purchase() {
        //
    }

    public Purchase(int price, Client client, RiskExpert riskExpert,
                    RiskKnowledge riskKnowledge) {
        this.price = price;
        this.timestamp = new Date(System.currentTimeMillis());
        this.client = client;
        this.riskExpert = riskExpert;
        this.riskKnowledge = riskKnowledge;
    }
}
