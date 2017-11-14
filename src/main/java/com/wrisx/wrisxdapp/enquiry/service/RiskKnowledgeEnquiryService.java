package com.wrisx.wrisxdapp.enquiry.service;

import com.wrisx.wrisxdapp.data.EnquiryBidData;
import com.wrisx.wrisxdapp.data.RiskKnowledgeEnquiryData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.EnquiryBid;
import com.wrisx.wrisxdapp.domain.EnquiryBidDao;
import com.wrisx.wrisxdapp.domain.RiskExpert;
import com.wrisx.wrisxdapp.domain.RiskExpertDao;
import com.wrisx.wrisxdapp.domain.RiskKnowledgeEnquiry;
import com.wrisx.wrisxdapp.domain.RiskKnowledgeEnquiryDao;
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
public class RiskKnowledgeEnquiryService {
    private final Logger logger = LoggerFactory.getLogger(RiskKnowledgeEnquiryService.class);

    private final RiskKnowledgeEnquiryDao riskKnowledgeEnquiryDao;
    private final ClientDao clientDao;
    private final RiskExpertDao riskExpertDao;
    private final EnquiryBidDao enquiryBidDao;

    @Autowired
    public RiskKnowledgeEnquiryService(RiskKnowledgeEnquiryDao riskKnowledgeEnquiryDao,
                                       ClientDao clientDao,
                                       RiskExpertDao riskExpertDao,
                                       EnquiryBidDao enquiryBidDao) {
        this.riskKnowledgeEnquiryDao = riskKnowledgeEnquiryDao;
        this.clientDao = clientDao;
        this.riskExpertDao = riskExpertDao;
        this.enquiryBidDao = enquiryBidDao;
    }

    public RiskKnowledgeEnquiryData saveEnquiry(String clientAddress, String keywords,
                                                String description) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }
        RiskKnowledgeEnquiry riskKnowledgeEnquiry =
                new RiskKnowledgeEnquiry(client, keywords, description);
        return new RiskKnowledgeEnquiryData(riskKnowledgeEnquiryDao.save(riskKnowledgeEnquiry));
    }

    public List<RiskKnowledgeEnquiryData> getClientEnquiries(String clientAddress) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }

        return getListFromIterable(riskKnowledgeEnquiryDao.findByClient(client)).stream().
                sorted(comparing(RiskKnowledgeEnquiry::getTimestamp).reversed()).
                map(enquiry -> new RiskKnowledgeEnquiryData(enquiry)).
                collect(toList());
    }

    public RiskKnowledgeEnquiryData getEnquiry(long id) {
        return new RiskKnowledgeEnquiryData(riskKnowledgeEnquiryDao.findOne(id));
    }

    public List<RiskKnowledgeEnquiryData> findExpertEnquiries(String expertAddress,
                                                              String keywords) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(expertAddress);
        if (riskExpert == null) {
            throw new RuntimeException(
                    MessageFormat.format("Risk expert not found {0}", expertAddress));
        }

        List<String> keywordList = getKeywordList(keywords);
        List<RiskKnowledgeEnquiry> riskKnowledgeEnquiries =
                getListFromIterable(riskKnowledgeEnquiryDao.findAll());

        return (keywordList.isEmpty() ? riskKnowledgeEnquiries.stream() :
                riskKnowledgeEnquiries.stream().filter(item -> {
                    List<String> itemKeywordList = getKeywordList(item.getKeywords());
                    return itemKeywordList.containsAll(keywordList);
                })).
                sorted(comparing(RiskKnowledgeEnquiry::getTimestamp).reversed()).
                map(enquiry -> {
                    Optional<EnquiryBid> enquiryBidOpt =
                            enquiryBidDao.findByRiskKnowledgeEnquiry(enquiry).stream().
                                    filter(bid -> bid.getRiskExpert().getAddress().
                                            equals(expertAddress)).findFirst();
                    return new RiskKnowledgeEnquiryData(enquiry,
                            enquiryBidOpt.map(enquiryBid -> new EnquiryBidData(enquiryBid)).
                                    orElse(null));
                }).
                collect(toList());
    }
}
