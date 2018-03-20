package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
    User findByEmailAddressAndPassword(String emailAddress, String password);
    User findByAddress(String address);
    User findByAddressAndTransactionHash(String address, String transactionHash);
}
