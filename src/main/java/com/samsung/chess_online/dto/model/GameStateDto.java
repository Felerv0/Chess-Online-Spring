package com.samsung.chess_online.dto.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameStateDto {
    private long id;
    private List<FigureDto> figures;
    private FigureColorDto currentPlayer;
    private MoveDto lastOpponentMove;

    private String user1;
    private String user2;

    private boolean myTurn = false;
    private boolean gameFinished = false;
    private boolean check = false;
    private FigureColorDto winner = null;
    private String gameFinishedReason = null;

    public boolean isCheck() {
        return check;
    }

    public boolean isMyTurn() {
        return myTurn;
    }
}
