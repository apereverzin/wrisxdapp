package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ExpertDao extends CrudRepository<Expert, Long> {
    Expert findByAddress(String address);
    Expert findByAddressAndTransactionHash(String address, String transactionHash);
}
