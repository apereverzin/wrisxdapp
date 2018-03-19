package com.wrisx.wrisxdapp.data.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ResearchRequest {
    String expertAddress;
    String pdfUuid;
    String videoUuid;
    String title;
    String description;
    String keywords;
    String checksum;
    String password;
    String clientAddress;
    long enquiryId;
    long bidId;
}
