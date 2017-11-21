package com.wrisx.wrisxdapp.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ResearchDao extends CrudRepository<Research, Long> {
  Research findByUuid(String uuid);
  List<Research> findByExpert(Expert expert);
}
