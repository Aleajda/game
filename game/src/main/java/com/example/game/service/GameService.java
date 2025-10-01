package com.example.game.service;

import com.example.game.core.Game;
import com.example.game.core.Player;
import com.example.game.core.PlayerType;
import com.example.game.dto.ResponseDto;
import com.example.game.exception.InvalidGameDataException;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public ResponseDto computeNextMove(int size, String data, char nextPlayerColor) {
        if (data.length() != Math.pow(size, 2)) throw new InvalidGameDataException("Size or data is incorrect");
        Game game = new Game();

        Player p1 = new Player(PlayerType.COMP, 'B');
        Player p2 = new Player(PlayerType.COMP, 'W');
        game.start(size, p1, p2);

        char[] cells = new char[size * size];
        String cleaned = data.toUpperCase().replace(" ", ".");
        for (int i = 0; i < size * size; i++) {
            cells[i] = cleaned.charAt(i);
        }
        game.setCells(cells);

        int count = 0;
        for (char c : cells) {
            if (c != '.') count++;
        }
        game.setMovesCount(count);

        nextPlayerColor = Character.toUpperCase(nextPlayerColor);
        if (nextPlayerColor == 'B') game.setCur(1);
        else game.setCur(2);

        char winner = game.checkWinner();
        if (winner != '.') return new ResponseDto("win", winner, -1, -1);

        if (game.isBoardFull()) return new ResponseDto("draw", '.', -1, -1);

        int[] move = game.compMove();
        return new ResponseDto("move", nextPlayerColor, move[0], move[1]);
    }
}
