package com.samsung.chess_online.engine.figures;

import com.samsung.chess_online.engine.model.FigureColor;
import com.samsung.chess_online.engine.model.FigureMove;
import com.samsung.chess_online.engine.model.FigureType;
import com.samsung.chess_online.engine.Game;
import com.samsung.chess_online.engine.model.Position;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class Figure {
    private final Position position;
    private final FigureColor color;
    private final FigureType type;

    public Figure(Position position, FigureColor color, FigureType type) {
        this.position = position;
        this.color = color;
        this.type = type;
    }

    public abstract Figure moveTo(Position position);

    public abstract Set<Position> validFigureMoves(Game game);

    public final Set<Position> possibleFigureMoves(Game game) {
        Set<Position> moves = validFigureMoves(game);

        moves.removeIf(pos ->
                game.at(pos).map(figure -> figure.getColor() == color).orElse(false));
        FigureColor currentPlayer = game.getCurrentPlayer();
        moves.removeIf(move -> game.applyMoveNoValidate(new FigureMove(position, move)).kingUnderAttack(currentPlayer));

        return moves;
    }
}
