package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public UserService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void add(User user){
        UserPo userPo = new UserPo();
        userPo.setName(user.getName());
        userPo.setAge(user.getAge());
        userPo.setGender(user.getGender());
        userPo.setEmail(user.getEmail());
        userPo.setPhone(user.getPhone());
        userPo.setVoteNum(user.getVoteNum());
        userRepository.save(userPo);
    }

    public void get(int id){
        userRepository.findById(id);
    }

    public void delete(int id){
        userRepository.deleteById(id);
    }
}
