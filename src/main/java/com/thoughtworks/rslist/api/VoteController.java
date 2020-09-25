package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteService voteService;

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity postVoteRecord(@PathVariable int rsEventId, @RequestBody @Valid Vote vote) {
        voteService.post(rsEventId, vote);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord
            (@RequestParam int userId, @RequestParam int rsEventId, @RequestParam int pageIndex) {
        List<Vote> votes = voteService.get(userId, rsEventId, pageIndex);
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecordBasedOnTime
            (@RequestParam int userId, @RequestParam int rsEventId, @RequestParam int pageIndex,
             @RequestParam(required = false) Date startDate, @RequestParam (required = false) Date endDate ) {
        List<Vote> votes = voteService.getBasedOnTime(userId, rsEventId, pageIndex, startDate, endDate);
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return ResponseEntity.ok(votes);
    }
}
