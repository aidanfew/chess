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
            files = new String[]{" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};
        } else {
            files = new String[]{" ", "h", "g", "f", "e", "d", "c", "b", "a", " "};
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
        out.print(EMPTY);
        printHeaderText(out, headerText);
//        out.print(EMPTY.repeat(1));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);

        out.print(RESET_TEXT_COLOR);
    }

    private void drawChessBoard(PrintStream out) {
        for (int boardRow = 8; boardRow > BOARD_SIZE_IN_SQUARES - 10; --boardRow) {

            drawRowsOfSquares(out, boardRow);
        }
    }

    private void drawRowsOfSquares(PrintStream out, int row) {
        String[] ranks = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(ranks[row-1]);
            out.print(RESET_TEXT_COLOR);
            out.print(RESET_BG_COLOR);
            for (int squareCol = 8; squareCol > BOARD_SIZE_IN_SQUARES - 10; --squareCol) {
                    ChessPosition position = new ChessPosition(row, squareCol);
                    ChessPiece piece = board.getPiece(position);
                    if (piece != null) {
                        ChessPiece.PieceType type = board.getPiece(position).getPieceType();
                        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
                        printPlayer(out, color, type);
                } else {
                        printPlayer(out, null, null);
                    }
                out.print(RESET_BG_COLOR);
            }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_WHITE);
            out.println(ranks[row-1]);
            out.print(RESET_TEXT_COLOR);
            out.print(RESET_BG_COLOR);
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
        out.print(SET_TEXT_COLOR_BLACK);

        if (Objects.equals(color, ChessGame.TeamColor.WHITE)) {
            out.print(SET_TEXT_COLOR_WHITE);
            switch (piece) {
                case null -> out.print(EMPTY);
                case PAWN -> out.print(WHITE_PAWN);
                case ROOK -> out.print(WHITE_ROOK);
                case KNIGHT -> out.print(WHITE_KNIGHT);
                case BISHOP -> out.print(WHITE_BISHOP);
                case KING -> out.print(WHITE_KING);
                case QUEEN -> out.print(WHITE_QUEEN);
            }
            out.print(RESET_TEXT_COLOR);
        } else {
            out.print();
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
        }
        out.print(RESET_BG_COLOR);
    }




}
