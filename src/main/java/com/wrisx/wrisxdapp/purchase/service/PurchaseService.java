package com.wrisx.wrisxdapp.purchase.service;

import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.data.PurchaseData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.Purchase;
import com.wrisx.wrisxdapp.domain.PurchaseDao;
import com.wrisx.wrisxdapp.domain.Research;
import com.wrisx.wrisxdapp.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class PurchaseService {
    private final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseDao purchaseDao;
    private final EntityProvider entityProvider;

    @Autowired
    public PurchaseService(PurchaseDao purchaseDao, EntityProvider entityProvider) {
        this.purchaseDao = purchaseDao;
        this.entityProvider = entityProvider;
    }

    public PurchaseData createPurchase(String clientAddress, String uuid)
            throws NotFoundException {
        Client client = entityProvider.getClientByAddress(clientAddress);
        Research research = entityProvider.getResearchByUuid(uuid);

        return createPurchase(client, research, research.getPrice());
    }

    public PurchaseData createPurchase(Client client, Research research, int price) {
        Purchase purchase =
                new Purchase(price, client, research.getExpert(), research);

        purchase =  purchaseDao.save(purchase);

        return new PurchaseData(purchase);
    }

    @Transactional
    public void deletePurchase(long purchaseId) throws NotFoundException {
        Purchase purchase = entityProvider.getPurchaseById(purchaseId);

        purchaseDao.delete(purchase);
    }

    @Transactional
    public void confirmPurchaseCreation(long purchaseId) throws NotFoundException {
        Purchase purchase = entityProvider.getPurchaseById(purchaseId);

        purchase.setConfirmed(true);

        purchaseDao.save(purchase);
    }

    public List<PurchaseData> getClientPurchases(String clientAddress)
            throws NotFoundException {
        Client client = entityProvider.getClientByAddress(clientAddress);

        return purchaseDao.findByClient(client).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }

    public List<PurchaseData> getExpertPurchases(String expertAddress)
            throws NotFoundException {
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        return purchaseDao.findByExpert(expert).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }

    public List<PurchaseData> getResearchPurchases(String uuid) throws NotFoundException {
        Research research = entityProvider.getResearchByUuid(uuid);

        return purchaseDao.findByResearch(research).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }
}
