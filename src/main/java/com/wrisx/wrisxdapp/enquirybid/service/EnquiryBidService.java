package com.wrisx.wrisxdapp.enquirybid.service;

import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.data.response.EnquiryBidData;
import com.wrisx.wrisxdapp.domain.EnquiryBid;
import com.wrisx.wrisxdapp.domain.EnquiryBidDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import com.wrisx.wrisxdapp.domain.ResearchEnquiry;
import com.wrisx.wrisxdapp.domain.ResearchEnquiryDao;
import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

import static com.wrisx.wrisxdapp.domain.State.COMMITTED;
import static com.wrisx.wrisxdapp.domain.State.CONFIRMED;
import static com.wrisx.wrisxdapp.util.WrisxUtil.getListFromIterable;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class EnquiryBidService {
    private final Logger logger = LoggerFactory.getLogger(EnquiryBidService.class);

    private final ResearchEnquiryDao researchEnquiryDao;
    private final EnquiryBidDao enquiryBidDao;
    private final ExpertDao expertDao;
    private final EntityProvider entityProvider;

    @Autowired
    public EnquiryBidService(ResearchEnquiryDao researchEnquiryDao,
                             EnquiryBidDao enquiryBidDao, ExpertDao expertDao,
                             EntityProvider entityProvider) {
        this.researchEnquiryDao = researchEnquiryDao;
        this.enquiryBidDao = enquiryBidDao;
        this.expertDao = expertDao;
        this.entityProvider = entityProvider;
    }

    public EnquiryBidData placeEnquiryBid(long enquiryId, String expertAddress,
                                          int bid, String comment) throws ResourceNotFoundException {
        ResearchEnquiry enquiry = entityProvider.getResearchEnquiryById(enquiryId);
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        EnquiryBid enquiryBid = new EnquiryBid(enquiry, expert, bid, comment);

        return new EnquiryBidData(enquiryBidDao.save(enquiryBid));
    }

    public List<EnquiryBidData> getEnquiryBidsByEnquiry(long enquiryId)
            throws ResourceNotFoundException {
        ResearchEnquiry enquiry = entityProvider.getResearchEnquiryById(enquiryId);
        return getListFromIterable(enquiryBidDao.findByResearchEnquiry(enquiry)).
                stream().
                sorted(comparing(EnquiryBid::getTimestamp).reversed()).
                map(bid -> new EnquiryBidData(bid)).
                collect(toList());
    }

    public EnquiryBidData setEnquiryBidSelection(long enquiryBidId, boolean selected)
            throws ResourceNotFoundException {
        EnquiryBid enquiryBid = entityProvider.getEnquiryBidById(enquiryBidId);

        enquiryBid.setSelected(selected);

        return new EnquiryBidData(enquiryBidDao.save(enquiryBid));
    }

    public void deleteEnquiryBid(long enquiryBidId) throws ResourceNotFoundException {
        EnquiryBid enquiryBid = entityProvider.getEnquiryBidById(enquiryBidId);

        enquiryBidDao.delete(enquiryBid);
    }

    public void confirmEnquiryBidCreation(long enquiryBidId, String transactionHash)
            throws ResourceNotFoundException {
        EnquiryBid enquiryBid = entityProvider.getEnquiryBidById(enquiryBidId);

        enquiryBid.setState(CONFIRMED);
        enquiryBid.setTransactionHash(transactionHash);

        enquiryBidDao.save(enquiryBid);
    }

    public void commitEnquiryBidCreation(long enquiryBidId, String transactionHash)
            throws ResourceNotFoundException {
        EnquiryBid enquiryBid = entityProvider.getEnquiryBidByIdAndTransactionHash(
                enquiryBidId, transactionHash);

        if (enquiryBid.getState() != CONFIRMED) {
            throw new BadRequestException(MessageFormat.format(
                    "Illegal state of enquiry bid {0}", enquiryBidId));
        }
        enquiryBid.setState(COMMITTED);

        enquiryBidDao.save(enquiryBid);
    }

    public List<EnquiryBidData> getEnquiryBidsByExpert(String expertAddress)
            throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        return getListFromIterable(enquiryBidDao.findByExpert(expert)).
                stream().
                sorted(comparing(EnquiryBid::getTimestamp).reversed()).
                map(bid -> new EnquiryBidData(bid)).
                collect(toList());
    }
}
