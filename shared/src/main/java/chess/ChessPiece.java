package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public boolean withinBounds(ChessPosition position){
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

    public void continuousMovement(List<ChessMove> list, ChessPosition position, Integer row, Integer col, ChessPosition start, ChessBoard board, ChessGame.TeamColor color) {
        while (withinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow() + row, position.getColumn() + col);
        }
    }

    public Collection<ChessMove> bishopCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        continuousMovement(list, position, 1, 1, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()-1);
        continuousMovement(list, position, -1, -1, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()+1);
        continuousMovement(list, position, -1, +1, start, board, color);

        position = new ChessPosition(start.getRow()+1, start.getColumn()-1);
        continuousMovement(list, position, +1, -1, start, board, color);
        return list;
    }

    public Collection<ChessMove> queenCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        continuousMovement(list, position, 1, 1, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()-1);
        continuousMovement(list, position, -1, -1, start, board, color);

        position = new ChessPosition(start.getRow()+1, start.getColumn()-1);
        continuousMovement(list, position, 1, -1, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()+1);
        continuousMovement(list, position, -1, 1, start, board, color);

        position = new ChessPosition(start.getRow(), start.getColumn()+1);
        continuousMovement(list, position, 0, 1, start, board, color);

        position = new ChessPosition(start.getRow()+1, start.getColumn());
        continuousMovement(list, position, 1, 0, start, board, color);

        position = new ChessPosition(start.getRow(), start.getColumn()-1);
        continuousMovement(list, position, 0, -1, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn());
        continuousMovement(list, position, -1, 0, start, board, color);

        return list;
    }

    public Collection<ChessMove> rookCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn());
        continuousMovement(list, position, 1, 0, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn());
        continuousMovement(list, position, -1, 0, start, board, color);

        position = new ChessPosition(start.getRow(), start.getColumn()+1);
        continuousMovement(list, position, 0, 1, start, board, color);

        position = new ChessPosition(start.getRow(), start.getColumn()-1);
        continuousMovement(list, position, 0, -1, start, board, color);
        return list;
    }

    public void singleMovement(List<ChessMove> list, ChessPosition position, ChessPosition start, ChessBoard board, ChessGame.TeamColor color) {
        if (withinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
    }

    public Collection<ChessMove> kingCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()-1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()+1, start.getColumn()-1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()+1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow(), start.getColumn()+1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()+1, start.getColumn());
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow(), start.getColumn()-1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn());
        singleMovement(list, position, start, board, color);

        return list;
    }

    public Collection<ChessMove> knightCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+2, position.getColumn()+1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()-2, start.getColumn()-1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()+2, start.getColumn()-1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()-2, start.getColumn()+1);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()+1, start.getColumn()+2);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()+1, start.getColumn()-2);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()+2);
        singleMovement(list, position, start, board, color);

        position = new ChessPosition(start.getRow()-1, start.getColumn()-2);
        singleMovement(list, position, start, board, color);

        return list;
    }

    public Collection<ChessMove> pawnCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        if (color == ChessGame.TeamColor.WHITE) {
            ChessPosition upOne = new ChessPosition(position.getRow() + 1, position.getColumn());
            ChessPosition upTwo = new ChessPosition(position.getRow() + 2, position.getColumn());
            ChessPosition upRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            ChessPosition upLeft = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            if (withinBounds(upOne) && board.getPiece(upOne) == null) {
                if (upOne.getRow() == 8) {
                    list.add(new ChessMove(start, upOne, PieceType.KNIGHT));
                    list.add(new ChessMove(start, upOne, PieceType.QUEEN));
                    list.add(new ChessMove(start, upOne, PieceType.ROOK));
                    list.add(new ChessMove(start, upOne, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, upOne, null));
                }
                if (withinBounds(upTwo) && board.getPiece(upTwo) == null && start.getRow() == 2) {
                    list.add(new ChessMove(start, upTwo, null));
                }
            }
            if (withinBounds(upRight) && board.getPiece(upRight) != null && board.getPiece(upRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (upRight.getRow() == 8) {
                    list.add(new ChessMove(start, upRight, PieceType.KNIGHT));
                    list.add(new ChessMove(start, upRight, PieceType.QUEEN));
                    list.add(new ChessMove(start, upRight, PieceType.ROOK));
                    list.add(new ChessMove(start, upRight, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, upRight, null));
                }
            }
            if (withinBounds(upLeft) && board.getPiece(upLeft) != null && board.getPiece(upLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (upLeft.getRow() == 8) {
                    list.add(new ChessMove(start, upLeft, PieceType.KNIGHT));
                    list.add(new ChessMove(start, upLeft, PieceType.QUEEN));
                    list.add(new ChessMove(start, upLeft, PieceType.ROOK));
                    list.add(new ChessMove(start, upLeft, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, upLeft, null));
                }
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            ChessPosition downOne = new ChessPosition(position.getRow() - 1, position.getColumn());
            ChessPosition downTwo = new ChessPosition(position.getRow() - 2, position.getColumn());
            ChessPosition downRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            ChessPosition downLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            if (withinBounds(downOne) && board.getPiece(downOne) == null) {
                if (downOne.getRow() == 1) {
                    list.add(new ChessMove(start, downOne, PieceType.KNIGHT));
                    list.add(new ChessMove(start, downOne, PieceType.QUEEN));
                    list.add(new ChessMove(start, downOne, PieceType.ROOK));
                    list.add(new ChessMove(start, downOne, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, downOne, null));
                }
                if (withinBounds(downTwo) && board.getPiece(downTwo) == null && start.getRow() == 7) {
                    list.add(new ChessMove(start, downTwo, null));
                }
            }
            if (withinBounds(downRight) && board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (downRight.getRow() == 1) {
                    list.add(new ChessMove(start, downRight, PieceType.KNIGHT));
                    list.add(new ChessMove(start, downRight, PieceType.QUEEN));
                    list.add(new ChessMove(start, downRight, PieceType.ROOK));
                    list.add(new ChessMove(start, downRight, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, downRight, null));
                }
            }
            if (withinBounds(downLeft) && board.getPiece(downLeft) != null && board.getPiece(downLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (downLeft.getRow() == 1) {
                    list.add(new ChessMove(start, downLeft, PieceType.KNIGHT));
                    list.add(new ChessMove(start, downLeft, PieceType.QUEEN));
                    list.add(new ChessMove(start, downLeft, PieceType.ROOK));
                    list.add(new ChessMove(start, downLeft, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, downLeft, null));
                }
            }
        }
        return list;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP) {
            return bishopCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.QUEEN){
            return queenCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.ROOK){
            return rookCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.KING) {
            return kingCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.KNIGHT) {
            return knightCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.PAWN) {
            return pawnCalc(board, myPosition);
        }
        return null;
    }
}
