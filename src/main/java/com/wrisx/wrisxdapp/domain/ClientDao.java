package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ClientDao extends CrudRepository<Client, Long> {
    Client findByAddress(String address);
    Client findByAddressAndTransactionHash(String address, String transactionHash);
}
