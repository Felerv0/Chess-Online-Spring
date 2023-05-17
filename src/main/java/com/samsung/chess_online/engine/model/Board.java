package com.samsung.chess_online.engine.model;

import com.samsung.chess_online.engine.figures.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board implements Cloneable {
    final Map<Position, Figure> whiteFigures = new HashMap<>();
    final Map<Position, Figure> blackFigures = new HashMap<>();

    public Board() {
    }

    public Board(Collection<Figure> figures) {
        whiteFigures.putAll(figures.stream().filter(figure -> figure.getColor() == FigureColor.WHITE)
                .collect(Collectors.toMap(Figure::getPosition, Function.identity())));
        blackFigures.putAll(figures.stream().filter(figure -> figure.getColor() == FigureColor.BLACK)
                .collect(Collectors.toMap(Figure::getPosition, Function.identity())));
    }

    public static Board startBoard() {
        Board board = new Board();

        Stream.iterate(Position.of(1, 2), Position::right1).limit(8)
                .forEach(figure -> board.put(new Pawn(figure, FigureColor.WHITE)));
        Stream.iterate(Position.of(1, 7), Position::right1).limit(8)
                .forEach(figure -> board.put(new Pawn(figure, FigureColor.BLACK)));

        board.put(new Rook(Position.of(1, 1), FigureColor.WHITE));
        board.put(new Rook(Position.of(8, 1), FigureColor.WHITE));
        board.put(new Rook(Position.of(1, 8), FigureColor.BLACK));
        board.put(new Rook(Position.of(8, 8), FigureColor.BLACK));

        board.put(new Knight(Position.of(2, 1), FigureColor.WHITE));
        board.put(new Knight(Position.of(7, 1), FigureColor.WHITE));
        board.put(new Knight(Position.of(2, 8), FigureColor.BLACK));
        board.put(new Knight(Position.of(7, 8), FigureColor.BLACK));

        board.put(new Bishop(Position.of(3, 1), FigureColor.WHITE));
        board.put(new Bishop(Position.of(6, 1), FigureColor.WHITE));
        board.put(new Bishop(Position.of(3, 8), FigureColor.BLACK));
        board.put(new Bishop(Position.of(6, 8), FigureColor.BLACK));

        board.put(new Queen(Position.of(4, 1), FigureColor.WHITE));
        board.put(new Queen(Position.of(4, 8), FigureColor.BLACK));

        board.put(new King(Position.of(5, 1), FigureColor.WHITE));
        board.put(new King(Position.of(5, 8), FigureColor.BLACK));

        return board;
    }


    public Position king(FigureColor side) {
        return figures(side).stream()
                .filter(figure -> figure.getType() == FigureType.KING)
                .findAny()
                .map(Figure::getPosition)
                .orElseThrow();
    }

    public Collection<Figure> figures() {
        return Stream.of(whiteFigures.values(), blackFigures.values())
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Collection<Figure> figures(FigureColor side) {
        return side == FigureColor.WHITE ? whiteFigures.values() : blackFigures.values();
    }

    public Map<Position, Figure> figureMap(FigureColor side) {
        return side == FigureColor.WHITE ? whiteFigures : blackFigures;
    }

    public Board applyMoveNoValidate(FigureMove figureMove) {
        Board cloned = this.clone();
        if (isCastling(figureMove)) {
            cloned.applyCastling(figureMove);
        } else if (isEnPassant(figureMove)) {
            cloned.applyEnPassant(figureMove);
        } else {
            cloned.applyStandardMove(figureMove);
        }
        if (figureMove.getPromotion() != null) {
            cloned.applyPromotion(figureMove);
        }

        return cloned;
    }

    private boolean isCastling(FigureMove figureMove) {
        return at(figureMove.getFrom()).orElseThrow().getType() == FigureType.KING
                && Math.abs(figureMove.getFrom().getX() - figureMove.getTo().getX()) > 1;
    }

    private void applyCastling(FigureMove figureMove) {
        Position from = figureMove.getFrom();
        Position to = figureMove.getTo();
        Position rockFrom;
        Position rockTo;

        if (to.getX() < from.getX()) {
            rockFrom = to.left1().left1();
            rockTo = to.right1();
        } else {
            rockFrom = to.right1();
            rockTo = to.left1();
        }

        FigureColor color = at(from).orElseThrow().getColor();
        Map<Position, Figure> playerFigures = figureMap(color);

        moveTo(playerFigures, from, to);
        moveTo(playerFigures, rockFrom, rockTo);
    }

    private boolean isEnPassant(FigureMove figureMove) {
        return at(figureMove.getFrom()).orElseThrow().getType() == FigureType.PAWN
                && figureMove.getFrom().getX() != figureMove.getTo().getX()
                && at(figureMove.getTo()).isEmpty();
    }

    private void applyEnPassant(FigureMove figureMove) {
        FigureColor figureColor = at(figureMove.getFrom()).orElseThrow().getColor();

        UnaryOperator<Position> moveBackward = Pawn.MOVE_BACKWARD.get(figureColor);
        Position near = moveBackward.apply(figureMove.getTo());

        moveTo(figureMap(figureColor), figureMove.getFrom(), figureMove.getTo());
        figureMap(figureColor.change()).remove(near);
    }

    private void applyPromotion(FigureMove figureMove) {
        FigureColor figureColor = at(figureMove.getTo()).orElseThrow().getColor();
        figureMap(figureColor).put(figureMove.getTo(),
                figureMove.getPromotion().newFigure(figureMove.getTo(), figureColor));
    }

    private void applyStandardMove(FigureMove figureMove) {
        Position from = figureMove.getFrom();
        Position to = figureMove.getTo();
        FigureColor color = at(from).orElseThrow().getColor();

        Map<Position, Figure> playerFigures = figureMap(color);
        Map<Position, Figure> opponentFigures = figureMap(color.change());

        opponentFigures.remove(to);
        moveTo(playerFigures, from, to);
    }

    private void moveTo(Map<Position, Figure> playerFigures, Position from, Position to) {
        playerFigures.put(to, playerFigures.get(from).moveTo(to));
        playerFigures.remove(from);
    }

    public Optional<Figure> at(Position position) {
        return whiteFigures.containsKey(position)
                ? Optional.of(whiteFigures.get(position))
                : Optional.ofNullable(blackFigures.get(position));
    }

    void put(Figure figure) {
        if (figure.getColor() == FigureColor.WHITE) {
            whiteFigures.put(figure.getPosition(), figure);
        } else if (figure.getColor() == FigureColor.BLACK) {
            blackFigures.put(figure.getPosition(), figure);
        }
    }

    @Override
    public Board clone() {
        Board cloned = new Board();
        cloned.whiteFigures.putAll(whiteFigures);
        cloned.blackFigures.putAll(blackFigures);

        return cloned;
    }
}