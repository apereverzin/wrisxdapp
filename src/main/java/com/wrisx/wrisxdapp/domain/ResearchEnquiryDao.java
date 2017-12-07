package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ResearchEnquiryDao extends CrudRepository<ResearchEnquiry, Long> {
    ResearchEnquiry findByIdAndTransactionHash(long enquiryBidId, String transactionHash);
    List<ResearchEnquiry> findByClient(Client client);
}
