package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface FacilitatorApprovalDao extends CrudRepository<FacilitatorApproval, Long> {
    List<FacilitatorApproval> findByResearch(Research research);
}
