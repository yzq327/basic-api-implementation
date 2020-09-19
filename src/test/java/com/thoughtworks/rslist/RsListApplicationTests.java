package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void SetUp() throws SQLException {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

   /* @Test
    public void should_get_rs_event_list() throws Exception{
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
              //  .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
              //  .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无标签")))
               // .andExpect(jsonPath("$[2]",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无标签")))
               // .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
              //  .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
               // .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(status().isOk());

    }


    @Test
    public void should_delete_rs_event() throws Exception {
        mockMvc.perform(delete("/rs/3")).andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }


    @Test
    public void eventName_should_not_null() throws Exception {
        User user =new User("xiaowang", "female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent(null,"娱乐",1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void keyWord_should_not_null() throws Exception {
        User user =new User("xiaowang", "female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜",null,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_rs_event_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    public void should_add_rs_event_when_user_exist() throws Exception {
        UserPo savedUser = userRepository.save(UserPo.builder().name("chenhui").age(19).phone("18888888888")
                .email("hhh11@a.com").gender("female").voteNum(19).build());
//        String jsonString ="{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": " + savedUser.getId() + "}";\
        User user =new User("Joey", "male",28,"hhh@b.com","18888888888");
        RsEvent rsEvent =new RsEvent("涨工资了","经济",savedUser.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals("涨工资了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
        assertEquals(savedUser.getId(),all.get(0).getUserPo().getId());
    }

    @Test
    public void should_add_rs_event_when_user_not_exist() throws Exception {
        String jsonString =
                "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": 100}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_patch_rs_event_when_user_exist() throws Exception {
        UserPo userPo = UserPo.builder().voteNum(10).phone("19999999999").name("daiyu")
                .age(20).gender("famale").email("www.@1.com").build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().keyWord("幻想").eventName("中彩票啦").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        RsEvent rsEvent =new RsEvent("涨工资了","经济",userPo.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",userPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(2,all.size());
        assertEquals("中彩票啦",all.get(0).getEventName());
        assertEquals("幻想",all.get(0).getKeyWord());
        assertEquals(userPo.getId(),all.get(0).getUserPo().getId());
        assertEquals(userPo.getId(),all.get(1).getUserPo().getId());
    }

    @Test
    public void should_patch_rs_event_when_user_not_exist() throws Exception {
        UserPo userPo = UserPo.builder().voteNum(10).phone("19999999999").name("daiyu")
                .age(20).gender("famale").email("www.@1.com").build();
        userRepository.save(userPo);
        UserPo userPo1 = UserPo.builder().voteNum(10).phone("19999999111").name("xxxx")
                .age(20).gender("famale").email("www.@1.com").build();
        userRepository.save(userPo1);
        RsEventPo rsEventPo = RsEventPo.builder().keyWord("幻想").eventName("中彩票啦").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        RsEvent rsEvent =new RsEvent("涨工资了","经济",userPo1.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",userPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_patch_rs_event_when_eventName_is_null() throws Exception {
        UserPo userPo = UserPo.builder().voteNum(10).phone("19999999999").name("daiyu")
                .age(20).gender("famale").email("www.@1.com").build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().keyWord("幻想").eventName("中彩票啦").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        RsEvent rsEvent =new RsEvent(null,"经济",userPo.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",userPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(2,all.size());
        assertEquals("中彩票啦",all.get(0).getEventName());
        assertEquals("幻想",all.get(0).getKeyWord());
        assertEquals(userPo.getId(),all.get(0).getUserPo().getId());
        assertEquals(userPo.getId(),all.get(1).getUserPo().getId());
    }

    @Test
    public void should_patch_rs_event_when_keyword_is_null() throws Exception {
        UserPo userPo = UserPo.builder().voteNum(10).phone("19999999999").name("daiyu")
                .age(20).gender("famale").email("www.@1.com").build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().keyWord("幻想").eventName("中彩票啦").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        RsEvent rsEvent =new RsEvent("台湾回归了",null,userPo.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}",userPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(2,all.size());
        assertEquals("中彩票啦",all.get(0).getEventName());
        assertEquals("幻想",all.get(0).getKeyWord());
        assertEquals(userPo.getId(),all.get(0).getUserPo().getId());
        assertEquals(userPo.getId(),all.get(1).getUserPo().getId());
    }*/

}
