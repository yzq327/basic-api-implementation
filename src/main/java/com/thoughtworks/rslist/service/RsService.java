package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RsService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public RsService(RsEventRepository rsEventRepository,UserRepository userRepository,VoteRepository voteRepository){
        this.rsEventRepository=rsEventRepository;
        this.userRepository=userRepository;
        this.voteRepository=voteRepository;
    }

    public void vote(Vote vote, int rsEventId) {
        Optional<RsEventPo> rsEventPo = rsEventRepository.findById(rsEventId);
        Optional<UserPo> userPo = userRepository.findById(vote.getUserId());
        if(!rsEventPo.isPresent()
            || !userPo.isPresent()
            || vote.getVoteNum() > userPo.get().getVoteNum()){
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
    }


}
