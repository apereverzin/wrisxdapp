package com.wrisx.wrisxdapp.enquiry.service;

import com.wrisx.wrisxdapp.data.EnquiryBidData;
import com.wrisx.wrisxdapp.data.ResearchEnquiryData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.EnquiryBid;
import com.wrisx.wrisxdapp.domain.EnquiryBidDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import com.wrisx.wrisxdapp.domain.ResearchEnquiry;
import com.wrisx.wrisxdapp.domain.ResearchEnquiryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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
    private final ClientDao clientDao;
    private final ExpertDao expertDao;
    private final EnquiryBidDao enquiryBidDao;

    @Autowired
    public ResearchEnquiryService(ResearchEnquiryDao researchEnquiryDao,
                                  ClientDao clientDao,
                                  ExpertDao expertDao,
                                  EnquiryBidDao enquiryBidDao) {
        this.researchEnquiryDao = researchEnquiryDao;
        this.clientDao = clientDao;
        this.expertDao = expertDao;
        this.enquiryBidDao = enquiryBidDao;
    }

    public ResearchEnquiryData saveEnquiry(String clientAddress, String keywords,
                                           String description) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }
        ResearchEnquiry researchEnquiry =
                new ResearchEnquiry(client, keywords, description);
        return new ResearchEnquiryData(researchEnquiryDao.save(researchEnquiry));
    }

    public List<ResearchEnquiryData> getClientEnquiries(String clientAddress) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }

        return getListFromIterable(researchEnquiryDao.findByClient(client)).stream().
                sorted(comparing(ResearchEnquiry::getTimestamp).reversed()).
                map(enquiry -> new ResearchEnquiryData(enquiry)).
                collect(toList());
    }

    public ResearchEnquiryData getEnquiry(long id) {
        return new ResearchEnquiryData(researchEnquiryDao.findOne(id));
    }

    public List<ResearchEnquiryData> findExpertEnquiries(String expertAddress,
                                                         String keywords) {
        Expert expert = expertDao.findByAddress(expertAddress);
        if (expert == null) {
            throw new RuntimeException(
                    MessageFormat.format("Expert not found {0}", expertAddress));
        }

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
