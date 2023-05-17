package com.samsung.chess_online.engine.figures;

import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.engine.model.Position;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Figure {
    public Knight(Position position, FigureColor color) {
        super(position, color, FigureType.KNIGHT);
    }


    @Override
    public Figure moveTo(Position position) {
        return new Knight(position, getColor());
    }

    @Override
    public Set<Position> validFigureMoves(Game game) {
        return new HashSet<>(getPosition().knightMoves());
    }
}
