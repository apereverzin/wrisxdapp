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
import java.util.Date;

import static com.wrisx.wrisxdapp.domain.State.CREATED;

@Getter
@Entity
@Table(name = "research")
public class Research {
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

    @Setter
    private String password;

    @NotNull
    private Date timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    private State state;

    @Setter
    private String transactionHash;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;

    public Research() {
        //
    }

    public Research(String uuid, int price, String title, String description,
                    String keywords, String checksum, String password,
                    Expert expert) {
        this.uuid = uuid;
        this.price = price;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.checksum = checksum;
        this.password = password;
        this.expert = expert;
        this.timestamp = new Date(System.currentTimeMillis());
        this.state = CREATED;
    }
}
