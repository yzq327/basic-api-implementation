package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.zaxxer.hikari.util.FastList;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class RsController {

  private List<RsEvent> rsList = initRsEventList();
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;


  public RsController()  {
  }


  private static List<RsEvent> initRsEventList() {
    User user =new User("yzq", "female",18,"a@b.com","12345678912");
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("第一条事件","无标签",1));
    rsEventList.add(new RsEvent("第二条事件","无标签",1));
    rsEventList.add(new RsEvent("第三条事件","无标签",1));
    return rsEventList;
  }


  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index) throws Exception {
    if(index<1 || index > rsList.size()){
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getOneRsEvent(@RequestParam (required = false) Integer start, @RequestParam (required = false)  Integer end){
    if (start == null || end == null) {
      return ResponseEntity.ok(rsList);
    }

    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Validated RsEvent rsEvent) throws JsonProcessingException {
    Optional<UserPo> userPo = userRepository.findById(rsEvent.getUserID());
    if( !userPo.isPresent()){
        return  ResponseEntity.badRequest().build();
    }
    RsEventPo rsEventPo = RsEventPo.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName())
            .userPo(userPo.get()).build();
    rsEventRepository.save(rsEventPo);
    return ResponseEntity.created(null).build();
  }


  @PatchMapping("/rs/{index}")
  public ResponseEntity patchRsEvent (@RequestBody RsEvent rsEvent, @PathVariable int index)  {
    if(index < 1 || index > rsList.size()){
      throw new IndexOutOfBoundsException();
    }
    rsList.set(index-1,rsEvent);
    return ResponseEntity.created(null).build();
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index)  {
    if(index < 1 || index > rsList.size()){
      throw new IndexOutOfBoundsException();
    }
    rsList.remove(index-1);
    return ResponseEntity.created(null).build();
  }

}