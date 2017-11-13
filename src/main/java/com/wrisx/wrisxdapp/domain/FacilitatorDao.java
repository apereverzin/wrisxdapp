package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface FacilitatorDao extends CrudRepository<Facilitator, Long> {
  public Facilitator findByAddress(String address);
}
