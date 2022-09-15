package com.yw.controller;

import com.yw.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("users")
    public List<User> selectAllUsers() {
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId(1);
            user.setName(String.format("name%s", i));
            user.setAddress(String.format("address%s", i));
            user.setEmail(String.format("name%s@gmail.com", i));
            userList.add(user);
        }
        return userList;
    }
}
