package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ManifestBoard {
    // Board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private static ChessBoard board;

    public ManifestBoard(ChessBoard board) {
        this.board = board;
    }

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, ChessGame.TeamColor.valueOf(args[1]));

        drawChessBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor playerColor) {
        setBlack(out);
        String[] files;
        String[] ranks;
        if (Objects.equals(playerColor, ChessGame.TeamColor.WHITE)) {
            files = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
            ranks = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        } else {
            files = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
            ranks = new String[]{"8", "7", "6", "5", "4", "3", "2", "1"};
        }

        for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, files[boardCol]);
            out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void drawColumn(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setBlack(out);
    }

    private static void printColumnText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowsOfSquares(out);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }

    private static void drawRowsOfSquares(PrintStream out) {
        for (int squareCol = 0; squareCol < BOARD_SIZE_IN_SQUARES; ++squareCol) {
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    setWhite(out);

                    if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                        out.print(EMPTY.repeat(prefixLength));
                        ChessPosition position = new ChessPosition(squareRow, squareCol);
                        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
                        ChessPiece.PieceType type = board.getPiece(position).getPieceType();
                        printPlayer(out, color, type);
                    }
                }
            }
        }
    }

    private static void drawHorizontalLine(PrintStream out) {
        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES - 9) * LINE_WIDTH_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            setWhite(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, ChessGame.TeamColor color, ChessPiece.PieceType piece) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        if (Objects.equals(color, ChessGame.TeamColor.WHITE)) {
            switch (piece) {
                case PAWN -> out.print(WHITE_PAWN);
                case ROOK -> out.print(WHITE_ROOK);
                case KNIGHT -> out.print(WHITE_KNIGHT);
                case BISHOP -> out.print(WHITE_BISHOP);
                case KING -> out.print(WHITE_KING);
                case QUEEN -> out.print(WHITE_QUEEN);
            }
        } else {
            switch (piece) {
                case PAWN -> out.print(BLACK_PAWN);
                case ROOK -> out.print(BLACK_ROOK);
                case KNIGHT -> out.print(BLACK_KNIGHT);
                case BISHOP -> out.print(BLACK_BISHOP);
                case KING -> out.print(BLACK_KING);
                case QUEEN -> out.print(BLACK_QUEEN);
            }
        }
        setWhite(out);
    }




}
