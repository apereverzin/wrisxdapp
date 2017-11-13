package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PurchaseDao extends CrudRepository<Purchase, Long> {
  public List<Purchase> findByClient(Client client);
  public List<Purchase> findByRiskExpert(RiskExpert riskExpert);
  public List<Purchase> findByRiskKnowledge(RiskKnowledge riskKnowledge);
  public List<Purchase> findByClientAndRiskKnowledge(Client client, RiskKnowledge riskKnowledge);
}
