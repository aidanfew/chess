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
        List<ChessMove> list = new ArrayList<ChessMove>();
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
        List<ChessMove> list = new ArrayList<ChessMove>();
        ChessPosition start = position;
        position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        }
        position = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        }
        position = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        }
        position = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        }
        position = new ChessPosition(position.getRow(), position.getColumn()+1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow(), position.getColumn()+1);
        }
        position = new ChessPosition(position.getRow()+1, position.getColumn());
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()+1, position.getColumn());
        }
        position = new ChessPosition(position.getRow(), position.getColumn()-1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow(), position.getColumn()-1);
        }
        position = new ChessPosition(position.getRow()-1, position.getColumn());
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()-1, position.getColumn());
        }
        return list;
    }

    public static Collection<ChessMove> RookCalc(ChessBoard board, ChessPosition position) {
        List<ChessMove> list = new ArrayList<ChessMove>();
        ChessPosition start = position;
        position = new ChessPosition(position.getRow()+1, position.getColumn());
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()+1, position.getColumn());
        }
        position = new ChessPosition(position.getRow()-1, position.getColumn());
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow()-1, position.getColumn());
        }
        position = new ChessPosition(position.getRow(), position.getColumn()-1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow(), position.getColumn()-1);
        }
        position = new ChessPosition(position.getRow(), position.getColumn()+1);
        while (board.getPiece(position) == null) {
            if (!WithinBounds(position)) break;
            list.add(new ChessMove(start, position, null));
            position = new ChessPosition(position.getRow(), position.getColumn()+1);
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
        return List.of();
    }
}
