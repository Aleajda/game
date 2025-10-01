package com.example.game.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RequestDto {

    @NotNull(message = "Field 'size' must not be null")
    private int size;
    @NotNull(message = "Field 'data' must not be null")
    private String data;
    @NotEmpty(message = "Field 'nextPlayerColor' must not be empty")
    private String nextPlayerColor;


    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getNextPlayerColor() { return nextPlayerColor; }
    public void setNextPlayerColor(String nextPlayerColor) { this.nextPlayerColor = nextPlayerColor; }
}
