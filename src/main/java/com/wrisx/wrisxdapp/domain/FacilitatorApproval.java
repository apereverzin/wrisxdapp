package com.wrisx.wrisxdapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "facilitator_approval")
public class FacilitatorApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonProperty
    private String comment;

    @NotNull
    private Date timestamp;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "facilitator_id")
    private Facilitator facilitator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "research_id")
    private Research research;

    public FacilitatorApproval() {
        //
    }

    public FacilitatorApproval(String comment,
                               Date timestamp,
                               Facilitator facilitator,
                               Research research) {
        this.comment = comment;
        this.timestamp = timestamp;
        this.facilitator = facilitator;
        this.research = research;
    }
}
