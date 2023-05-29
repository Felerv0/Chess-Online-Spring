package com.samsung.chess_online.engine.model;

import com.samsung.chess_online.dto.model.MoveDto;

import java.util.Optional;

public class FigureMove {
    private final Position from;
    private final Position to;
    private final FigureType promotion;

    public FigureMove(Position from, Position to) {
        this.from = from;
        this.to = to;
        this.promotion = null;
    }

    public FigureMove(Position from, Position to, FigureType promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    public static FigureMove of(MoveDto moveDto) {
        FigureType promotion = (moveDto.getPromotion() != null && !moveDto.getPromotion().isEmpty())
                ? FigureType.valueOf(moveDto.getPromotion().toUpperCase())
                : null;
        return new FigureMove(Position.of(moveDto.getFrom()), Position.of(moveDto.getTo()), promotion);
    }

    public MoveDto toDto() {
        return new MoveDto(from.parseString(), to.parseString(),
                Optional.ofNullable(promotion).map(Enum::name).orElse(null));
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public FigureType getPromotion() {
        return promotion;
    }
}
