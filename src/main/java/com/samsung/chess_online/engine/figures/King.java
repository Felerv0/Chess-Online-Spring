package com.samsung.chess_online.engine.figures;

import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.engine.model.Position;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class King extends Figure {
    final static Map<FigureColor, Position> START_POSITION = Map.of(
            FigureColor.WHITE, Position.of(5,1),
            FigureColor.BLACK, Position.of(5,8)
    );

    public King(Position position, FigureColor playerColor) {
        super(position, playerColor, FigureType.KING);
    }

    @Override
    public Set<Position> validFigureMoves(Game game) {
        Set<Position> moves = new HashSet<>();
        moves.addAll(getPosition().kingMoves());
        if(game.getCurrentPlayer() == getColor()) {
            moves.addAll(castling(game).collect(Collectors.toList()));
        }

        return moves;
    }
    public Stream<Position> castling(Game game) {
        if(getPosition().equals(START_POSITION.get(getColor())) && neverMoved(getPosition(), game)) {
            return Stream.of(castlingLeft(game), castlingRight(game))
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        } else {
            return Stream.empty();
        }
    }

    private Optional<Position> castlingLeft(Game game) {

        Position l1 = getPosition().left1();
        Position l2 = l1.left1();
        Position l3 = l2.left1();
        Position rock = l3.left1();

        if(game.empty(l1)
                && game.empty(l2)
                && game.empty(l3)
                && neverMoved(rock, game)
                && !game.isUnderAttack(getPosition())
                && !game.isUnderAttack(l1)
                && !game.isUnderAttack(l2)) {

            return Optional.of(l2);
        }

        return Optional.empty();
    }

    private Optional<Position> castlingRight(Game game) {
        Position r1 = getPosition().right1();
        Position r2 = r1.right1();
        Position rock = r2.right1();

        if(game.empty(r1)
                && game.empty(r2)
                && neverMoved(rock, game)
                && !game.isUnderAttack(getPosition())
                && !game.isUnderAttack(r1)
                && !game.isUnderAttack(r2)) {

            return Optional.of(r2);
        }

        return Optional.empty();
    }

    private boolean neverMoved(Position position, Game game) {
        return game.getPreviousMoves().stream()
                .noneMatch(m -> m.getFrom().equals(position));
    }


    @Override
    public Figure moveTo(Position to) {
        return new King(to, getColor());
    }
}
