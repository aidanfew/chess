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

    public static boolean WithinBounds(ChessPosition position){
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

    public static Collection<ChessMove> BishopCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()-1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()+1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        }
        position = new ChessPosition(start.getRow()+1, start.getColumn()-1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        }
        return list;
    }

    public static Collection<ChessMove> QueenCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()-1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        }
        position = new ChessPosition(start.getRow()+1, start.getColumn()-1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()+1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        }
        position = new ChessPosition(start.getRow(), start.getColumn()+1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow(), position.getColumn()+1);
        }
        position = new ChessPosition(start.getRow()+1, start.getColumn());
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()+1, position.getColumn());
        }
        position = new ChessPosition(start.getRow(), start.getColumn()-1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow(), position.getColumn()-1);
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn());
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()-1, position.getColumn());
        }

        return list;
    }

    public static Collection<ChessMove> RookCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn());
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()+1, position.getColumn());
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn());
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow()-1, position.getColumn());
        }
        position = new ChessPosition(start.getRow(), start.getColumn()+1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow(), position.getColumn()+1);
        }
        position = new ChessPosition(start.getRow(), start.getColumn()-1);
        while (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != color) break;
            position = new ChessPosition(position.getRow(), position.getColumn()-1);
        }
        return list;
    }

    public static Collection<ChessMove> KingCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()-1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()+1, start.getColumn()-1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()+1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow(), start.getColumn()+1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()+1, start.getColumn());
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow(), start.getColumn()-1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn());
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }

        return list;
    }

    public static Collection<ChessMove> KnightCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        position = new ChessPosition(position.getRow()+2, position.getColumn()+1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()-2, start.getColumn()-1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()+2, start.getColumn()-1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()-2, start.getColumn()+1);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()+1, start.getColumn()+2);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()+1, start.getColumn()-2);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()+2);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }
        position = new ChessPosition(start.getRow()-1, start.getColumn()-2);
        if (WithinBounds(position) && (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color)) {
            list.add(new ChessMove(start, position, null));
        }

        return list;
    }

    public static Collection<ChessMove> PawnCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<>();
        ChessPosition start = position;
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        if (color == ChessGame.TeamColor.WHITE) {
            ChessPosition up_one = new ChessPosition(position.getRow() + 1, position.getColumn());
            ChessPosition up_two = new ChessPosition(position.getRow() + 2, position.getColumn());
            ChessPosition up_right = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            ChessPosition up_left = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            if (WithinBounds(up_one) && board.getPiece(up_one) == null) {
                if (up_one.getRow() == 8) {
                    list.add(new ChessMove(start, up_one, PieceType.KNIGHT));
                    list.add(new ChessMove(start, up_one, PieceType.QUEEN));
                    list.add(new ChessMove(start, up_one, PieceType.ROOK));
                    list.add(new ChessMove(start, up_one, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, up_one, null));
                }
                if (WithinBounds(up_two) && board.getPiece(up_two) == null && start.getRow() == 2) {
                    list.add(new ChessMove(start, up_two, null));
                }
            }
            if (WithinBounds(up_right) && board.getPiece(up_right) != null && board.getPiece(up_right).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (up_right.getRow() == 8) {
                    list.add(new ChessMove(start, up_right, PieceType.KNIGHT));
                    list.add(new ChessMove(start, up_right, PieceType.QUEEN));
                    list.add(new ChessMove(start, up_right, PieceType.ROOK));
                    list.add(new ChessMove(start, up_right, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, up_right, null));
                }
            }
            if (WithinBounds(up_left) && board.getPiece(up_left) != null && board.getPiece(up_left).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (up_left.getRow() == 8) {
                    list.add(new ChessMove(start, up_left, PieceType.KNIGHT));
                    list.add(new ChessMove(start, up_left, PieceType.QUEEN));
                    list.add(new ChessMove(start, up_left, PieceType.ROOK));
                    list.add(new ChessMove(start, up_left, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, up_left, null));
                }
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            ChessPosition down_one = new ChessPosition(position.getRow() - 1, position.getColumn());
            ChessPosition down_two = new ChessPosition(position.getRow() - 2, position.getColumn());
            ChessPosition down_right = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            ChessPosition down_left = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            if (WithinBounds(down_one) && board.getPiece(down_one) == null) {
                if (down_one.getRow() == 1) {
                    list.add(new ChessMove(start, down_one, PieceType.KNIGHT));
                    list.add(new ChessMove(start, down_one, PieceType.QUEEN));
                    list.add(new ChessMove(start, down_one, PieceType.ROOK));
                    list.add(new ChessMove(start, down_one, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, down_one, null));
                }
                if (WithinBounds(down_two) && board.getPiece(down_two) == null && start.getRow() == 7) {
                    list.add(new ChessMove(start, down_two, null));
                }
            }
            if (WithinBounds(down_right) && board.getPiece(down_right) != null && board.getPiece(down_right).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (down_right.getRow() == 1) {
                    list.add(new ChessMove(start, down_right, PieceType.KNIGHT));
                    list.add(new ChessMove(start, down_right, PieceType.QUEEN));
                    list.add(new ChessMove(start, down_right, PieceType.ROOK));
                    list.add(new ChessMove(start, down_right, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, down_right, null));
                }
            }
            if (WithinBounds(down_left) && board.getPiece(down_left) != null && board.getPiece(down_left).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (down_left.getRow() == 1) {
                    list.add(new ChessMove(start, down_left, PieceType.KNIGHT));
                    list.add(new ChessMove(start, down_left, PieceType.QUEEN));
                    list.add(new ChessMove(start, down_left, PieceType.ROOK));
                    list.add(new ChessMove(start, down_left, PieceType.BISHOP));
                } else {
                    list.add(new ChessMove(start, down_left, null));
                }
            }
        }
        return list;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP) {
            return BishopCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.QUEEN){
            return QueenCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.ROOK){
            return RookCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.KING) {
            return KingCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.KNIGHT) {
            return KnightCalc(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.PAWN) {
            return PawnCalc(board, myPosition);
        }
        return null;
    }
}
