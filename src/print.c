#include "print.h"
#include <stdio.h>

void print_help(void) {
    puts("Commands:");
    puts("GAME N, U1, U2   - start new game, N>2, Ux = 'user W' or 'comp B'");
    puts("MOVE X, Y        - make move at coordinates");
    puts("HELP             - show this help");
    puts("EXIT             - exit program");
}

void print_board(Game *g) {
    printf("Board:\n");
    for (int y=0; y<g->N; y++) {
        for (int x=0; x<g->N; x++) {
            putchar(game_get_cell(g,x,y));
            if (x+1<g->N) putchar(' ');
        }
        putchar('\n');
    }
}
