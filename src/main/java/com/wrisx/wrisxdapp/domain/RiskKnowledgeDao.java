package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface RiskKnowledgeDao extends CrudRepository<RiskKnowledge, Long> {
  RiskKnowledge findByUuid(String uuid);
    public List<RiskKnowledge> findByRiskExpert(RiskExpert riskExpert);
}
