package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
    User findByAddress(String address);
    User findByAddressAndTransactionHash(String address, String transactionHash);
}
