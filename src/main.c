#include "game.h"
#include "io.h"
#include <stdlib.h>
#include <time.h>

int main(void) {
    srand((unsigned)time(NULL));

    Game game;
    game_init(&game);

    io_run(&game);

    game_free(&game);
    return 0;
}
