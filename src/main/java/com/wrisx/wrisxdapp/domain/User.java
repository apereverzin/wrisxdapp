package com.wrisx.wrisxdapp.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static com.wrisx.wrisxdapp.domain.State.CREATED;

@Getter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String address;

    @NotNull
    private String name;

    @NotNull
    private String emailAddress;

    private String profileLink;

    private String websiteLink;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    private State state;

    @Setter
    private String transactionHash;

    public User() {
        //
    }

    public User(String address,
                String name,
                String emailAddress,
                String profileLink,
                String websiteLink,
                String password) {
        this.address = address;
        this.name = name;
        this.emailAddress = emailAddress;
        this.profileLink = profileLink;
        this.websiteLink = websiteLink;
        this.password = password;
        this.state = CREATED;
    }
}
