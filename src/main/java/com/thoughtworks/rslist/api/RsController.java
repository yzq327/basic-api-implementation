package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.zaxxer.hikari.util.FastList;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class RsController {

  private List<RsEvent> rsList = initRsEventList();


  private List<RsEvent> initRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    User user =new User("yzq", "female",18,"a@b.com","12345678912");
    rsEventList.add(new RsEvent("第一条事件","无标签",user));
    rsEventList.add(new RsEvent("第二条事件","无标签",user));
    rsEventList.add(new RsEvent("第三条事件","无标签",user));
    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index) throws Exception {
    if(index<1 || index > rsList.size()){
      throw new IndexOutOfBoundsException();
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
    String userName= rsEvent.getUser().getName();
    boolean isNotExist=true;
    for (User user : UserController.userList){
      if (user.getName().equals(userName)) {
         isNotExist=false;
      }
    }
    if(isNotExist){
      UserController.userList.add(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    int newRsIndex=rsList.size()-1;
   // return ResponseEntity.created(null)
    //        .header("index",Integer.toString(newRsIndex)).build();
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