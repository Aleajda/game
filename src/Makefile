CC = gcc
CFLAGS = -Wall -Wextra
OBJ = main.o io.o game.o parse.o print.o
TARGET = game

all: $(TARGET)

$(TARGET): $(OBJ)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJ)

main.o: main.c game.h io.h
io.o: io.c io.h game.h parse.h print.h
game.o: game.c game.h
parse.o: parse.c parse.h game.h
print.o: print.c print.h

clean:
	rm -f $(OBJ) $(TARGET)
