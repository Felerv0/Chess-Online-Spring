package com.samsung.chess_online.engine.figures;

import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.engine.model.Position;

import java.util.Set;

public class Queen extends Figure {
    public Queen(Position position, FigureColor color) {
        super(position, color, FigureType.QUEEN);
    }

    @Override
    public Figure moveTo(Position position) {
        return new Queen(position, getColor());
    }

    @Override
    public Set<Position> validFigureMoves(Game game) {
        return getPosition().moveUntilHit(getPosition().queenMoves(), game, getColor());
    }
}
