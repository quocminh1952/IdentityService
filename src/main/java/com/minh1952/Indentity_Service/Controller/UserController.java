package com.minh1952.Indentity_Service.Controller;

import com.minh1952.Indentity_Service.Entity.User;
import com.minh1952.Indentity_Service.Service.UserService;
import com.minh1952.Indentity_Service.dto.request.UserCreationRequest;
import com.minh1952.Indentity_Service.dto.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    User createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId ){
        return userService.getUserById(userId);
    }

    @GetMapping
    List<User> getUsers(){
        return userService.getUsers();
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable String userId ,@RequestBody UserUpdateRequest request){
        return userService.updateUser(userId,request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been delete";
    }

}
