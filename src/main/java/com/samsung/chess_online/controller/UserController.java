package com.samsung.chess_online.controller;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.dto.UserDto;
import com.samsung.chess_online.exception.UserAlreadyExistsException;
import com.samsung.chess_online.exception.UserNotFoundException;
import com.samsung.chess_online.services.UserService;
import com.samsung.chess_online.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public UserDto add(@RequestBody UserDto user) {
        return UserDto.toDto(userService.add(UserDto.fromDto(user)));
    }

    @GetMapping("/user")
    public List<UserDto> getAll() {
        List<UserDto> lst = new ArrayList<>();
        for (User user : userService.getAll()) {
            lst.add(UserDto.toDto(user));
        }
        return lst;
    }

    @GetMapping("/user/id/{id}")
    public UserDto getById(@PathVariable long id) {
        return UserDto.toDto(userService.getById(id));
    }

    @GetMapping("/user/{username}")
    public UserDto getByUsername(@PathVariable String username) {
        return UserDto.toDto(userService.getByUsername(username));
    }

    @PutMapping("/user")
    public UserDto update(@RequestBody UserDto user) {
        return UserDto.toDto(userService.update(UserDto.fromDto(user)));
    }

    @DeleteMapping("/user/id/{id}")
    public void deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserNotFoundException.class})
    public ResponseEntity<String> handlerUserException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
