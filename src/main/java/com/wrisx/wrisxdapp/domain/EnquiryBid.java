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
    @Enumerated(EnumType.STRING)
    @Setter
    private State state;

    @Setter
    private String transactionHash;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "enquiry_id")
    private ResearchEnquiry researchEnquiry;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;

    @Setter
    @ManyToOne
    @JoinColumn(name = "research_id")
    private Research research;

    public EnquiryBid() {
        //
    }

    public EnquiryBid(ResearchEnquiry researchEnquiry, Expert expert,
                      int price, String comment) {
        this.researchEnquiry = researchEnquiry;
        this.expert = expert;
        this.price = price;
        this.comment = comment;
        this.selected = false;
        this.timestamp = new Date(System.currentTimeMillis());
        this.state = CREATED;
    }
}
