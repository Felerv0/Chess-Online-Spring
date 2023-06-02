package com.samsung.chess_online.controller;

import com.samsung.chess_online.dto.GameDto;
import com.samsung.chess_online.dto.model.*;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.exception.GameNotFoundException;
import com.samsung.chess_online.repository.GameRepository;
import com.samsung.chess_online.security.SecurityUser;
import com.samsung.chess_online.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {
    private final GameService gameService;
    private final Logger log = LoggerFactory.getLogger(GameController.class);

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/sample")
    public GameStateDto gameStateSample() {
        GameStateDto gameStateDto = new GameStateDto();
        gameStateDto.setCurrentPlayer(FigureColorDto.WHITE);
        FigureDto pawn1 = new FigureDto();
        pawn1.setColor(FigureColorDto.WHITE);
        pawn1.setFigureType(FigureTypeDto.PAWN);
        pawn1.setPosition("e2");
        pawn1.setValidMoves(Arrays.asList("e3", "e4"));

        FigureDto pawnBlack = new FigureDto();
        pawnBlack.setColor(FigureColorDto.BLACK);
        pawnBlack.setFigureType(FigureTypeDto.PAWN);
        pawnBlack.setPosition("f7");

        gameStateDto.setFigures(Arrays.asList(pawn1, pawnBlack));

        return gameStateDto;
    }

    @GetMapping("/{username}/matches")
    public List<GameStateDto> getGames(@PathVariable("username") String username) {
        List<GameDto> list = gameService.findByUsername(username);
        List<GameStateDto> list1 = new ArrayList<>();
        for (GameDto gameDto : list) {
            list1.add(gameDto.getGameStateDtoForPlayer("user"));
        }
        return list1;
    }

    @GetMapping("/{id}/get")
    public GameStateDto getGame(@PathVariable("id") long id) {
        GameDto gameDto = gameService.find(id);
        String username = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (gameDto.isPlayerParty(username)) {
            return gameDto.getGameStateDtoForPlayer(username);
        }
        return null;
    }

    @GetMapping("/{id}/accept")
    public void accept(@PathVariable("id") long id) {
        GameDto gameDto = gameService.find(id);
        if (gameDto.isPlayerParty(((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()))
            gameService.acceptGame(id);
    }

    @PostMapping("/{id}/move")
    public GameStateDto move(@PathVariable("id") long id, @RequestBody MoveDto moveDto) {
        GameDto gameDto = gameService.find(id);
        String username = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (gameDto.isPlayerParty(username)) {
            GameDto gameDtoAfterMove = gameService.applyMove(id, moveDto);
            return gameDtoAfterMove.getGameStateDtoForPlayer(username);
        }
        return null;
    }

    @GetMapping("/invite/{username}")
    public GameStateDto invite(@PathVariable("username") String username) {
        log.debug(username);
        if (!username.equals(((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername())) {
            GameDto gameDto = gameService.newGame(
                    ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),
                    username);
            return gameDto.getGameStateDtoForPlayer(((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        }
        return null;
    }

    @GetMapping("/all")
    public List<GameStateDto> getAll() {
        return gameService.getAll();
    }
}