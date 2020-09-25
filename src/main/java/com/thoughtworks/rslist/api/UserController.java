package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;




@RestController
@CrossOrigin
class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserService userService;
    /*@GetMapping("/user")
    public List<User> getUserList(@RequestParam(required = false) Integer start, @RequestParam (required = false) Integer end  ) {
        if(start <= 0 || end > userList.size()){
            throw new RsEventNotValidException("invalid request param");
        }
        if (start == null || end == null) {
            return userList;
        }
        return userList.subList(start - 1, end);
    }*/

    @CrossOrigin
   @PostMapping("/user")
   public  ResponseEntity addUser(@RequestBody  @Valid User user){
       userService.add(user);
       return ResponseEntity.created(null).build();
    }
    @CrossOrigin
    @GetMapping("/user/{id}")
    public ResponseEntity getOneUser(@PathVariable int id ) {
        userService.get(id);
        return ResponseEntity.created(null).build();
    }
    /*@CrossOrigin
    @GetMapping("/user")
    public ResponseEntity<List<RsEvent>>  getList() {

        List<User> users =userRepository.findAll().stream().map(item ->
                        User.builder().name(item.getName()).age(item.getAge()).gender(item.getGender())
                        .email(item.getEmail()).phone(item.getPhone()).voteNum(item.getVoteNum()).build()
                         .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }*/

    @CrossOrigin
    @GetMapping("/user")
    public ResponseEntity<List<UserPo>> getList() {
        List<UserPo> users =userRepository.findAll();
        return ResponseEntity.ok(users);
    }


    @CrossOrigin
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable int id)  {
        userService.delete(id);
        return ResponseEntity.created(null).build();
    }
}
