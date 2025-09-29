#ifndef PARSE_H
#define PARSE_H

#include "game.h"

int parse_player(const char *s, Player *p);
int parse_game_command(char *args, Game *g);

#endif
