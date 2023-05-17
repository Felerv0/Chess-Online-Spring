package com.samsung.chess_online.engine.figures;

import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureMove;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.engine.model.Position;

import java.util.*;
import java.util.function.UnaryOperator;

public class Pawn extends Figure {
    final static Map<FigureColor, Integer> START_LINE = Map.of(
            FigureColor.WHITE, 2,
            FigureColor.BLACK, 7
    );
    final static Map<FigureColor, Integer> ENPASSANT_LINE = Map.of(
            FigureColor.WHITE, 5,
            FigureColor.BLACK, 4
    );
    public final static Map<FigureColor, UnaryOperator<Position>> MOVE_FOWARD = Map.of(
            FigureColor.WHITE, Position::up1,
            FigureColor.BLACK, Position::down1
    );
    public final static Map<FigureColor, UnaryOperator<Position>> MOVE_BACKWARD = Map.of(
            FigureColor.WHITE, Position::down1,
            FigureColor.BLACK, Position::up1
    );

    public Pawn(Position position, FigureColor playerColor) {
        super(position, playerColor, FigureType.PAWN);
    }

    @Override
    public Set<Position> validFigureMoves(Game game) {
        Set<Position> moves = new HashSet<>();

        UnaryOperator<Position> moveForward = MOVE_FOWARD.get(getColor());
        Position f1 = moveForward.apply(getPosition());

        if (f1 == null) {
            return Collections.emptySet();
        }

        Position f2 = moveForward.apply(f1);

        Position fl = f1.left1();
        Position fr = f1.right1();

        if (game.empty(f1)) {
            moves.add(f1);
        }
        if (fl != null && game.at(fl).map(figure -> figure.getColor() != getColor()).orElse(false)) {
            moves.add(fl);
        }
        if (fr != null && game.at(fr).map(figure -> figure.getColor() != getColor()).orElse(false)) {
            moves.add(fr);
        }
        if (isAtStartLine() && game.empty(f1) && game.empty(f2)) {
            moves.add(f2);
        }

        enPassant(game).ifPresent(moves::add);

        return moves;
    }

    private Optional<Position> enPassant(Game game) {
        if (!ENPASSANT_LINE.get(getColor()).equals(getPosition().getY())
                || game.getPreviousMoves().isEmpty()) {
            return Optional.empty();
        }
        UnaryOperator<Position> moveForward = MOVE_FOWARD.get(getColor());
        UnaryOperator<Position> moveBackward = MOVE_BACKWARD.get(getColor());
        Position fl = moveForward.apply(getPosition()).left1();
        Position fr = moveForward.apply(getPosition()).right1();

        for (Position forwardAttack : new Position[]{fl, fr}) {
            if (forwardAttack != null) {
                Position near = moveBackward.apply(forwardAttack);
                Position nearFrom = moveForward.apply(forwardAttack);

                FigureMove lastOppMove = game.getPreviousMoves().get(game.getPreviousMoves().size() - 1);
                if (game.at(near).map(p -> p.getType() == FigureType.PAWN && p.getColor() != getColor()).orElse(false)
                        && lastOppMove.getFrom().equals(nearFrom)
                        && lastOppMove.getTo().equals(near)) {
                    return Optional.of(forwardAttack);
                }
            }
        }

        return Optional.empty();
    }

    private boolean isAtStartLine() {
        return START_LINE.get(getColor()).equals(getPosition().getY());
    }

    @Override
    public Figure moveTo(Position to) {
        return new Pawn(to, getColor());
    }
}
