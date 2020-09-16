package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
class UserController {
   public static List<User> userList=initUserList();
   public static List<User> initUserList() {
        userList = new ArrayList<>();
//   List<User> userList=initUserList();
//   public static List<User> initUserList() {
//        List<User> userList = new ArrayList<>();
        userList.add(new User("yzq", "female",18,"a@b.com","12345678912"));
        userList.add(new User("Jack", "male",20,"c@b.com","11111111111"));
        return userList;
    }



   @PostMapping("/user")
   public  void addUser(@RequestBody  @Valid User user){
       userList.add(user);
    }

   @GetMapping("/user")
   public List<User> getUserList(@RequestParam(required = false) Integer start, @RequestParam (required = false) Integer end  ) {
       if(start <= 0 || end > userList.size()){
           throw new RsEventNotValidException("invalid request param");
       }
       if (start == null || end == null) {
           return userList;
       }
       return userList.subList(start - 1, end);
    }



}
