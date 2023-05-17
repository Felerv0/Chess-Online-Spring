package com.samsung.chess_online.engine;

import com.samsung.chess_online.dto.model.FigureDto;
import com.samsung.chess_online.dto.model.FigureTypeDto;
import com.samsung.chess_online.engine.exceptions.InvalidMoveException;
import com.samsung.chess_online.engine.figures.Figure;
import com.samsung.chess_online.engine.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Game {
    private final FigureColor currentPlayer;
    private final List<FigureMove> previousMoves;
    private final List<Board> previousStates;
    private final Board board;

    private boolean finished = false;
    private FigureColor winner = null;
    private GameStatus status = GameStatus.NORMAL;
    private Map<Position, Set<Position>> validMovesForCurrentPlayer;

    public Game(FigureColor currentPlayer, List<Board> previousStates, List<FigureMove> previousMoves, Board board) {
        this.previousStates = previousStates;
        this.currentPlayer = currentPlayer;
        this.previousMoves = previousMoves;
        this.board = board;
    }

    public Optional<Figure> at(Position position) {
        return board.at(position);
    }

    public boolean empty(Position p) {
        return board.at(p).isEmpty();
    }

    public static Game startGame() {
        Game game = new Game(FigureColor.WHITE, Collections.emptyList(), Collections.emptyList(), Board.startBoard());
        game.validMovesForCurrentPlayer = game.validMovesForCurrentPlayer();

        return game;
    }

    public List<FigureDto> calculateFigureDtos(boolean withValidMoves) {
        return board.figures()
                .stream()
                .map(p -> {
                    FigureDto figureDto = new FigureDto();
                    figureDto.setFigureType(FigureTypeDto.valueOf(p.getType().name()));
                    figureDto.setPosition(p.getPosition().parseString());
                    figureDto.setColor(p.getColor().toDto());
                    if(withValidMoves) {
                        if (validMovesForCurrentPlayer.containsKey(p.getPosition())) {
                            figureDto.setValidMoves(validMovesForCurrentPlayer.get(p.getPosition()).stream()
                                    .map(Position::parseString).collect(Collectors.toList()));
                        }
                    }
                    return figureDto;
                }).collect(Collectors.toList());
    }

    public Game applyMove(FigureMove figureMove) {
        ensureMoveValid(figureMove);
        Game gameAfterMove = applyMoveNoValidate(figureMove);
        gameAfterMove.updateGameStatus();
        return gameAfterMove;
    }

    public void updateGameStatus() {
        this.validMovesForCurrentPlayer = validMovesForCurrentPlayer();

        boolean kingAttacked = kingUnderAttack(currentPlayer);
        boolean canMove = validMovesForCurrentPlayer.values().stream().anyMatch(s -> !s.isEmpty());

        if (canMove && kingAttacked) {
            status = GameStatus.CHECK;
        }
        if (!canMove && kingAttacked) {
            status = GameStatus.CHECKMATE;
            finished = true;
        }
        if (!canMove && !kingAttacked) {
            status = GameStatus.DRAW;
            finished = true;
        }
    }

    private Map<Position, Set<Position>> validMovesForCurrentPlayer() {
        return board.figureMap(currentPlayer).values().stream()
                .collect(Collectors.toMap(Figure::getPosition, figure -> figure.possibleFigureMoves(this)));
    }

    private void ensureMoveValid(FigureMove figureMove) throws InvalidMoveException {
        Figure figure = at(figureMove.getFrom()).orElseThrow(() -> new InvalidMoveException("No figure on " + figureMove.getFrom().toString()));
        if (figureMove.getPromotion() != null
                && (figure.getType() != FigureType.PAWN || figureMove.getTo().getY() != 1 && figureMove.getTo().getY() != 8)) {
            throw new InvalidMoveException("Figure is not at finish line");
        }

        Set<Position> validMoves = figure.possibleFigureMoves(this);
        if (!validMoves.contains(figureMove.getTo())) {
            throw new InvalidMoveException("Figure is not allowed to be moved to: " + figureMove.getTo().toString());
        }
    }

    public Game applyMoveNoValidate(FigureMove figureMove) {
        Board boardAfterMove = board.applyMoveNoValidate(figureMove);
        List<Board> states = new ArrayList<>(previousStates);
        states.add(board);

        List<FigureMove> moves = new ArrayList<>(previousMoves);
        moves.add(figureMove);

        return new Game(currentPlayer.change(), states, moves, boardAfterMove);
    }

    public boolean isUnderAttack(Position position) {
        Set<Position> attackPositions =
                board.figures(currentPlayer.change()).stream()
                        .flatMap(figure -> figure.possibleFigureMoves(this).stream())
                        .collect(Collectors.toSet());
        return attackPositions.contains(position);
    }

    public boolean kingUnderAttack(FigureColor player) {

        Set<Position> attackPositions =
                board.figures(player.change()).stream()
                        .flatMap(p -> p.possibleFigureMoves(this).stream())
                        .collect(Collectors.toSet());

        return attackPositions.contains(board.king(player));
    }

    public boolean isFinished() {
        return finished;
    }

}
