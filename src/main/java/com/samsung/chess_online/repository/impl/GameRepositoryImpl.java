package com.samsung.chess_online.repository.impl;

import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.dto.GameDto;
import com.samsung.chess_online.dto.model.GameStateDto;
import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameRepositoryImpl implements GameRepository {
    private long lastId = 0;
    private Map<Long, GameDto> gameDtoMap = new ConcurrentHashMap<>();

    @Override
    public GameDto newGame(String player1, String player2) {
        Game game = Game.startGame();
        GameDto gameDto = new GameDto(player1, player2);
        gameDto.setGame(game);
        gameDto.setId(generateId());

        gameDtoMap.put(gameDto.getId(), gameDto);
        return gameDto;
    }

    @Override
    public Optional<GameDto> getById(long id) {
        return Optional.ofNullable(gameDtoMap.get(id));
    }

    @Override
    public Optional<List<GameDto>> getByUser(User user) {
        List<GameDto> list = new ArrayList<>();
        for (Map.Entry<Long, GameDto> entry : gameDtoMap.entrySet()) {
            if (entry.getValue().isPlayerParty(user.getUsername()))
                list.add(entry.getValue());
        }
        return Optional.of(list);
    }

    @Override
    public Optional<List<GameDto>> getByUsername(String username) {
        List<GameDto> list = new ArrayList<>();
        for (Map.Entry<Long, GameDto> entry : gameDtoMap.entrySet()) {
            if (entry.getValue().isPlayerParty(username))
                list.add(entry.getValue());
        }
        return Optional.of(list);
    }

    @Override
    public void save(GameDto gameDto) {
        gameDtoMap.put(gameDto.getId(), gameDto);
    }

    public Optional<List<GameStateDto>> getAll() {
        List<GameStateDto> list = new ArrayList<>();
        for (Map.Entry<Long, GameDto> entry : gameDtoMap.entrySet()) {
            list.add(entry.getValue().getGameStateDtoForPlayer(entry.getValue().currentPlayerUsername()));
        }
        return Optional.of(list);
    }

    private long generateId() {
        lastId++;
        return lastId;
    }
}
