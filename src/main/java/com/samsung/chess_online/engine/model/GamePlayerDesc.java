package com.samsung.chess_online.engine.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class GamePlayerDesc {
    private final long gameId;
    private final String username;

    public GamePlayerDesc(long id, String username) {
        this.gameId = id;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayerDesc that = (GamePlayerDesc) o;
        return gameId == that.gameId && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, username);
    }
}
