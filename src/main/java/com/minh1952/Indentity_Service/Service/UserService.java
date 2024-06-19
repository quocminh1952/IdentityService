package com.minh1952.Indentity_Service.Service;

import com.minh1952.Indentity_Service.Entity.User;
import com.minh1952.Indentity_Service.Repository.userRepository;
import com.minh1952.Indentity_Service.dto.request.UserCreationRequest;
import com.minh1952.Indentity_Service.dto.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private userRepository userRepository;

    public User createUser(UserCreationRequest request){

        User user = new User();

        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("User name Existed");

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(String id){
        //Nếu Null thì trả về Exception
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    public User updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }

    public void deleteUser(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        userRepository.delete(user);
    }


}
