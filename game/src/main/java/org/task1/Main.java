package org.task1;

import org.task1.core.Game;
import org.task1.io.IO;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        IO.run(game);
    }
}
