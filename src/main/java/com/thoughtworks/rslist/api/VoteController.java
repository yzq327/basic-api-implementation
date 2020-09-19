package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord
            (@RequestParam int userId, @RequestParam int rsEventId ,@RequestParam int pageIndex){
        Pageable pageable = PageRequest.of(pageIndex-1, 5);
        return ResponseEntity.ok(
                voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId, pageable).stream().map(
                        item -> Vote.builder().userId(item.getUser().getId())
                                .time(item.getLocalDateTime())
                                .rsEventId(item.getRsEvent().getId())
                                .voteNum(item.getNum()).build()
                ).collect(Collectors.toList()));
    }
}
