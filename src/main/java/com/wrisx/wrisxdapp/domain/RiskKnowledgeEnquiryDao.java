package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface RiskKnowledgeEnquiryDao extends CrudRepository<RiskKnowledgeEnquiry, Long> {
  public List<RiskKnowledgeEnquiry> findByClient(Client client);
}
