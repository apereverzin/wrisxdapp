package com.wrisx.wrisxdapp.data.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ExpertRequest {
    private String address;
    private String name;
    private String emailAddress;
    private String password;
    private String profileLink;
    private String websiteLink;
    private String keywords;
    private String description;
}
