#ifndef GAME_H
#define GAME_H

typedef enum { PLAYER_USER, PLAYER_COMP } PlayerType;

typedef struct {
  PlayerType type;
  char color;
} Player;

typedef struct {
  int N;
  char *cells;
  Player p1, p2;
  int cur;
  int started;
  int moves_count;
} Game;

void game_init(Game *g);
void game_free(Game *g);
int game_start(Game *g, int N, Player p1, Player p2);
int game_in_bounds(Game *g, int x, int y);
char game_get_cell(Game *g, int x, int y);
void game_set_cell(Game *g, int x, int y, char c);
Player *game_cur_player(Game *g);
Player *game_other_player(Game *g);
void game_switch_turn(Game *g);
int game_board_full(Game *g);
int game_check_winner(Game *g, char *out_color);

void game_comp_move(Game *g, int *outx, int *outy);

#endif
