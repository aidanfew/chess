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
    private ChessBoard board;
    private String color;

    public ManifestBoard(ChessBoard board, String color) {
        this.board = board;
        this.color = color;
    }

    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, ChessGame.TeamColor.valueOf(color));

        drawChessBoard(out);

        drawHeaders(out, ChessGame.TeamColor.valueOf(color));

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor playerColor) {
        String[] files;
        if (Objects.equals(playerColor, ChessGame.TeamColor.WHITE)) {
            files = new String[]{"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
        } else {
            files = new String[]{"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};
        }

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            drawHeader(out, files[boardCol]);
        }
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        printHeaderText(out, headerText);
//        out.print(EMPTY.repeat(1));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);

        out.print(RESET_TEXT_COLOR);
    }

    private void drawChessBoard(PrintStream out) {
        if (Objects.equals(ChessGame.TeamColor.WHITE, ChessGame.TeamColor.valueOf(color))) {
            for (int boardRow = 8; boardRow > BOARD_SIZE_IN_SQUARES - 10; --boardRow) {
                drawWhiteRow(out, boardRow);
            }
        } else {
            for (int boardRow = 1; boardRow < BOARD_SIZE_IN_SQUARES - 1; ++boardRow) {
                drawBlackRow(out, boardRow);
            }
        }
    }

    private void setUpRowColors(PrintStream out, int row) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" " + row + " ");
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
    }

    private void drawWhiteRow(PrintStream out, int row) {
        setUpRowColors(out, row);
        for (int squareCol = 1; squareCol < BOARD_SIZE_IN_SQUARES - 1; ++squareCol) {
            drawRowsOfSquares(out, row, squareCol);
        }
        setUpRowColors(out, row);
        out.println();
    }

    private void drawBlackRow(PrintStream out, int row) {
        setUpRowColors(out, row);
        for (int squareCol = 8; squareCol > BOARD_SIZE_IN_SQUARES - 10; --squareCol) {
            drawRowsOfSquares(out, row, squareCol);
        }
        setUpRowColors(out, row);
        out.println();
    }

    private void drawRowsOfSquares(PrintStream out, int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);
        if ((position.getColumn() + position.getRow()) % 2 == 0) {
            out.print(SET_BG_COLOR_DARK_GREY);
        } else {
            out.print(SET_BG_COLOR_BLUE);
        }
        if (piece != null) {
            ChessPiece.PieceType type = board.getPiece(position).getPieceType();
            ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
            printPlayer(out, color, type);
        } else {
            printPlayer(out, null, null);
        }
            out.print(RESET_BG_COLOR);
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
        if (Objects.equals(color, ChessGame.TeamColor.WHITE)) {
            out.print(SET_TEXT_COLOR_WHITE);
        } else {
            out.print(SET_TEXT_COLOR_BLACK);
        }
        switch (piece) {
            case null -> out.print(EMPTY);
            case PAWN -> out.print(BLACK_PAWN);
            case ROOK -> out.print(BLACK_ROOK);
            case KNIGHT -> out.print(BLACK_KNIGHT);
            case BISHOP -> out.print(BLACK_BISHOP);
            case KING -> out.print(BLACK_KING);
            case QUEEN -> out.print(BLACK_QUEEN);
        }
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
    }




}
