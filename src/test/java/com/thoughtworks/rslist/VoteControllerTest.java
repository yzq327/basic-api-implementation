package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.util.List;

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
        voteRepository.deleteAll();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        userPo = UserPo.builder().name("Joey").age(19)
                .email("a@b.com").gender("male")
                .phone("12345678910").voteNum(10).build();
        userPo = userRepository.save(userPo);
        rsEventPo = RsEventPo.builder().eventName("代码通过啦")
                .keyWord("你真棒").voteNum(8).build();
        rsEventPo = rsEventRepository.save(rsEventPo);
    }

    @Test
    public void should_add_vote_record() throws Exception {
        userPo = UserPo.builder().name("Joey").age(19)
                .email("a@b.com").gender("male")
                .phone("12345678910").voteNum(10).build();
        userPo = userRepository.save(userPo);
        rsEventPo = RsEventPo.builder().eventName("代码通过啦")
                .keyWord("你真棒").voteNum(8).build();
        rsEventPo = rsEventRepository.save(rsEventPo);
        User user = new User("Joey", "male", 28, "hhh@b.com", "18888888888", 5);
        Vote vote = new Vote(userPo.getId(), rsEventPo.getId(), LocalDateTime.now(), 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventPo.getId()).param("rsEventId", String.valueOf(rsEventPo.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<VotePo> all = voteRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals(1,all.get(1).getNum());

    }

    @Test
    public void should_get_vote_record() throws Exception{
        for(int i = 0; i < 8; i++){
            VotePo votePo = VotePo.builder().user(userPo).rsEvent(rsEventPo)
                    .localDateTime(LocalDateTime.now()).num(i+1).build();
            voteRepository.save(votePo);
        }
        mockMvc.perform(get("/voteRecord").param("userId", String.valueOf(userPo.getId()))
                .param("rsEventId", String.valueOf(rsEventPo.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(1)))
                .andExpect(jsonPath("$[1].voteNum",is(2)))
                .andExpect(jsonPath("$[2].voteNum",is(3)))
                .andExpect(jsonPath("$[3].voteNum",is(4)))
                .andExpect(jsonPath("$[4].voteNum",is(5)));
        mockMvc.perform(get("/voteRecord").param("userId", String.valueOf(userPo.getId()))
                .param("rsEventId", String.valueOf(rsEventPo.getId()))
                .param("pageIndex", "2"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(6)))
                .andExpect(jsonPath("$[1].voteNum",is(7)))
                .andExpect(jsonPath("$[2].voteNum",is(8)));
    }

    @Test
    public void should_get_vote_record_based_on_time() throws Exception{
        for(int i = 0; i < 4; i++){
            VotePo votePo = VotePo.builder().user(userPo).rsEvent(rsEventPo)
                    .localDateTime(LocalDateTime.now()).num(i+1).build();
            voteRepository.save(votePo);
        }
        mockMvc.perform(get("/voteRecord?startDate=2020/9/20 00:00:25&endDate=2020/9/23 00:00:25 ").param("userId", String.valueOf(userPo.getId()))
                .param("rsEventId", String.valueOf(rsEventPo.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(1)))
                .andExpect(jsonPath("$[1].voteNum",is(2)))
                .andExpect(jsonPath("$[2].voteNum",is(3)))
                .andExpect(jsonPath("$[3].voteNum",is(4)));
    }
}
