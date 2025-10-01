import React, { useState } from "react";
import { usersAPI } from "../api/Api.ts";
import Cell from "./Cell.tsx";

interface Response {
    result: string;
    color: string;
    x: string;
    y: string;
}

const Game: React.FC = () => {
    const [boardSize, setBoardSize] = useState<number | null>(null);
    const [inputSize, setInputSize] = useState<string>("");
    const [board, setBoard] = useState<string>("");
    const [currentPlayer, setCurrentPlayer] = useState<"w" | "b">("w");
    const [startingPlayer, setStartingPlayer] = useState<"w" | "b">("w");
    const [winner, setWinner] = useState<string | null>(null);
    const [isDraw, setIsDraw] = useState<boolean>(false);

    const startGame = () => {
        const sizeNum = parseInt(inputSize);
        if (isNaN(sizeNum) || sizeNum < 3 || sizeNum > 50) {
            alert("Размер поля должен быть числом от 3 до 50");
            return;
        }
        setBoardSize(sizeNum);
        setBoard(" ".repeat(sizeNum * sizeNum));
        setWinner(null);
        setIsDraw(false);
        setCurrentPlayer(startingPlayer);
    };

    const checkDraw = (boardString: string) => {
        if (!boardString.includes(" ") && !winner) {
            setIsDraw(true);
            return true;
        }
        return false;
    };

    const handleClick = async (row: number, col: number) => {
        if (!boardSize || winner || isDraw) return;

        const index = row * boardSize + col;
        if (board[index] !== " ") return;

        let newBoard = board.split("");
        newBoard[index] = currentPlayer;
        let boardString = newBoard.join("");
        setBoard(boardString);

        if (checkDraw(boardString)) return;

        try {
            const response: Response = await usersAPI.nextMove(
                boardSize,
                boardString,
                currentPlayer === "w" ? "b" : "w"
            );

            if (response.result === "win") {
                setWinner(response.color);
                return;
            }

            if (response.result === "draw") {
                setIsDraw(true);
                return;
            }

            if (response.result === "move") {
                const botIndex = response.y * boardSize + response.x;

                if (boardString[botIndex] === " ") {
                    newBoard[botIndex] = response.color.toLowerCase();
                    const updatedBoard = newBoard.join("");
                    setBoard(updatedBoard);

                    if (checkDraw(updatedBoard)) return;

                    boardString = updatedBoard; // на всякий случай обновляем ссылку
                }
            }

            if (!winner && checkDraw(boardString)) return;

        } catch (err) {
            console.error("Ошибка при запросе:", err);
        }
    };


    if (!boardSize) {
        return (
            <div className="flex flex-col items-center p-4">
                <h2 className="text-xl font-semibold mb-2">Игра в квадраты</h2>

                <input
                    type="number"
                    className="border p-2 mb-2 focus:outline-none rounded"
                    value={inputSize}
                    onChange={(e) => setInputSize(e.target.value)}
                    placeholder="Введите размер (3-50)"
                />

                <div className="flex items-center gap-4 mb-4">
                    <label className="flex items-center gap-2">
                        <input
                            type="radio"
                            name="startingColor"
                            value="w"
                            checked={startingPlayer === "w"}
                            onChange={() => setStartingPlayer("w")}
                        />
                        Белые начинают
                    </label>
                    <label className="flex items-center gap-2">
                        <input
                            type="radio"
                            name="startingColor"
                            value="b"
                            checked={startingPlayer === "b"}
                            onChange={() => setStartingPlayer("b")}
                        />
                        Чёрные начинают
                    </label>
                </div>

                <button
                    onClick={startGame}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    Начать игру
                </button>
            </div>
        );
    }

    return (
        <div className="flex flex-col items-center p-4 min-h-screen w-full bg-blue-200">
            <h2 className="text-xl font-semibold mb-4">Игра в квадраты</h2>

            {winner && (
                <div className="text-2xl text-green-600 mb-4">
                    Победитель: {winner === "W" ? "Белые" : "Чёрные"}
                </div>
            )}

            {isDraw && (
                <div className="text-2xl text-gray-600 mb-4">
                    Ничья
                </div>
            )}

            <div
                className="grid"
                style={{
                    gridTemplateColumns: `repeat(${boardSize}, 40px)`,
                    gridTemplateRows: `repeat(${boardSize}, 40px)`,
                    gap: "4px",
                }}
            >
                {board.split("").map((cell, index) => {
                    const row = Math.floor(index / boardSize);
                    const col = index % boardSize;
                    return (
                        <Cell
                            key={index}
                            value={cell}
                            row={row}
                            col={col}
                            onClick={handleClick}
                        />
                    );
                })}
            </div>

            <button
                onClick={() => {
                    setBoardSize(null);
                    setInputSize("");
                    setBoard("");
                    setWinner(null);
                    setIsDraw(false);
                }}
                className="mt-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
            >
                Новая игра
            </button>
        </div>
    );

};

export default Game;
