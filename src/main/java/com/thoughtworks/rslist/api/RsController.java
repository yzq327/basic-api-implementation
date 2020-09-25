package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class RsController {
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  RsService rsService;
  @Autowired
  VoteRepository voteRepository;



  public RsController()  {
  }



  /*@PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Validated RsEvent rsEvent) throws JsonProcessingException {
    Optional<UserPo> userPo = userRepository.findById(rsEvent.getUserID());
    if( !userPo.isPresent()){
        return  ResponseEntity.badRequest().build();
    }
    RsEventPo rsEventPo = RsEventPo.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName())
            .userPo(userPo.get()).build();
    rsEventRepository.save(rsEventPo);
    return ResponseEntity.created(null).build();
  }*/

  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity patchRsEvent (@PathVariable int rsEventId, @RequestBody  @Valid RsEvent rsEvent)  {
      rsService.patch(rsEventId, rsEvent);
    return ResponseEntity.created(null).build();
  }

/* @GetMapping("/rs/event")
  public ResponseEntity<List<RsEvent>> getEventListBetween(
          @RequestParam (required = false) Integer start, @RequestParam (required = false)  Integer end){

    List<RsEvent> rsEvents =
            rsEventRepository.findAll().stream().map(item ->
                    RsEvent.builder().eventName(item.getEventName()).keyWord(item.getKeyWord())
                            .userID(item.getId()).voteNum(item.getVoteNum()).build())
                    .collect(Collectors.toList());

   if (start == null || end == null) {
      return ResponseEntity.ok(rsEvents);
    }
    return ResponseEntity.ok(rsEvents.subList(start - 1, end));
  }*/
 @CrossOrigin
 @GetMapping("/event")
  public ResponseEntity<List<RsEvent>> getEventListBetween(
          @RequestParam (required = false) Integer start, @RequestParam (required = false)  Integer end){

    List<RsEvent> rsEvents =
            rsEventRepository.findAll().stream().map(item ->
                    RsEvent.builder().eventName(item.getEventName()).keyWord(item.getKeyWord())
                            .userID(item.getId()).voteNum(item.getVoteNum()).build())
                    .collect(Collectors.toList());

   if (start == null || end == null) {
      return ResponseEntity.ok(rsEvents);
    }
    return ResponseEntity.ok(rsEvents.subList(start - 1, end));
  }

    @CrossOrigin
  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) {
      rsService.get(index);
      return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int rsEventId)  {
      rsService.delete(rsEventId);
      return ResponseEntity.created(null).build();
  }

    @CrossOrigin
  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Validated RsEvent rsEvent) {
      rsService.add(rsEvent);
      return ResponseEntity.ok().build();
  }
  @PostMapping("/event")
  public  ResponseEntity addUser(@RequestBody  @Valid RsEvent rsEvent){
        Optional<UserPo> userPo = userRepository.findById(rsEvent.getUserID());
        if( !userPo.isPresent()){
            throw new RuntimeException();
        } else {
            RsEventPo rsEventPo = RsEventPo.builder()
                    .keyWord(rsEvent.getKeyWord())
                    .eventName(rsEvent.getEventName())
                    .userPo(userPo.get()).build();
            rsEventRepository.save(rsEventPo);
        }
        return ResponseEntity.ok().build();
    }



  @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody Vote vote) {
        rsService.vote(vote, rsEventId);
        return ResponseEntity.ok().build();
    }
}