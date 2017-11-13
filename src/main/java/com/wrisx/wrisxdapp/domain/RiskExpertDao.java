package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface RiskExpertDao extends CrudRepository<RiskExpert, Long> {
  public RiskExpert findByAddress(String address);
}
