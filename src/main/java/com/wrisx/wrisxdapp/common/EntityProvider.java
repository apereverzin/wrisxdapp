package com.wrisx.wrisxdapp.common;

import com.wrisx.wrisxdapp.domain.*;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class EntityProvider {
    private final Logger logger = LoggerFactory.getLogger(EntityProvider.class);

    private final ExpertDao expertDao;
    private final ClientDao clientDao;
    private final ResearchDao researchDao;
    private final EnquiryBidDao enquiryBidDao;
    private final ResearchEnquiryDao researchEnquiryDao;
    private final PurchaseDao purchaseDao;

    public EntityProvider(ExpertDao expertDao, ClientDao clientDao,
                          ResearchDao researchDao, EnquiryBidDao enquiryBidDao,
                          ResearchEnquiryDao researchEnquiryDao,
                          PurchaseDao purchaseDao) {
        this.expertDao = expertDao;
        this.clientDao = clientDao;
        this.researchDao = researchDao;
        this.enquiryBidDao = enquiryBidDao;
        this.researchEnquiryDao = researchEnquiryDao;
        this.purchaseDao = purchaseDao;
    }

    public Expert getExpertByAddress(String expertAddress) throws ResourceNotFoundException {
        Expert expert = expertDao.findByAddress(expertAddress);

        if (expert == null) {
            String msg = MessageFormat.format("Expert not found {0}", expertAddress);
            throwException(msg);
        }

        return expert;
    }

    public Client getClientByAddress(String clientAddress) throws ResourceNotFoundException {
        Client client = clientDao.findByAddress(clientAddress);

        if (client == null) {
            String msg = MessageFormat.format("Client not found {0}", clientAddress);
            throwException(msg);
        }

        return client;
    }

    public Research getResearchByUuid(String uuid) throws ResourceNotFoundException {
        Research research = researchDao.findByUuid(uuid);

        if (research == null) {
            String msg = MessageFormat.format("Research not found {0}", uuid);
            throwException(msg);
        }

        return research;
    }

    public EnquiryBid getEnquiryBidById(long enquiryBidId) throws ResourceNotFoundException {
        EnquiryBid enquiryBid = enquiryBidDao.findOne(enquiryBidId);

        if (enquiryBid == null) {
            String msg = MessageFormat.format("Enquiry bid not found {0}", enquiryBidId);
            throwException(msg);
        }

        return enquiryBid;
    }

    public ResearchEnquiry getResearchEnquiryById(long enquiryId) throws ResourceNotFoundException {
        ResearchEnquiry enquiry = researchEnquiryDao.findOne(enquiryId);

        if (enquiry == null) {
            String msg = MessageFormat.format("Enquiry not found {0}", enquiryId);
            throwException(msg);
        }

        return enquiry;
    }

    public Purchase getPurchaseById(long purchaseId) throws ResourceNotFoundException {
        Purchase purchase = purchaseDao.findOne(purchaseId);

        if (purchase == null) {
            String msg = MessageFormat.format("Purchase not found {0}", purchaseId);
            throwException(msg);
        }

        return purchase;
    }

    private Client throwException(String msg) {
        logger.error(msg);
        throw new ResourceNotFoundException(msg);
    }
}
