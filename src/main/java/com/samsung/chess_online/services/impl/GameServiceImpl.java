package com.samsung.chess_online.services.impl;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.dto.GameDto;
import com.samsung.chess_online.dto.model.GameStateDto;
import com.samsung.chess_online.dto.model.MoveDto;
import com.samsung.chess_online.engine.model.FigureMove;
import com.samsung.chess_online.engine.model.GamePlayerDesc;
import com.samsung.chess_online.exception.GameNotFoundException;
import com.samsung.chess_online.repository.GameRepository;
import com.samsung.chess_online.services.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final ConcurrentMap<GamePlayerDesc, Consumer<GameDto>> handlers = new ConcurrentHashMap<>();

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameDto newGame(String player1, String player2) {
        return gameRepository.newGame(player1, player2);
    }

    public GameDto find(long id) {
        return gameRepository.getById(id).orElseThrow(() -> new GameNotFoundException("Game with id: '" + id + "' was not found"));
    }

    public GameDto acceptGame(long id) {
        GameDto gameDto = find(id);
        gameDto.setSecondPlayerAccepted(true);
        gameRepository.save(gameDto);
        return gameDto;
    }

    public GameDto applyMove(long id, MoveDto moveDto) {
        GameDto gameDto = find(id);
        //TODO:Security
        gameDto.applyMove(FigureMove.of(moveDto));
        gameRepository.save(gameDto);
        return gameDto;
    }

    @Override
    public List<GameDto> findByUsername(String username) {
        return gameRepository.getByUsername(username).get();
    }

    @Override
    public List<GameDto> findByUser(User user) {
        return gameRepository.getByUser(user).get();
    }

    public List<GameStateDto> getAll() {
        return gameRepository.getAll().get();
    }


    public void awaitOpponentMove(GamePlayerDesc gamePlayerDesc, Consumer<GameDto> handler) {
        handlers.put(gamePlayerDesc, handler);
    }

    public GameRepository getGameRepository() {
        return gameRepository;
    }
}
