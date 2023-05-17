package com.samsung.chess_online.services.impl;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.exception.UserAlreadyExistsException;
import com.samsung.chess_online.exception.UserNotFoundException;
import com.samsung.chess_online.repository.UserRepository;
import com.samsung.chess_online.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional()
    public User add(User user) {
        // encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (repository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException("user with email: " + user.getEmail() + " already exists");
        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("user with id '" + id + "' doesn't exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user with username '" + username + "' doesn't exist"));
    }

    @Override
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.deleteByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user with email " + email + " doesn't exist"));
    }
}
