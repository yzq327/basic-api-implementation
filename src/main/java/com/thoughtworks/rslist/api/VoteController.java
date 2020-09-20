package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
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

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    /*@PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity postVoteRecord(@PathVariable int rsEventId, @RequestBody @Valid Vote vote) {
        Optional<RsEventPo> rsEventPo = rsEventRepository.findById(rsEventId);
        Optional<UserPo> userPo = userRepository.findById(vote.getUserId());
        if (!rsEventPo.isPresent()
                || !userPo.isPresent()
                || vote.getVoteNum() > userPo.get().getVoteNum()) {
            throw new RuntimeException();
        }
        VotePo votePo = VotePo.builder().localDateTime(vote.getTime())
                .num(vote.getVoteNum()).rsEvent(rsEventPo.get())
                .user(userPo.get()).build();
        voteRepository.save(votePo);
        UserPo user = userPo.get();
        user.setVoteNum(user.getVoteNum() - vote.getVoteNum());
        userRepository.save(user);
        RsEventPo rsEvent = rsEventPo.get();
        rsEvent.setEventName(rsEvent.getEventName() + vote.getVoteNum());
        rsEventRepository.save(rsEvent);
        return ResponseEntity.ok().build();
    }*/

   /* @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord
            (@RequestParam int userId, @RequestParam int rsEventId, @RequestParam int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return ResponseEntity.ok(
                voteRepository.findAccordingToUserAndRsEvent(userId, rsEventId, pageable).stream().map(
                        item -> Vote.builder().userId(item.getUser().getId())
                                .time(item.getLocalDateTime())
                                .rsEventId(item.getRsEvent().getId())
                                .voteNum(item.getNum()).build()
                ).collect(Collectors.toList()));
    }*/

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord
            (@RequestParam int userId, @RequestParam int rsEventId, @RequestParam int pageIndex,
             @RequestParam(required = false) Date startDate, @RequestParam (required = false) Date endDate ) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return ResponseEntity.ok(
                voteRepository.findAccordingToUserAndRsEvent(userId, rsEventId, pageable).stream().map(
                        item -> Vote.builder().userId(item.getUser().getId())
                                .time(item.getLocalDateTime())
                                .rsEventId(item.getRsEvent().getId())
                                .voteNum(item.getNum()).build()
                ).collect(Collectors.toList()));
    }
}
