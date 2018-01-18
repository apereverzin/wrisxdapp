package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ResearchBinaryDao extends CrudRepository<ResearchBinary, Long> {
    Client findByUuid(String uuid);
}
