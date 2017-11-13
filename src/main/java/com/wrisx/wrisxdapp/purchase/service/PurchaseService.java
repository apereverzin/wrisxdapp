package com.wrisx.wrisxdapp.purchase.service;

import com.wrisx.wrisxdapp.data.PurchaseData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.Purchase;
import com.wrisx.wrisxdapp.domain.PurchaseDao;
import com.wrisx.wrisxdapp.domain.RiskExpert;
import com.wrisx.wrisxdapp.domain.RiskExpertDao;
import com.wrisx.wrisxdapp.domain.RiskKnowledge;
import com.wrisx.wrisxdapp.domain.RiskKnowledgeDao;
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
    private final RiskKnowledgeDao riskKnowledgeDao;
    private final RiskExpertDao riskExpertDao;

    @Autowired
    public PurchaseService(PurchaseDao purchaseDao, ClientDao clientDao,
                           RiskKnowledgeDao riskKnowledgeDao, RiskExpertDao riskExpertDao) {
        this.purchaseDao = purchaseDao;
        this.clientDao = clientDao;
        this.riskKnowledgeDao = riskKnowledgeDao;
        this.riskExpertDao = riskExpertDao;
    }

    public PurchaseData createPurchase(String clientAddress, String uuid) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }
        RiskKnowledge riskKnowledge = riskKnowledgeDao.findByUuid(uuid);
        if (riskKnowledge == null) {
            throw new RuntimeException(
                    MessageFormat.format("Risk Knowledge not found {0}", uuid));
        }
        return createPurchase(client, riskKnowledge, riskKnowledge.getPrice());
    }

    public PurchaseData createPurchase(Client client, RiskKnowledge riskKnowledge, int price) {
        Purchase purchase =
                new Purchase(price, client, riskKnowledge.getRiskExpert(), riskKnowledge);
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

    public List<PurchaseData> getRiskExpertPurchases(String expertAddress) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(expertAddress);
        if (riskExpert == null) {
            throw new RuntimeException(
                    MessageFormat.format("Risk expert not found {0}", expertAddress));
        }
        return purchaseDao.findByRiskExpert(riskExpert).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }

    public List<PurchaseData> getRiskKnowledgePurchases(String uuid) {
        RiskKnowledge riskKnowledge = riskKnowledgeDao.findByUuid(uuid);
        if (riskKnowledge == null) {
            throw new RuntimeException(
                    MessageFormat.format("Risk knowledge not found {0}", uuid));
        }
        return purchaseDao.findByRiskKnowledge(riskKnowledge).stream().
                sorted(comparing(Purchase::getTimestamp).reversed()).
                map(purchase -> new PurchaseData(purchase)).
                collect(toList());
    }
}
