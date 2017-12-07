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
            throwException(MessageFormat.format("Expert not found {0}", expertAddress));
        }

        return expert;
    }

    public Expert getExpertByAddressAndTransactionHash(String expertAddress,
                                                       String transactionHash)
            throws ResourceNotFoundException {
        Expert expert =
                expertDao.findByAddressAndTransactionHash(expertAddress, transactionHash);

        if (expert == null) {
            throwException(MessageFormat.format(
                    "Expert not found {0} {1}", expertAddress, transactionHash));
        }

        return expert;
    }

    public Client getClientByAddress(String clientAddress) throws ResourceNotFoundException {
        Client client = clientDao.findByAddress(clientAddress);

        if (client == null) {
            throwException(MessageFormat.format(
                    "Client not found {0}", clientAddress));
        }

        return client;
    }

    public Client getClientByAddressAndTransactionHash(String clientAddress,
                                                       String transactionHash)
            throws ResourceNotFoundException {
        Client client =
                clientDao.findByAddressAndTransactionHash(clientAddress, transactionHash);

        if (client == null) {
            throwException(MessageFormat.format(
                    "Client not found {0} {1}", clientAddress, transactionHash));
        }

        return client;
    }

    public Research getResearchByUuid(String uuid) throws ResourceNotFoundException {
        Research research = researchDao.findByUuid(uuid);

        if (research == null) {
            throwException(MessageFormat.format("Research not found {0}", uuid));
        }

        return research;
    }

    public Research getResearchByUuidAndTransactionHash(String uuid,
                                                        String transactionHash)
            throws ResourceNotFoundException {
        Research research = researchDao.findByUuidAndTransactionHash(uuid, transactionHash);

        if (research == null) {
            throwException(MessageFormat.format(
                    "Research not found {0} {1}", uuid, transactionHash));
        }

        return research;
    }

    public EnquiryBid getEnquiryBidById(long enquiryBidId) throws ResourceNotFoundException {
        EnquiryBid enquiryBid = enquiryBidDao.findOne(enquiryBidId);

        if (enquiryBid == null) {
            throwException(MessageFormat.format(
                    "Enquiry bid not found {0}", enquiryBidId));
        }

        return enquiryBid;
    }

    public EnquiryBid getEnquiryBidByIdAndTransactionHash(
            long enquiryBidId, String transactionHash)
            throws ResourceNotFoundException {
        EnquiryBid enquiryBid =
                enquiryBidDao.findByIdAndTransactionHash(enquiryBidId, transactionHash);

        if (enquiryBid == null) {
            throwException(MessageFormat.format(
                    "Enquiry bid not found {0} {1}", enquiryBidId, transactionHash));
        }

        return enquiryBid;
    }

    public ResearchEnquiry getResearchEnquiryById(long enquiryId) throws ResourceNotFoundException {
        ResearchEnquiry enquiry = researchEnquiryDao.findOne(enquiryId);

        if (enquiry == null) {
            throwException(MessageFormat.format("Enquiry not found {0}", enquiryId));
        }

        return enquiry;
    }

    public ResearchEnquiry getResearchEnquiryByIdAndTransactionHash(
            long enquiryId, String transactionHash) throws ResourceNotFoundException {
        ResearchEnquiry enquiry =
                researchEnquiryDao.findByIdAndTransactionHash(enquiryId, transactionHash);

        if (enquiry == null) {
            throwException(MessageFormat.format(
                    "Enquiry not found {0} {1}", enquiryId, transactionHash));
        }

        return enquiry;
    }

    public Purchase getPurchaseById(long purchaseId) throws ResourceNotFoundException {
        Purchase purchase = purchaseDao.findOne(purchaseId);

        if (purchase == null) {
            throwException(MessageFormat.format("Purchase not found {0}", purchaseId));
        }

        return purchase;
    }

    public Purchase getPurchaseByIdAndTransactionHash(long purchaseId,
                                                      String transactionHash)
            throws ResourceNotFoundException {
        Purchase purchase =
                purchaseDao.findByIdAndTransactionHash(purchaseId, transactionHash);

        if (purchase == null) {
            throwException(MessageFormat.format(
                    "Purchase not found {0} {1}", purchaseId, transactionHash));
        }

        return purchase;
    }

    private void throwException(String msg) {
        logger.error(msg);
        throw new ResourceNotFoundException(msg);
    }
}
