#include "io.h"
#include "parse.h"
#include "print.h"
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static void trim(char *s) {
  char *p = s;
  while (isspace((unsigned char)*p))
    p++;
  if (p != s)
    memmove(s, p, strlen(p) + 1);
  size_t len = strlen(s);
  while (len > 0 && isspace((unsigned char)s[len - 1])) {
    s[--len] = '\0';
  }
}

void io_run(Game *game) {
  char line[512];
  int running = 1;

  print_help();

  while (running && fgets(line, sizeof(line), stdin)) {
    trim(line);
    if (!*line) {
      continue;
    }

    char cmd[64], args[448] = "";
    char *sp = strchr(line, ' ');
    if (sp) {
      *sp = '\0';
      strcpy(cmd, line);
      strcpy(args, sp + 1);
    } else {
      strcpy(cmd, line);
    }

    for (char *q = cmd; *q; q++)
      *q = toupper(*q);

    if (strcmp(cmd, "EXIT") == 0) {
      running = 0;
    } else if (strcmp(cmd, "HELP") == 0) {
      print_help();
    } else if (strcmp(cmd, "GAME") == 0) {
      if (!parse_game_command(args, game)) {
        puts("Incorrect command");
      } else {
        puts("New game started");

        if (game->p1.type == PLAYER_COMP) {
          int x, y;
          game_comp_move(game, &x, &y);
          printf("%c (%d, %d)\n", game->p1.color, x, y);

          char w;
          if (game_check_winner(game, &w)) {
            printf("Game finished. %c wins!\n", w);
            game->started = 0;
          } else if (game_board_full(game)) {
            puts("Game finished. Draw");
            game->started = 0;
          } else {
            game_switch_turn(game);
          }
        }

        if (game->p1.type == PLAYER_COMP && game->p2.type == PLAYER_COMP) {
          int autoplay = 1;
          while (autoplay && game->started) {
            Player *cur = game_cur_player(game);
            int x, y;
            game_comp_move(game, &x, &y);
            printf("%c (%d, %d)\n", cur->color, x, y);

            char w;
            if (game_check_winner(game, &w)) {
              printf("Game finished. %c wins!\n", w);
              game->started = 0;
              autoplay = 0;
            } else if (game_board_full(game)) {
              puts("Game finished. Draw");
              game->started = 0;
              autoplay = 0;
            } else {
              game_switch_turn(game);
            }
          }
        }
      }
    } else if (strcmp(cmd, "MOVE") == 0) {
      if (!game->started) {
        puts("Incorrect command");
      } else {
        Player *cur = game_cur_player(game);
        if (cur->type != PLAYER_USER) {
          puts("Incorrect command");
        } else {
          char *comma = strchr(args, ',');
          if (!comma) {
            puts("Incorrect command");
          } else {
            *comma = '\0';
            int x = atoi(args), y = atoi(comma + 1);

            if (!game_in_bounds(game, x, y) ||
                game_get_cell(game, x, y) != '.') {
              puts("Incorrect command");
            } else {
              game_set_cell(game, x, y, cur->color);
              game->moves_count++;

              char w;
              if (game_check_winner(game, &w)) {
                printf("Game finished. %c wins!\n", w);
                game->started = 0;
              } else if (game_board_full(game)) {
                puts("Game finished. Draw");
                game->started = 0;
              } else {
                game_switch_turn(game);

                int comp_turn = 1;
                while (comp_turn && game->started &&
                       game_cur_player(game)->type == PLAYER_COMP) {
                  Player *cpl = game_cur_player(game);
                  int mx, my;
                  game_comp_move(game, &mx, &my);
                  printf("%c (%d, %d)\n", cpl->color, mx, my);

                  if (game_check_winner(game, &w)) {
                    printf("Game finished. %c wins!\n", w);
                    game->started = 0;
                    comp_turn = 0;
                  } else if (game_board_full(game)) {
                    puts("Game finished. Draw");
                    game->started = 0;
                    comp_turn = 0;
                  } else {
                    game_switch_turn(game);
                  }
                }
              }
            }
          }
        }
      }
    } else {
      puts("Incorrect command");
    }
  }
}
