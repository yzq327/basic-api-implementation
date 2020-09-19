package com.thoughtworks.rslist;


import com.fasterxml.jackson.databind.ObjectMapper;


import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void setUp(){
       userRepository.deleteAll();
       rsEventRepository.deleteAll();
       objectMapper=new ObjectMapper();
    }

    @Test
    public void neme_should_less_than_8() throws Exception {
        User user =new User("yzq123456", "female",18,"a@b.com","12345678912");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isBadRequest()));
    }

    @Test
    public void gender_should_not_null() throws Exception {
        User user =new User("yzq", null,19,"a@b.com","12345678912");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isBadRequest()));
    }

    @Test
    public void age_should_between_18_and_100() throws Exception {
        User user =new User("yzq", "female",10,"a@b.com","12345678912");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isBadRequest()));
    }

    @Test
    public void email_should_suit_format() throws Exception {
        User user =new User("yzq", "female",19,"ab.com","12345678912");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isBadRequest()));
    }

    @Test
    public void tel_should_suit_format() throws Exception {
        User user =new User("yzq", "female",19,"a@b.com","123456789120");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isBadRequest()));
    }

    @Test
    public void should_get_user_list() throws Exception{
        mockMvc.perform(get("/user?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user-name",is("yzq")))
                .andExpect(jsonPath("$[0].user-gender",is("female")))
                .andExpect(jsonPath("$[0].user-age",is(18)))
                .andExpect(jsonPath("$[0].user-email",is("a@b.com")))
                .andExpect(jsonPath("$[0].user-phone",is("12345678912")))
                .andExpect(jsonPath("$[1].user-name",is("Jack")))
                .andExpect(jsonPath("$[1].user-gender",is("male")))
                .andExpect(jsonPath("$[1].user-age",is(20)))
                .andExpect(jsonPath("$[1].user-email",is("c@b.com")))
                .andExpect(jsonPath("$[1].user-phone",is("11111111111")))
                .andExpect(status().isOk());
    }


    @Test
    public void should_throw_invalid_request_param_exceptio() throws Exception{
        mockMvc.perform(get("/user?start=0&end=2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    public void should_throw_method_argument_not_valid_user_exception() throws Exception {
        User user =new User("Joeyhhhhhhhhh", "male",28,"hhh@b.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.error", is("invalid param")));

    }

    @Test
    public void should_register_user() throws Exception {
        User user =new User("Joey", "male",28,"hhh@b.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()));
        List<UserPo> all = userRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("Joey",all.get(0).getName());
        assertEquals("male",all.get(0).getGender());
    }

    @Test
    public void should_get_one_user() throws Exception{
        UserPo userPo = UserPo.builder().voteNum(10).phone("19999999999").name("daiyu")
                .age(20).gender("famale").email("www.@1.com").build();
        userRepository.save(userPo);
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk());
        Optional<UserPo> byId = userRepository.findById(4);
        assertEquals("daiyu",byId.get().getName());
        assertEquals("famale",byId.get().getGender());
    }

    @Test
    public void should_delete_one_user() throws Exception{
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isCreated());
        List<UserPo> all = userRepository.findAll();;
        assertEquals(0,all.size());
    }

    @Test
    public void should_delete_user() throws Exception{
        UserPo userPo = UserPo.builder().voteNum(10).phone("19999999999").name("daiyu")
                .age(20).gender("famale").email("www.@1.com").build();
        userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().keyWord("经济").eventName("涨工资了").userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
        mockMvc.perform(delete("/user/{id}",userPo.getId()))
                .andExpect(status().isCreated());
        assertEquals(0,userRepository.findAll().size());
        assertEquals(0, rsEventRepository.findAll().size());
    }

}


