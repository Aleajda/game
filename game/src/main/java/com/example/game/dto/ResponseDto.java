package com.example.game.dto;

public class ResponseDto {
    private final String result;
    private final char color;
    private final int x;
    private final int y;

    public ResponseDto(String result, char color, int x, int y) {
        this.result = result;
        this.y = y;
        this.x = x;
        this.color = color;
    }


    public String getResult() { return result; }
    public char getColor() { return color; }
    public int getX() { return x; }
    public int getY() { return y; }
}
