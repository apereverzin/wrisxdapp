package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PurchaseDao extends CrudRepository<Purchase, Long> {
    Purchase findByIdAndTransactionHash(long purchaseId, String transactionHash);
    List<Purchase> findByClient(Client client);
    List<Purchase> findByExpert(Expert expert);
    List<Purchase> findByResearch(Research research);
    List<Purchase> findByClientAndResearch(Client client, Research research);
}
