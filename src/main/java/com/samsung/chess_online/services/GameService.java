package com.samsung.chess_online.services;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.dto.GameDto;
import com.samsung.chess_online.dto.model.GameStateDto;
import com.samsung.chess_online.dto.model.MoveDto;

import java.util.List;

public interface GameService {
    GameDto newGame(String player1, String player2);
    GameDto find(long id);
    GameDto acceptGame(long id);
    GameDto applyMove(long id, MoveDto moveDto);
    List<GameDto> findByUsername(String username);
    List<GameDto> findByUser(User user);
    List<GameStateDto> getAll();
}
