package com.example.game.controller;


import com.example.game.dto.RequestDto;
import com.example.game.dto.ResponseDto;
import com.example.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/nextMove")
    public ResponseDto nextMove(@RequestBody RequestDto request) {
        char nextPlayerColor = request.getNextPlayerColor().charAt(0);
        return gameService.computeNextMove(request.getSize(), request.getData(), nextPlayerColor);
    }
}
