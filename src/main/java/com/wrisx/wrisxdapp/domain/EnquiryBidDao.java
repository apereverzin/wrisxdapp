package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EnquiryBidDao extends CrudRepository<EnquiryBid, Long> {
  public List<EnquiryBid> findByRiskKnowledgeEnquiry(RiskKnowledgeEnquiry riskKnowledgeEnquiry);
  public List<EnquiryBid> findByRiskExpert(RiskExpert riskExpert);
}
