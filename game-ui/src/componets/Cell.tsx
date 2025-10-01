import React from "react";

interface CellProps {
    value: string;
    row: number;
    col: number;
    onClick: (row: number, col: number) => void;
}

const Cell: React.FC<CellProps> = ({ value, row, col, onClick }) => {
    return (
        <div
            onClick={() => onClick(row, col)}
            className={`rounded-2xl cursor-pointer hover:scale-110 transition-transform ${
                value === "w"
                    ? "bg-white text-black"
                    : value === "b"
                        ? "bg-black text-white"
                        : "bg-gray-200"
            }`}
            style={{
                width: "40px",
                height: "40px",
            }}
        />
    );
};

export default Cell;
