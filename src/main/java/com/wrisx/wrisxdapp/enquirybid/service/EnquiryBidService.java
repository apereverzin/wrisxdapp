package com.wrisx.wrisxdapp.enquirybid.service;

import com.wrisx.wrisxdapp.data.EnquiryBidData;
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

import static com.wrisx.wrisxdapp.util.WrisxUtil.getListFromIterable;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class EnquiryBidService {
    private final Logger logger = LoggerFactory.getLogger(EnquiryBidService.class);

    private final ResearchEnquiryDao researchEnquiryDao;
    private final EnquiryBidDao enquiryBidDao;
    private final ExpertDao expertDao;

    @Autowired
    public EnquiryBidService(ResearchEnquiryDao researchEnquiryDao, EnquiryBidDao enquiryBidDao, ExpertDao expertDao) {
        this.researchEnquiryDao = researchEnquiryDao;
        this.enquiryBidDao = enquiryBidDao;
        this.expertDao = expertDao;
    }

    public EnquiryBidData placeEnquiryBid(long enquiryId, String expertAddress,
                                          int bid, String comment) {
        ResearchEnquiry enquiry = researchEnquiryDao.findOne(enquiryId);
        if (enquiry == null) {
            throw new RuntimeException(MessageFormat.format("Enquiry not found {0}", enquiryId));
        }
        Expert expert = expertDao.findByAddress(expertAddress);
        if (expert == null) {
            throw new RuntimeException(MessageFormat.format("Expert not found {0}", expertAddress));
        }
        EnquiryBid enquiryBid = new EnquiryBid(enquiry, expert, bid, comment);
        return new EnquiryBidData(enquiryBidDao.save(enquiryBid));
    }

    public List<EnquiryBidData> getEnquiryBidsByEnquiry(long enquiryId) {
        ResearchEnquiry enquiry = researchEnquiryDao.findOne(enquiryId);
        if (enquiry == null) {
            throw new RuntimeException(MessageFormat.format("Enquiry not found {0}", enquiryId));
        }
        return getListFromIterable(enquiryBidDao.findByResearchEnquiry(enquiry)).
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
        Expert expert = expertDao.findByAddress(expertAddress);
        if (expert == null) {
            throw new RuntimeException(MessageFormat.format("Expert not found {0}", expertAddress));
        }
        return getListFromIterable(enquiryBidDao.findByExpert(expert)).
                stream().
                sorted(comparing(EnquiryBid::getTimestamp).reversed()).
                map(bid -> new EnquiryBidData(bid)).
                collect(toList());
    }
}
