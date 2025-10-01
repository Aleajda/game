package com.example.game.core;

public class Player {
    private PlayerType type;
    private char color; // 'W' или 'B'

    public Player() {}

    public Player(PlayerType type, char color) {
        this.type = type;
        this.color = color;
    }

    public PlayerType getType() {
        return type;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }
}
