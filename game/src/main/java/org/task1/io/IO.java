package org.task1.io;

import org.task1.core.Game;
import org.task1.core.Player;
import org.task1.core.PlayerType;

import java.util.Scanner;

public class IO {

    private static String trim(String s) {
        return (s == null) ? "" : s.strip();
    }

    public static void run(Game game) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        Print.printHelp();

        while (running && scanner.hasNextLine()) {
            String line = trim(scanner.nextLine());
            if (line.isEmpty()) continue;

            String cmd, args = "";
            int sp = line.indexOf(' ');
            if (sp != -1) {
                cmd = line.substring(0, sp);
                args = line.substring(sp + 1);
            } else {
                cmd = line;
            }

            cmd = cmd.toUpperCase();

            switch (cmd) {
                case "EXIT" -> running = false;

                case "HELP" -> Print.printHelp();

                case "GAME" -> handleGameCommand(game, args);

                case "MOVE" -> handleMoveCommand(game, args);

                default -> System.out.println("Incorrect command");
            }
        }
    }

    private static void handleGameCommand(Game game, String args) {
        if (!Parse.parseGameCommand(args, game)) {
            System.out.println("Incorrect command");
        } else {
            System.out.println("New game started");

            if (game.getP1().getType() == PlayerType.COMP) {
                int[] move = game.compMove();
                System.out.printf("%c (%d, %d)%n", game.getP1().getColor(), move[0], move[1]);
                checkGameEnd(game);
            }

            if (game.getP1().getType() == PlayerType.COMP &&
                    game.getP2().getType() == PlayerType.COMP) {
                while (game.isStarted()) {
                    Player cur = game.getCurPlayer();
                    int[] move = game.compMove();
                    System.out.printf("%c (%d, %d)%n", cur.getColor(), move[0], move[1]);

                    if (checkGameEnd(game)) break;

                    game.switchTurn();
                }
            }
        }
    }


    private static void handleMoveCommand(Game game, String args) {
        if (!game.isStarted()) {
            System.out.println("Incorrect command");
            return;
        }

        Player cur = game.getCurPlayer();
        if (cur.getType() != PlayerType.USER) {
            System.out.println("Incorrect command");
            return;
        }

        String[] parts = args.split(",");
        if (parts.length != 2) {
            System.out.println("Incorrect command");
            return;
        }

        try {
            int x = Integer.parseInt(trim(parts[0]));
            int y = Integer.parseInt(trim(parts[1]));
            if (!game.inBounds(x, y) || game.getCell(x, y) != '.') {
                System.out.println("Incorrect command");
            } else {
                game.setCell(x, y, cur.getColor());
                game.incMovesCount();

                if (!checkGameEnd(game)) {
                    game.switchTurn();

                    while (game.isStarted() && game.getCurPlayer().getType() == PlayerType.COMP) {
                        Player cpl = game.getCurPlayer();
                        int[] move = game.compMove();
                        System.out.printf("%c (%d, %d)%n", cpl.getColor(), move[0], move[1]);
                        if (checkGameEnd(game)) break;
                        game.switchTurn();
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Incorrect command");
        }
    }

    private static boolean checkGameEnd(Game game) {
        char winner = game.checkWinner();
        if (winner != '.') {
            System.out.printf("Game finished. %c wins!%n", winner);
            game.setStarted(false);
            return true;
        } else if (game.isBoardFull()) {
            System.out.println("Game finished. Draw");
            game.setStarted(false);
            return true;
        }
        return false;
    }
}
