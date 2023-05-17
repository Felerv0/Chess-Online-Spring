package com.samsung.chess_online.engine.model;

import com.samsung.chess_online.dto.model.FigureColorDto;

public enum FigureColor {
    WHITE,
    BLACK;

    public FigureColor change() {
        if (this == WHITE) {
            return BLACK;
        }
        return WHITE;
    }

    public FigureColorDto toDto() {
        return this == WHITE ? FigureColorDto.WHITE : FigureColorDto.BLACK;
    }
}
