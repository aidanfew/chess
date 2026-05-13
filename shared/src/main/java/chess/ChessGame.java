package chess;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard official_board = new ChessBoard();
    int count = 0;
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (count % 2 == 0) {
            return TeamColor.WHITE;
        } else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (getTeamTurn() == TeamColor.WHITE) {
            team = TeamColor.WHITE;
        } else {
            team = TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public boolean simpleCheck(ChessMove move, ChessPosition KingPosition) {
        return move.getEndPosition() == KingPosition;
    }

    public ChessPosition findTheKing(TeamColor color) {
        ChessBoard board = getBoard();
        for (int i=1; i<=8; i++){
            int j = 0;
            ChessPosition position = new ChessPosition(i, j);
            ChessPiece piece = board.getPiece(position);
            j++;
            if (piece.getPieceType() == ChessPiece.PieceType.KING && color == piece.getTeamColor()) {
                return new ChessPosition(i, j);
            }
        }
        return null;
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor piece_color = piece.getTeamColor();
        ArrayList<ChessMove> list = new ArrayList<>();

        if (piece == null) {
            return null;
        } else {
            ArrayList<ChessMove> complete_list = (ArrayList<ChessMove>) piece.pieceMoves(board, startPosition);
            ChessPosition KingPosition = findTheKing(piece_color);
            for (ChessMove move : complete_list) {
                if (!simpleCheck(move, KingPosition)) {
                    list.add(new ChessMove(startPosition, move.getEndPosition(), null));
                }
            }
        }
        return list;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        count ++;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    public boolean isInCheck(TeamColor teamColor) {

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return official_board;
    }
}
