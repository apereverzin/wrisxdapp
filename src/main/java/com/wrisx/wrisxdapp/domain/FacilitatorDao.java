package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface FacilitatorDao extends CrudRepository<Facilitator, Long> {
    Facilitator findByAddress(String address);
    Facilitator findByAddressAndTransactionHash(String address, String transactionHash);
}
