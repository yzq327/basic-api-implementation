package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public VoteService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void post(int rsEventId, Vote vote){
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
    }

    public List<Vote> get(int userId, int rsEventId, int pageIndex)  {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return voteRepository.findAccordingToUserAndRsEvent(userId, rsEventId, pageable).stream().map(
                        item -> Vote.builder().userId(item.getUser().getId())
                                .time(item.getLocalDateTime())
                                .rsEventId(item.getRsEvent().getId())
                                .voteNum(item.getNum()).build()
                ).collect(Collectors.toList());
    }
    public List<Vote> getBasedOnTime(int userId, int rsEventId, int pageIndex, Date startDate, Date endDate)  {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return voteRepository.findAccordingToUserAndRsEvent(userId, rsEventId, pageable).stream().map(
                item -> Vote.builder().userId(item.getUser().getId())
                        .time(item.getLocalDateTime())
                        .rsEventId(item.getRsEvent().getId())
                        .voteNum(item.getNum()).build()
        ).collect(Collectors.toList());
    }


}
