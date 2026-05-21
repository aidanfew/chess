package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard officialBoard = new ChessBoard();
    TeamColor teamColor = TeamColor.WHITE;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(officialBoard, chessGame.officialBoard) && teamColor == chessGame.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(officialBoard, teamColor);
    }

    public ChessGame() {
        officialBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
//        if (count % 2 == 0) {
//            return TeamColor.WHITE;
//        } else {
//            return TeamColor.BLACK;
//        }
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamColor = team;
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

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (piece == null) {
            return null;
        } else {
            ArrayList<ChessMove> completeList = (ArrayList<ChessMove>) piece.pieceMoves(board, startPosition);
            for (ChessMove move : completeList) {
                ChessBoard copiedBoard = copyBoard(officialBoard);
                officialBoard.addPiece(move.getEndPosition(), officialBoard.getPiece(move.getStartPosition()));
                officialBoard.removePiece(move.getStartPosition());
                if (!isInCheck(piece.getTeamColor())) {
                    validMoves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece()));
                }
                officialBoard = copiedBoard;
            }
            return validMoves;
        }
    }


    public ChessPosition findTheKing(TeamColor color) {
        ChessBoard board = getBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && color == piece.getTeamColor()) {
                    return position;
                }
            }
        }
        return null;
    }

    public ChessBoard copyBoard(ChessBoard board) {
        ChessBoard chessBoard = new ChessBoard();
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                chessBoard.addPiece(position, piece);
            }
        }
        return chessBoard;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null || piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException();
        }
        ArrayList<ChessMove> validatedMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        if (validatedMoves.contains(move)) {
            if (move.getPromotionPiece() == null) {
                officialBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                officialBoard.removePiece(move.getStartPosition());
            } else {
                officialBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
                officialBoard.removePiece(move.getStartPosition());
            }
            if (teamColor.equals(TeamColor.BLACK)) {
                setTeamTurn(TeamColor.WHITE);
            } else {
                setTeamTurn(TeamColor.BLACK);
            }
            } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard board = getBoard();
        ChessPosition kingPosition = findTheKing(teamColor);
            for (int i=1; i<=8; i++) {
                for (int j=1; j<=8; j++) {
                    ChessPosition checker = new ChessPosition(i, j);
                    ChessPiece piece = board.getPiece(checker);
                    if (piece != null && piece.getTeamColor() != teamColor) {
                        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) piece.pieceMoves(board, checker);
                        for (ChessMove move : moves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    public boolean noValidMoves(TeamColor teamColor) {
        ChessBoard board = getBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition checker = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(checker);
                if (piece != null && piece.getTeamColor().equals(teamColor)) {
                    ArrayList<ChessMove> list = (ArrayList<ChessMove>) validMoves(checker);
                    if (list != null && !list.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        officialBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return officialBoard;
    }
}
