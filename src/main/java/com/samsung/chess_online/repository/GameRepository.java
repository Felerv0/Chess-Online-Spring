package com.samsung.chess_online.repository;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.dto.GameDto;
import com.samsung.chess_online.dto.model.GameStateDto;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    GameDto newGame(String player1, String player2);
    Optional<GameDto> getById(long id);
    Optional<List<GameDto>> getByUser(User user);
    Optional<List<GameDto>> getByUsername(String username);
    void save(GameDto gameDto);
    Optional<List<GameStateDto>> getAll();
}
