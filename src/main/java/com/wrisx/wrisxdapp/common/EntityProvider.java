package com.wrisx.wrisxdapp.common;

import com.wrisx.wrisxdapp.domain.*;
import com.wrisx.wrisxdapp.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class EntityProvider {
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

    public Expert getExpertByAddress(String expertAddress) throws NotFoundException {
        Expert expert = expertDao.findByAddress(expertAddress);

        if (expert == null) {
            throw new NotFoundException(
                    MessageFormat.format("Expert not found {0}", expertAddress));
        }
        return expert;
    }

    public Client getClientByAddress(String clientAddress) throws NotFoundException {
        Client client = clientDao.findByAddress(clientAddress);

        if (client == null) {
            throw new NotFoundException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }
        return client;
    }

    public Research getResearchByUuid(String uuid) throws NotFoundException {
        Research research = researchDao.findByUuid(uuid);

        if (research == null) {
            throw new NotFoundException(
                    MessageFormat.format("Research not found {0}", uuid));
        }
        return research;
    }

    public EnquiryBid getEnquiryBidById(long enquiryBidId) throws NotFoundException {
        EnquiryBid enquiryBid = enquiryBidDao.findOne(enquiryBidId);

        if (enquiryBid == null) {
            throw new NotFoundException(
                    MessageFormat.format("Enquiry bid not found {0}", enquiryBid));
        }

        return enquiryBid;
    }

    public ResearchEnquiry getResearchEnquiryById(long enquiryId) throws NotFoundException {
        ResearchEnquiry enquiry = researchEnquiryDao.findOne(enquiryId);

        if (enquiry == null) {
            throw new NotFoundException(
                    MessageFormat.format("Enquiry not found {0}", enquiryId));
        }

        return enquiry;
    }

    public Purchase getPurchaseById(long purchaseId) throws NotFoundException {
        Purchase purchase = purchaseDao.findOne(purchaseId);

        if (purchase == null) {
            throw new NotFoundException(
                    MessageFormat.format("Purchase not found {0}", purchaseId));
        }

        return purchase;
    }
}
