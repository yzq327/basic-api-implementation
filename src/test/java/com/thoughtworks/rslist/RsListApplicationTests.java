package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
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
import org.springframework.test.annotation.DirtiesContext;
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

    RsController rsController;
    @BeforeEach
    public void SetUp() throws SQLException {
        rsController=new RsController();
    }


    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;


    @DirtiesContext
    @Test
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

    @DirtiesContext
    @Test
    public void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无标签")))
               // .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
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



    @DirtiesContext
    @Test
    public void should_patch_rs_event() throws Exception {
        User user =new User("yzq", "female",18,"a@b.com","12345678912");
        RsEvent rsEvent = new RsEvent("第三条事件patch","实时",1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/3").content(jsonString).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件patch")))
                .andExpect(jsonPath("$[2].keyWord", is("实时")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
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

    @DirtiesContext
    @Test
    public void should_add_rs_with_userName_not_exist_event() throws Exception {
        User user =new User("hhh", "female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜","娱乐",1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_add_users() throws Exception {
        User user =new User("user3", "female",18,"a@b.com","12345678912");
        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/user?start=3&end=3"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user-name",is("user3")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void eventName_should_not_null() throws Exception {
        User user =new User("xiaowang", "female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent(null,"娱乐",1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    public void keyWord_should_not_null() throws Exception {
        User user =new User("xiaowang", "female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜",null,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    public void should_throw_method_argument_not_valid_exception() throws Exception {
        User user =new User("yzq", "female",18,"a@b.com","12345678912");
        RsEvent rsEvent = new RsEvent(null,"经济",1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @DirtiesContext
    @Test
    public void should_throw_rs_event_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_user_exist() throws Exception {
        UserPo savedUser = userRepository.save(UserPo.builder().name("chenhui").age(18).phone("18888888888")
                .email("hhh11@a.com").gender("female").voteNum(10).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": " + savedUser.getId() + "}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(19,all.size());
        assertEquals("猪肉涨价了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
        //assertEquals(savedUser.getId(),all.get(0).getUserId());


    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_user_not_exist() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": 100}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
