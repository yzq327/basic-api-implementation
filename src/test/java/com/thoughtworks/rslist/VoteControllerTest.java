package com.thoughtworks.rslist;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;
    UserPo userPo;
    RsEventPo rsEventPo;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
        userPo = UserPo.builder().name("Joey").age(19)
                .email("a@b.com").gender("male")
                .phone("12345678910").voteNum(10).build();
        userPo = userRepository.save(userPo);
        rsEventPo = RsEventPo.builder().eventName("代码通过啦")
                .keyWord("你真棒").voteNum(8).build();
        rsEventPo = rsEventRepository.save(rsEventPo);
    }

    @Test
    public void should_get_vote_record() throws Exception{
        VotePo votePo = VotePo.builder().user(userPo).rsEvent(rsEventPo)
                .localDateTime(LocalDateTime.now()).num(5).build();
        voteRepository.save(votePo);
        mockMvc.perform(get("/voteRecord").param("userId", String.valueOf(userPo.getId()))
                .param("rsEventId", String.valueOf(rsEventPo.getId())))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(5)));
    }
}
