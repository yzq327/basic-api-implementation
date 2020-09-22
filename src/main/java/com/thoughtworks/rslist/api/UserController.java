package com.thoughtworks.rslist.api;


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



@RestController
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

   @PostMapping("/user")
   public  ResponseEntity addUser(@RequestBody  @Valid User user){
       userService.add(user);
       return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getOneUser(@PathVariable int id ) {
        userService.get(id);
        return ResponseEntity.created(null).build();
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable int id)  {
        userService.delete(id);
        return ResponseEntity.created(null).build();
    }
}
