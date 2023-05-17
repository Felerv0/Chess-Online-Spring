package com.samsung.chess_online.engine.figures;

import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.engine.model.Position;

import java.util.Set;

public class Rook extends Figure {

    public Rook(Position position, FigureColor color) {
        super(position, color, FigureType.ROOK);
    }

    @Override
    public Figure moveTo(Position position) {
        return null;
    }

    @Override
    public Set<Position> validFigureMoves(Game game) {
        return getPosition().moveUntilHit(getPosition().rookMoves(), game, getColor());
    }
}
