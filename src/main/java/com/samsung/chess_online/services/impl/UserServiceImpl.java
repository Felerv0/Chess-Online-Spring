package com.samsung.chess_online.services.impl;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.exception.UserAlreadyExistsException;
import com.samsung.chess_online.exception.UserNotFoundException;
import com.samsung.chess_online.repository.UserRepository;
import com.samsung.chess_online.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional()
    public User add(User user) {
        // encrypt password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (repository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException("user with email: " + user.getEmail() + " already exists");
        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User getById(long id) {
        return repository.findById(id).orElse(new User());
    }

    @Override
    public User update(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user with email " + email + " was not found"));
    }
}
