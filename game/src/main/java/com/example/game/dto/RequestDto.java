package com.example.game.dto;

public class RequestDto {
    private int size;
    private String data;
    private String nextPlayerColor;

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getNextPlayerColor() { return nextPlayerColor; }
    public void setNextPlayerColor(String nextPlayerColor) { this.nextPlayerColor = nextPlayerColor; }
}
