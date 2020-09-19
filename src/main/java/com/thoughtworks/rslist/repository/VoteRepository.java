package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VotePo, Integer> {
    @Override
    List<VotePo> findAll();

    List<VotePo> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
}
