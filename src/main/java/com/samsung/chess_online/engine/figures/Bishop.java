package com.samsung.chess_online.engine.figures;

import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.engine.model.Position;

import java.util.Set;

public class Bishop extends Figure {
    public Bishop(Position position, FigureColor color) {
        super(position, color, FigureType.BISHOP);
    }

    @Override
    public Set<Position> validFigureMoves(Game game) {
        return getPosition().moveUntilHit(getPosition().bishopMoves(), game, getColor());
    }

    @Override
    public Figure moveTo(Position to) {
        return new Bishop(to, getColor());
    }
}
