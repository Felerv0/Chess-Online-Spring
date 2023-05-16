package com.samsung.chess_online.controller;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.exception.UserAlreadyExistsException;
import com.samsung.chess_online.exception.UserNotFoundException;
import com.samsung.chess_online.services.UserService;
import com.samsung.chess_online.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/user/id/{id}")
    public User getById(@PathVariable long id) {
        return userService.getById(id);
    }

    @GetMapping("/user/{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @PutMapping("/user")
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/user/id/{id}")
    public void deleteById(@PathVariable long id) {
        userService.deleteById(id);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserNotFoundException.class})
    public ResponseEntity<String> handlerUserException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
