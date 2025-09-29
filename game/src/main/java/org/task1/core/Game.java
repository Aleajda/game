package org.task1.core;

import java.util.Random;

public class Game {
    private char[] cells;
    private int N;
    private int movesCount;
    private int cur; // 1 или 2
    private boolean started;

    private Player p1;
    private Player p2;

    private final Random random = new Random();

    public void free() {
        cells = null;
    }

    public boolean start(int N, Player p1, Player p2) {
        if (N <= 2) return false;
        free();
        this.N = N;
        this.cells = new char[N * N];
        for (int i = 0; i < N * N; i++) {
            cells[i] = '.';
        }
        this.p1 = p1;
        this.p2 = p2;
        this.cur = 1;
        this.started = true;
        this.movesCount = 0;
        return true;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < N && y < N;
    }

    public char getCell(int x, int y) {
        return cells[y * N + x];
    }

    public void setCell(int x, int y, char c) {
        cells[y * N + x] = c;
    }

    public Player getCurPlayer() {
        return (cur == 1) ? p1 : p2;
    }

    public void switchTurn() {
        cur = (cur == 1) ? 2 : 1;
    }

    public boolean isBoardFull() {
        return movesCount >= N * N;
    }

    public char checkWinner() {
        if (checkSquareForColor('W')) return 'W';
        if (checkSquareForColor('B')) return 'B';
        return '.';
    }

    private boolean checkSquareForColor(char C) {
        for (int ay = 0; ay < N; ay++) {
            for (int ax = 0; ax < N; ax++) {
                if (getCell(ax, ay) != C) continue;
                for (int by = ay; by < N; by++) {
                    for (int bx = (by == ay ? ax + 1 : 0); bx < N; bx++) {
                        if (getCell(bx, by) != C) continue;

                        int dx = bx - ax;
                        int dy = by - ay;

                        int cx = ax - dy, cy = ay + dx;
                        int dx2 = bx - dy, dy2 = by + dx;
                        if (inBounds(cx, cy) && inBounds(dx2, dy2) &&
                                getCell(cx, cy) == C && getCell(dx2, dy2) == C) {
                            return true;
                        }

                        cx = ax + dy; cy = ay - dx;
                        dx2 = bx + dy; dy2 = by - dx;
                        if (inBounds(cx, cy) && inBounds(dx2, dy2) &&
                                getCell(cx, cy) == C && getCell(dx2, dy2) == C) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean wouldWin(int x, int y, char C) {
        if (!inBounds(x, y)) return false;
        if (getCell(x, y) != '.') return false;
        setCell(x, y, C); movesCount++;
        boolean ok = checkSquareForColor(C);
        setCell(x, y, '.'); movesCount--;
        return ok;
    }

    private boolean findWinning(char C, int[] result) {
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (getCell(x, y) == '.' && wouldWin(x, y, C)) {
                    result[0] = x;
                    result[1] = y;
                    return true;
                }
            }
        }
        return false;
    }

    public int[] compMove() {
        char C = getCurPlayer().getColor();
        char O = (C == 'W') ? 'B' : 'W';
        int[] res = new int[2];

        if (findWinning(C, res)) {
            setCell(res[0], res[1], C);
            movesCount++;
            return res;
        }
        if (findWinning(O, res)) {
            setCell(res[0], res[1], C);
            movesCount++;
            return res;
        }

        int freecount = 0;
        for (int yy = 0; yy < N; yy++) {
            for (int xx = 0; xx < N; xx++) {
                if (getCell(xx, yy) == '.') freecount++;
            }
        }
        int r = random.nextInt(freecount);
        int idx = 0;
        for (int yy = 0; yy < N; yy++) {
            for (int xx = 0; xx < N; xx++) {
                if (getCell(xx, yy) == '.') {
                    if (idx == r) {
                        setCell(xx, yy, C);
                        movesCount++;
                        res[0] = xx; res[1] = yy;
                        return res;
                    }
                    idx++;
                }
            }
        }
        return new int[]{-1, -1};
    }



    public void incMovesCount() { movesCount++; }

    public char[] getCells() {
        return cells;
    }

    public void setCells(char[] cells) {
        this.cells = cells;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    public int getCur() {
        return cur;
    }

    public void setCur(int cur) {
        this.cur = cur;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }
}
