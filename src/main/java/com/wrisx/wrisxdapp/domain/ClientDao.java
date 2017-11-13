package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ClientDao extends CrudRepository<Client, Long> {
  public Client findByAddress(String address);
}
