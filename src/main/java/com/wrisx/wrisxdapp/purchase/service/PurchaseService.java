package com.wrisx.wrisxdapp.purchase.service;

import com.wrisx.wrisxdapp.data.PurchaseData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.Purchase;
import com.wrisx.wrisxdapp.domain.PurchaseDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import com.wrisx.wrisxdapp.domain.Research;
import com.wrisx.wrisxdapp.domain.ResearchDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class PurchaseService {
    private final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseDao purchaseDao;
    private final ClientDao clientDao;
    private final ResearchDao researchDao;
    private final ExpertDao expertDao;

    @Autowired
    public PurchaseService(PurchaseDao purchaseDao, ClientDao clientDao,
                           ResearchDao researchDao, ExpertDao expertDao) {
        this.purchaseDao = purchaseDao;
        this.clientDao = clientDao;
        this.researchDao = researchDao;
        this.expertDao = expertDao;
    }

    public PurchaseData createPurchase(String clientAddress, String uuid) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }
        Research research = researchDao.findByUuid(uuid);
        if (research == null) {
            throw new RuntimeException(
                    MessageFormat.format("Research not found {0}", uuid));
        }
        return createPurchase(client, research, research.getPrice());
    }

    public PurchaseData createPurchase(Client client, Research research, int price) {
        Purchase purchase =
                new Purchase(price, client, research.getExpert(), research);
        purchase =  purchaseDao.save(purchase);
        return new PurchaseData(purchase);
    }

    public List<PurchaseData> getClientPurchases(String clientAddress) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }
        return purchaseDao.findByClient(client).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }

    public List<PurchaseData> getExpertPurchases(String expertAddress) {
        Expert expert = expertDao.findByAddress(expertAddress);
        if (expert == null) {
            throw new RuntimeException(
                    MessageFormat.format("Expert not found {0}", expertAddress));
        }
        return purchaseDao.findByExpert(expert).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }

    public List<PurchaseData> getResearchPurchases(String uuid) {
        Research research = researchDao.findByUuid(uuid);
        if (research == null) {
            throw new RuntimeException(
                    MessageFormat.format("Research not found {0}", uuid));
        }
        return purchaseDao.findByResearch(research).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }
}
