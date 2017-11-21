package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ExpertDao extends CrudRepository<Expert, Long> {
  public Expert findByAddress(String address);
}
