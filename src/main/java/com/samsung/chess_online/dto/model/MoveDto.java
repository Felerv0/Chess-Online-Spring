package com.samsung.chess_online.dto.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveDto {
    private String from;
    private String to;
    private String promotion;

    public MoveDto() {
    }

    public MoveDto(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public MoveDto(String from, String to, String promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "MoveDto{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", promotion='" + promotion + '\'' +
                '}';
    }
}
