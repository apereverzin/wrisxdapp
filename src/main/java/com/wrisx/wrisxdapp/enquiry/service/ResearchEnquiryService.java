package com.wrisx.wrisxdapp.enquiry.service;

import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.data.request.ResearchEnquiryRequest;
import com.wrisx.wrisxdapp.data.response.EnquiryBidData;
import com.wrisx.wrisxdapp.data.response.ResearchEnquiryData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.EnquiryBid;
import com.wrisx.wrisxdapp.domain.EnquiryBidDao;
import com.wrisx.wrisxdapp.domain.ResearchEnquiry;
import com.wrisx.wrisxdapp.domain.ResearchEnquiryDao;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.wrisx.wrisxdapp.util.WrisxUtil.getKeywordList;
import static com.wrisx.wrisxdapp.util.WrisxUtil.getListFromIterable;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class ResearchEnquiryService {
    private final Logger logger = LoggerFactory.getLogger(ResearchEnquiryService.class);

    private final ResearchEnquiryDao researchEnquiryDao;
    private final EnquiryBidDao enquiryBidDao;
    private final EntityProvider entityProvider;

    @Autowired
    public ResearchEnquiryService(ResearchEnquiryDao researchEnquiryDao,
                                  EnquiryBidDao enquiryBidDao,
                                  EntityProvider entityProvider) {
        this.researchEnquiryDao = researchEnquiryDao;
        this.enquiryBidDao = enquiryBidDao;
        this.entityProvider = entityProvider;
    }

    public ResearchEnquiryData saveEnquiry(ResearchEnquiryRequest researchEnquiryRequest)
            throws ResourceNotFoundException {
        Client client = entityProvider.getClientByAddress(researchEnquiryRequest.getAddress());

        ResearchEnquiry researchEnquiry =
                new ResearchEnquiry(client, researchEnquiryRequest.getKeywords(),
                        researchEnquiryRequest.getDescription());

        return new ResearchEnquiryData(researchEnquiryDao.save(researchEnquiry));
    }

    public List<ResearchEnquiryData> getClientEnquiries(String clientAddress)
            throws ResourceNotFoundException {
        Client client = entityProvider.getClientByAddress(clientAddress);

        return getListFromIterable(researchEnquiryDao.findByClient(client)).stream().
                sorted(comparing(ResearchEnquiry::getTimestamp).reversed()).
                map(enquiry -> new ResearchEnquiryData(enquiry)).
                collect(toList());
    }

    public ResearchEnquiryData getEnquiry(long id) throws ResourceNotFoundException {
        ResearchEnquiry researchEnquiry = entityProvider.getResearchEnquiryById(id);

        return new ResearchEnquiryData(researchEnquiry);
    }

    public List<ResearchEnquiryData> findExpertEnquiries(String expertAddress,
                                                         String keywords)
            throws ResourceNotFoundException {
        entityProvider.getExpertByAddress(expertAddress);

        List<String> keywordList = getKeywordList(keywords);
        List<ResearchEnquiry> researchEnquiries =
                getListFromIterable(researchEnquiryDao.findAll());

        return (keywordList.isEmpty() ? researchEnquiries.stream() :
                researchEnquiries.stream().filter(item -> {
                    List<String> itemKeywordList = getKeywordList(item.getKeywords());
                    return itemKeywordList.containsAll(keywordList);
                })).
                sorted(comparing(ResearchEnquiry::getTimestamp).reversed()).
                map(enquiry -> {
                    Optional<EnquiryBid> enquiryBidOpt =
                            enquiryBidDao.findByResearchEnquiry(enquiry).stream().
                                    filter(bid -> bid.getExpert().getAddress().
                                            equals(expertAddress)).findFirst();
                    return new ResearchEnquiryData(enquiry,
                            enquiryBidOpt.map(enquiryBid -> new EnquiryBidData(enquiryBid)).
                                    orElse(null));
                }).
                collect(toList());
    }
}
