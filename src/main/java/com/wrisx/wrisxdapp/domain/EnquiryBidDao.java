package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EnquiryBidDao extends CrudRepository<EnquiryBid, Long> {
  List<EnquiryBid> findByResearchEnquiry(ResearchEnquiry researchEnquiry);
  List<EnquiryBid> findByResearch(Research research);
  List<EnquiryBid> findByExpert(Expert expert);
}
