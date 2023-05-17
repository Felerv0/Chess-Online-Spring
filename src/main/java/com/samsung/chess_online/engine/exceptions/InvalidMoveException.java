package com.samsung.chess_online.engine.exceptions;

public class InvalidMoveException extends GameException{
    public InvalidMoveException() {
        super();
    }
    public InvalidMoveException(String message) {
        super(message);
    }
}
