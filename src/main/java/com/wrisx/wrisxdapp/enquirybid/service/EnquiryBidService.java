package com.wrisx.wrisxdapp.enquirybid.service;

import com.wrisx.wrisxdapp.data.EnquiryBidData;
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

import static com.wrisx.wrisxdapp.util.WrisxUtil.getListFromIterable;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class EnquiryBidService {
    private final Logger logger = LoggerFactory.getLogger(EnquiryBidService.class);

    private final RiskKnowledgeEnquiryDao riskKnowledgeEnquiryDao;
    private final EnquiryBidDao enquiryBidDao;
    private final RiskExpertDao riskExpertDao;

    @Autowired
    public EnquiryBidService(RiskKnowledgeEnquiryDao riskKnowledgeEnquiryDao, EnquiryBidDao enquiryBidDao, RiskExpertDao riskExpertDao) {
        this.riskKnowledgeEnquiryDao = riskKnowledgeEnquiryDao;
        this.enquiryBidDao = enquiryBidDao;
        this.riskExpertDao = riskExpertDao;
    }

    public EnquiryBidData placeEnquiryBid(long enquiryId, String expertAddress,
                                          int bid, String comment) {
        RiskKnowledgeEnquiry enquiry = riskKnowledgeEnquiryDao.findOne(enquiryId);
        if (enquiry == null) {
            throw new RuntimeException(MessageFormat.format("Enquiry not found {0}", enquiryId));
        }
        RiskExpert riskExpert = riskExpertDao.findByAddress(expertAddress);
        if (riskExpert == null) {
            throw new RuntimeException(MessageFormat.format("Risk expert not found {0}", expertAddress));
        }
        EnquiryBid enquiryBid = new EnquiryBid(enquiry, riskExpert, bid, comment);
        return new EnquiryBidData(enquiryBidDao.save(enquiryBid));
    }

    public List<EnquiryBidData> getEnquiryBidsByEnquiry(long enquiryId) {
        RiskKnowledgeEnquiry enquiry = riskKnowledgeEnquiryDao.findOne(enquiryId);
        if (enquiry == null) {
            throw new RuntimeException(MessageFormat.format("Enquiry not found {0}", enquiryId));
        }
        return getListFromIterable(enquiryBidDao.findByRiskKnowledgeEnquiry(enquiry)).
                stream().
                sorted(comparing(EnquiryBid::getTimestamp).reversed()).
                map(bid -> new EnquiryBidData(bid)).
                collect(toList());
    }

    public EnquiryBidData selectEnquiryBid(long enquiryBidId) {
        EnquiryBid enquiryBid = enquiryBidDao.findOne(enquiryBidId);
        if (enquiryBid == null) {
            throw new RuntimeException(MessageFormat.format("Enquiry bid not found {0}", enquiryBid));
        }
        enquiryBid.setSelected(true);
        return new EnquiryBidData(enquiryBidDao.save(enquiryBid));
    }

    public List<EnquiryBidData> getEnquiryBidsByExpert(String expertAddress) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(expertAddress);
        if (riskExpert == null) {
            throw new RuntimeException(MessageFormat.format("Risk expert not found {0}", expertAddress));
        }
        return getListFromIterable(enquiryBidDao.findByRiskExpert(riskExpert)).
                stream().
                sorted(comparing(EnquiryBid::getTimestamp).reversed()).
                map(bid -> new EnquiryBidData(bid)).
                collect(toList());
    }
}
