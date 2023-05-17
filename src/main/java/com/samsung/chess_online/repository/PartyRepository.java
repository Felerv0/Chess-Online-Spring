package com.samsung.chess_online.repository;

import com.samsung.chess_online.domain.Party;
import com.samsung.chess_online.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
    Optional<Party> getById(long id);
    Optional<Party> getByUser1(User user);
    Optional<Party> getByUser2(User user);
}
