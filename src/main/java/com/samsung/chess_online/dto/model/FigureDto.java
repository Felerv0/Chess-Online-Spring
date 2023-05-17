package com.samsung.chess_online.dto.model;

import com.samsung.chess_online.engine.model.FigureType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FigureDto {
    private String position;
    private FigureColorDto color;
    private FigureTypeDto figureType;

    private List<String> validMoves;
}
