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
    @Setter
    private boolean confirmed;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "research_id")
    private Research research;

    public Purchase() {
        //
    }

    public Purchase(int price, Client client, Expert expert,
                    Research research) {
        this.price = price;
        this.timestamp = new Date(System.currentTimeMillis());
        this.client = client;
        this.expert = expert;
        this.research = research;
        this.confirmed = false;
    }
}
