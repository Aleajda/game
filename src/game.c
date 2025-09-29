#include "game.h"
#include <stdlib.h>

static int check_square_for_color(Game *g, char C);

void game_init(Game *g) {
    g->cells = NULL;
    g->started = 0;
    g->N = 0;
    g->moves_count = 0;
}

void game_free(Game *g) {
    if (g->cells) free(g->cells);
    g->cells = NULL;
}

int game_start(Game *g, int N, Player p1, Player p2) {
    if (N <= 2) return 0;
    game_free(g);
    g->N = N;
    g->cells = malloc(N*N);
    for (int i = 0; i < N*N; i++) g->cells[i] = '.';
    g->p1 = p1;
    g->p2 = p2;
    g->cur = 1;
    g->started = 1;
    g->moves_count = 0;
    return 1;
}

int game_in_bounds(Game *g, int x, int y) {
    return x >= 0 && y >= 0 && x < g->N && y < g->N;
}

char game_get_cell(Game *g, int x, int y) {
    return g->cells[y * g->N + x];
}

void game_set_cell(Game *g, int x, int y, char c) {
    g->cells[y * g->N + x] = c;
}

Player *game_cur_player(Game *g) {
    return (g->cur == 1) ? &g->p1 : &g->p2;
}

Player *game_other_player(Game *g) {
    return (g->cur == 1) ? &g->p2 : &g->p1;
}

void game_switch_turn(Game *g) {
    g->cur = (g->cur == 1) ? 2 : 1;
}

int game_board_full(Game *g) {
    return g->moves_count >= g->N * g->N;
}

int game_check_winner(Game *g, char *out_color) {
    if (check_square_for_color(g, 'W')) { *out_color = 'W'; return 1; }
    if (check_square_for_color(g, 'B')) { *out_color = 'B'; return 1; }
    return 0;
}

/* --- Проверка квадрата (любой ориентации) --- */
static int check_square_for_color(Game *g, char C) {
    int N = g->N;
    for (int ay = 0; ay < N; ay++) {
        for (int ax = 0; ax < N; ax++) {
            if (game_get_cell(g, ax, ay) != C) continue;
            for (int by = ay; by < N; by++) {
                for (int bx = (by == ay ? ax + 1 : 0); bx < N; bx++) {
                    if (game_get_cell(g, bx, by) != C) continue;

                    int dx = bx - ax;
                    int dy = by - ay;

                    // поворот влево
                    int cx = ax - dy, cy = ay + dx;
                    int dx2 = bx - dy, dy2 = by + dx;
                    if (game_in_bounds(g, cx, cy) &&
                        game_in_bounds(g, dx2, dy2) &&
                        game_get_cell(g, cx, cy) == C &&
                        game_get_cell(g, dx2, dy2) == C) {
                        return 1;
                    }

                    // поворот вправо
                    cx = ax + dy; cy = ay - dx;
                    dx2 = bx + dy; dy2 = by - dx;
                    if (game_in_bounds(g, cx, cy) &&
                        game_in_bounds(g, dx2, dy2) &&
                        game_get_cell(g, cx, cy) == C &&
                        game_get_cell(g, dx2, dy2) == C) {
                        return 1;
                    }
                }
            }
        }
    }
    return 0;
}


/* --- Логика компьютера (как раньше) --- */
static int would_win(Game *g,int x,int y,char C) {
    if (!game_in_bounds(g,x,y)) return 0;
    if (game_get_cell(g,x,y)!='.') return 0;
    game_set_cell(g,x,y,C); g->moves_count++;
    int ok=check_square_for_color(g,C);
    game_set_cell(g,x,y,'.'); g->moves_count--;
    return ok;
}
static int find_winning(Game *g,char C,int *rx,int *ry){
    for(int y=0;y<g->N;y++)for(int x=0;x<g->N;x++)
        if(game_get_cell(g,x,y)=='.'&&would_win(g,x,y,C)){*rx=x;*ry=y;return 1;}
    return 0;
}
void game_comp_move(Game *g,int *outx,int *outy){
    char C=game_cur_player(g)->color;
    char O=(C=='W'?'B':'W');
    int x,y;
    if(find_winning(g,C,&x,&y)){game_set_cell(g,x,y,C);g->moves_count++;*outx=x;*outy=y;return;}
    if(find_winning(g,O,&x,&y)){game_set_cell(g,x,y,C);g->moves_count++;*outx=x;*outy=y;return;}
    int freecount=0;
    for(int yy=0;yy<g->N;yy++)for(int xx=0;xx<g->N;xx++)if(game_get_cell(g,xx,yy)=='.')freecount++;
    int r=rand()%freecount,idx=0;
    for(int yy=0;yy<g->N;yy++)for(int xx=0;xx<g->N;xx++)if(game_get_cell(g,xx,yy)=='.'){if(idx==r){game_set_cell(g,xx,yy,C);g->moves_count++;*outx=xx;*outy=yy;return;}idx++;}
    *outx=*outy=-1;
}
