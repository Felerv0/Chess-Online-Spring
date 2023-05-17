package com.samsung.chess_online.engine.model;

import com.samsung.chess_online.engine.figures.*;

public enum FigureType {
    BISHOP {
        @Override
        public Bishop newFigure(Position position, FigureColor figureColor) {
            return new Bishop(position, figureColor);
        }
    },
    KING {
        @Override
        public King newFigure(Position position, FigureColor figureColor) {
            return new King(position, figureColor);
        }
    },
    KNIGHT {
        @Override
        public Knight newFigure(Position position, FigureColor figureColor) {
            return new Knight(position, figureColor);
        }
    },
    PAWN {
        @Override
        public Pawn newFigure(Position position, FigureColor figureColor) {
            return new Pawn(position, figureColor);
        }
    },
    QUEEN {
        @Override
        public Queen newFigure(Position position, FigureColor figureColor) {
            return new Queen(position, figureColor);
        }
    },
    ROOK {
        @Override
        public Rook newFigure(Position position, FigureColor figureColor) {
            return new Rook(position, figureColor);
        }
    };

    public Figure newFigure(Position position, FigureColor figureColor) {
        throw new UnsupportedOperationException();
    }
}