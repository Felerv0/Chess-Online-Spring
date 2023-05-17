package com.samsung.chess_online.services;

import com.samsung.chess_online.domain.User;

import java.util.List;

public interface UserService {
    User add(User user);
    List<User> getAll();
    User getById(long id);
    User getByUsername(String username);
    User update(User user);
    void deleteByUsername(String username);
    User findByEmail(String email);
}
