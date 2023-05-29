package com.samsung.chess_online.engine.model;

import com.samsung.chess_online.engine.figures.Figure;
import com.samsung.chess_online.engine.Game;
import lombok.Getter;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@Getter
public class Position {
    public static Map<Character, Integer> LETTERS_MAP = Map.of('a', 1, 'b', 2, 'c', 3, 'd', 4, 'e', 5, 'f', 6, 'g', 7, 'h', 8);
    public static String LETTERS = "abcdefgh";

    private final int x;
    private final int y;

    private Position(int x, int y) {
        if (x < 1 || y < 1 || x > 8 || y > 8)
            throw new IllegalArgumentException("Invalid position");
        this.x = x;
        this.y = y;
    }

    public static Position of(String s) {
        if (!s.matches("(?i)[A-H][1-8]")) {
            throw new IllegalArgumentException("Invalid string: " + s);
        }
        int x = LETTERS_MAP.get(s.charAt(0));
        int y = Integer.parseInt(s.substring(1));
        return new Position(x, y);
    }

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public String parseString() {
        return String.valueOf(LETTERS.charAt(x - 1)) + y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return toString();
    }

    public Position up1() {
        return (y < 8) ? of(x, y + 1) : null;
    }

    public Position down1() {
        return (y > 1) ? of(x, y - 1) : null;
    }

    public Position left1() {
        return (x > 1) ? of(x - 1, y) : null;
    }

    public Position right1() {
        return (x < 8) ? of(x + 1, y) : null;
    }

    public Position diagonalUpRight1() {
        return move(Position::up1, Position::right1);
    }

    public Position diagonalUpLeft1() {
        return move(Position::up1, Position::left1);
    }

    public Position diagonalDownRight1() {
        return move(Position::down1, Position::right1);
    }

    public Position diagonalDownLeft1() {
        return move(Position::down1, Position::left1);
    }

    public Stream<Position> up() {
        return Stream.iterate(this, Objects::nonNull, Position::up1).skip(1);
    }

    public Stream<Position> down() {
        return Stream.iterate(this, Objects::nonNull, Position::down1).skip(1);
    }

    public Stream<Position> right() {
        return Stream.iterate(this, Objects::nonNull, Position::right1).skip(1);
    }

    public Stream<Position> left() {
        return Stream.iterate(this, Objects::nonNull, Position::left1).skip(1);
    }


    public Stream<Position> diagonalUpRight() {
        return Stream.iterate(this, Objects::nonNull, Position::diagonalUpRight1).skip(1);
    }

    public Stream<Position> diagonalUpLeft() {
        return Stream.iterate(this, Objects::nonNull, Position::diagonalUpLeft1).skip(1);
    }

    public Stream<Position> diagonalDownLeft() {
        return Stream.iterate(this, Objects::nonNull, Position::diagonalDownLeft1).skip(1);
    }

    public Stream<Position> diagonalDownRight() {
        return Stream.iterate(this, Objects::nonNull, Position::diagonalDownRight1).skip(1);
    }

    public Position move(UnaryOperator<Position>... moves) {
        Position currentPosition = this;
        for (int i = 0; i < moves.length && (currentPosition != null); i++) {
            UnaryOperator<Position> currentMove = moves[i];
            currentPosition = currentMove.apply(currentPosition);
        }
        return currentPosition;
    }

    public Set<Position> moveUntilHit(List<Stream<Position>> moves, Game game, FigureColor color) {
        Set<Position> validMoves = new HashSet<>();
        moves.forEach(line -> {
            for (Position position : (Iterable<Position>) line::iterator) {
                Optional<Figure> figure = game.at(position);

                if(figure.isEmpty() || figure.get().getColor() != color) {
                    validMoves.add(position);
                }
                if(figure.isPresent()) {
                    break;
                }
            }
        });

        return validMoves;
    }

    public List<Position> kingMoves() {
        List<Position> moves = new LinkedList<>();
        moves.add(up1());
        moves.add(down1());
        moves.add(left1());
        moves.add(right1());
        moves.add(diagonalDownLeft1());
        moves.add(diagonalUpLeft1());
        moves.add(diagonalUpRight1());
        moves.add(diagonalDownRight1());

        moves.removeIf(Objects::isNull);
        return moves;
    }

    public List<Position> knightMoves() {
        List<Position> moves = new LinkedList<>();
        moves.add(move(Position::up1, Position::up1, Position::right1));
        moves.add(move(Position::up1, Position::up1, Position::left1));
        moves.add(move(Position::down1, Position::down1, Position::right1));
        moves.add(move(Position::down1, Position::down1, Position::left1));
        moves.add(move(Position::right1, Position::right1, Position::up1));
        moves.add(move(Position::right1, Position::right1, Position::down1));
        moves.add(move(Position::left1, Position::left1, Position::up1));
        moves.add(move(Position::left1, Position::left1, Position::down1));

        moves.removeIf(Objects::isNull);
        return moves;
    }

    public List<Stream<Position>> rookMoves() {
        return Arrays.asList(up(), down(), left(), right());
    }

    public List<Stream<Position>> bishopMoves() {
        return Arrays.asList(diagonalDownLeft(), diagonalUpLeft(), diagonalDownRight(), diagonalUpRight());
    }

    public List<Stream<Position>> queenMoves() {
        List<Stream<Position>> validMoves = new ArrayList<>();
        validMoves.addAll(rookMoves());
        validMoves.addAll(bishopMoves());
        return validMoves;
    }
}
