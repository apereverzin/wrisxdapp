package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PurchaseDao extends CrudRepository<Purchase, Long> {
  public List<Purchase> findByClient(Client client);
  public List<Purchase> findByExpert(Expert expert);
  public List<Purchase> findByResearch(Research research);
  public List<Purchase> findByClientAndResearch(Client client, Research research);
}
