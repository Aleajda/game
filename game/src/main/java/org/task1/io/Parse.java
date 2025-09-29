package org.task1.io;

import org.task1.core.Game;
import org.task1.core.Player;
import org.task1.core.PlayerType;

public class Parse {
    private static String trim(String s) {
        return (s == null) ? "" : s.strip();
    }

    public static boolean parsePlayer(String s, Player p) {
        if (s == null) return false;
        String[] parts = trim(s).split(" ");
        if (parts.length != 2) return false;

        String type = parts[0].toLowerCase();
        String color = parts[1].toUpperCase();

        if (type.equals("user")) {
            p.setType(PlayerType.USER);
        } else if (type.equals("comp")) {
            p.setType(PlayerType.COMP);
        } else {
            return false;
        }

        if (color.length() != 1 || (!color.equals("W") && !color.equals("B"))) return false;
        p.setColor(color.charAt(0));
        return true;
    }

    public static boolean parseGameCommand(String args, Game g) {
        if (args == null) return false;
        String[] parts = args.split(",");
        if (parts.length != 3) return false;

        int N;
        try {
            N = Integer.parseInt(trim(parts[0]));
        } catch (NumberFormatException e) {
            return false;
        }
        if (N <= 2) return false;

        Player p1 = new Player();
        Player p2 = new Player();
        if (!parsePlayer(parts[1], p1) || !parsePlayer(parts[2], p2)) return false;
        if (p1.getColor() == p2.getColor()) return false;

        return g.start(N, p1, p2);
    }
}
