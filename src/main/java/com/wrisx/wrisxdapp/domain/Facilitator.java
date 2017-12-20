package com.wrisx.wrisxdapp.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static com.wrisx.wrisxdapp.domain.State.CREATED;

@Getter
@Entity
@Table(name = "facilitator")
public class Facilitator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String address;

    @NotNull
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    private State state;

    @Setter
    private String transactionHash;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    public Facilitator() {
        //
    }

    public Facilitator(String address, String description, User user) {
        this.address = address;
        this.description = description;
        this.user = user;
        this.state = CREATED;
    }
}
