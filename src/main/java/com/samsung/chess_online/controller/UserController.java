package com.samsung.chess_online.controller;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.services.UserService;
import com.samsung.chess_online.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping("/user")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/user/{id}")
    public User getById(@PathVariable long id) {
        return userService.getById(id);
    }

    @PutMapping("/user")
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/user/{id}")
    public void deleteById(@PathVariable long id) {
        userService.deleteById(id);
    }
}
