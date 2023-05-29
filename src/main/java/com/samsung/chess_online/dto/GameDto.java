package com.samsung.chess_online.dto;

import com.samsung.chess_online.dto.model.FigureDto;
import com.samsung.chess_online.dto.model.GameStateDto;
import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureMove;
import com.samsung.chess_online.engine.model.GamePlayerDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
public class GameDto {
    private long id;
    private String player1;
    private String player2;
    private Map<FigureColor, String> players;
    private Game game;
    private OffsetDateTime matchStarted = OffsetDateTime.now();
    private boolean secondPlayerAccepted = false;

    public GameDto(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        players = setPlayerColors();
    }

    public GameStateDto getGameStateDtoForPlayer(String username) {
        boolean forCurrentPlayer = currentPlayerUsername().equals(username);

        GameStateDto gameStateDto = new GameStateDto();
        gameStateDto.setMyTurn(forCurrentPlayer);
        gameStateDto.setCurrentPlayer(game.getCurrentPlayer().toDto());

        gameStateDto.setFigures(game.calculateFigureDtos(forCurrentPlayer));
        if (!game.getPreviousMoves().isEmpty()) {
            gameStateDto.setLastOpponentMove(game.getPreviousMoves().get(game.getPreviousMoves().size() - 1).toDto());
        }

        switch (game.getStatus()) {
            case CHECK -> {
                gameStateDto.setCheck(true);
            }
            case CHECKMATE -> {
                gameStateDto.setGameFinished(true);
                gameStateDto.setGameFinishedReason("Шах и мат! " + game.getCurrentPlayer().change() + " выиграли!");
                gameStateDto.setWinner(game.getCurrentPlayer().change().toDto());
            }
            case DRAW -> {
                gameStateDto.setGameFinished(true);
                gameStateDto.setGameFinishedReason("Ничья!");
            }
            case NORMAL -> {
            }
        }
        return gameStateDto;
    }

    private GamePlayerDesc getCurrentPlayerDesc() {
        return new GamePlayerDesc(id, currentPlayerUsername());
    }

    private GamePlayerDesc waitingPlayerDesc() {
        return new GamePlayerDesc(id, waitingPlayerUsername());
    }

    public void applyMove(FigureMove figureMove) {
        game = game.applyMove(figureMove);
    }

    public String waitingPlayerUsername() {
        return players.get(game.getCurrentPlayer().change());
    }

    public String currentPlayerUsername() {
        return players.get(game.getCurrentPlayer());
    }

    private Map<FigureColor, String> setPlayerColors() {
        return Map.of(
                FigureColor.WHITE, player1,
                FigureColor.BLACK, player2);
    }

    public boolean isSecondPlayerAccepted() {
        return secondPlayerAccepted;
    }

    public boolean isPlayerParty(String username) {
        return player1.equals(username) || player2.equals(username);
    }

    public void setPlayersUsernames(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
}
