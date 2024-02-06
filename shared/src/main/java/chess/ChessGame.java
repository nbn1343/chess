package chess;

import java.security.KeyStore;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamColor;

    public ChessGame () {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn () {
        if (teamColor == TeamColor.WHITE) {
            return TeamColor.WHITE;
        } else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */

    public void setTeamTurn (TeamColor team) {
        this.teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves (ChessPosition startPosition) {
        ChessPiece currentPiece = board.getPiece (startPosition);
        HashSet<ChessMove> moves = new HashSet<> (currentPiece.pieceMoves (board, startPosition));
        if (currentPiece == null) {
            return null;
        } else {
            return moves;
        }

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    ChessMove move;

    public void makeMove (ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition ();
        ChessPosition endPosition = move.getEndPosition ();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece ();

        if (!getTeamTurn().equals(board.getPiece(startPosition).getTeamColor())) {
            throw new InvalidMoveException();
        }

        if (!validMoves(startPosition).contains(move)) {
            throw new InvalidMoveException();
        }

        if (isInCheck (getTeamTurn ())) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition currentPosition = new ChessPosition (row, col);
                    ChessPiece currentPiece = board.getPiece (currentPosition);
                    if (currentPiece != null) {
                        for (ChessMove possibleMoves : currentPiece.pieceMoves (board, currentPosition)) {
                            if (move.getStartPosition().equals(possibleMoves.getStartPosition()) &&
                                    move.getEndPosition().equals(possibleMoves.getEndPosition())) {
                                ChessPosition start = possibleMoves.getStartPosition ();
                                ChessPosition end = possibleMoves.getEndPosition ();
                                ChessPiece capturedPiece = board.getPiece (end);

                                board.addPiece (end, board.getPiece (start));
                                board.addPiece (start, null);
                                if (!isInCheck (getTeamTurn ())) {
                                    setTeamTurn (getTeamTurn () == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
                                    return;
                                }
                                board.addPiece (start, board.getPiece (end));
                                board.addPiece (end, capturedPiece);

                            }

                        }
                    }
                }
            }
            throw new InvalidMoveException ();


        }
        else if (promotionPiece != null) {
            board.addPiece (endPosition,new ChessPiece (teamColor,promotionPiece));
            board.addPiece(startPosition, null);
            setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        }
        else {
            board.addPiece(endPosition, board.getPiece(startPosition));
            board.addPiece(startPosition, null);
            setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck (TeamColor teamColor) {
        ChessPosition kingPosition = kingPosition(teamColor);

        for (int row = 0; row <= 8; row++) {
            for (int col = 0; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = currentPiece.pieceMoves(board, currentPosition);
                    if (moves.stream().anyMatch(move -> move.getEndPosition().equals(kingPosition))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private ChessPosition kingPosition(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);


                if (currentPiece != null && currentPiece.getTeamColor() == teamColor &&
                        currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return currentPosition;
                }
            }
        }

        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = kingPosition (teamColor);
        ChessPiece kingPiece = board.getPiece (kingPosition);
        Collection<ChessMove> kingMoves = kingPiece.pieceMoves(board, kingPosition);
        HashSet<ChessMove> inCheckMoves = new HashSet<> ();
        if(isInCheck (getTeamTurn ())) {
            for (ChessMove moves : kingMoves) {
                ChessPosition start = moves.getStartPosition ();
                ChessPosition end = moves.getEndPosition ();

                board.addPiece (end, board.getPiece (start));
                board.addPiece (start, null);
                ChessPiece capturedPiece = board.getPiece (end);
                if (isInCheck (getTeamTurn ())) {
                    inCheckMoves.add (moves);
                }
                board.addPiece (start, board.getPiece (end));
                board.addPiece (end, capturedPiece);
            }
        }
        setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
      return inCheckMoves.size () == kingMoves.size ();

    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate (TeamColor teamColor) {
        ChessPosition kingPosition = kingPosition (teamColor);
        ChessPiece kingPiece = board.getPiece (kingPosition);
        Collection<ChessMove> kingMoves = kingPiece.pieceMoves(board, kingPosition);
        HashSet<ChessMove> inCheckMoves = new HashSet<> ();
        for (ChessMove moves : kingMoves) {
            ChessPosition start = moves.getStartPosition ();
            ChessPosition end = moves.getEndPosition ();

            board.addPiece (end, board.getPiece (start));
            board.addPiece (start, null);
            ChessPiece capturedPiece = board.getPiece (end);
            if (isInCheck (getTeamTurn ())) {
                inCheckMoves.add (moves);
            }
            board.addPiece (start, board.getPiece (end));
            board.addPiece (end, capturedPiece);

        }
        setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        return inCheckMoves.size () == kingMoves.size ();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */

    public void setBoard (ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard () {
        printBoard ();
        return board;
    }

    public void printBoard () {
        board.printBoard ();
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals (board, chessGame.board) && teamColor == chessGame.teamColor && Objects.equals (move, chessGame.move);
    }

    @Override
    public int hashCode () {
        return Objects.hash (board, teamColor, move);
    }

    @Override
    public String toString () {
        return "ChessGame{" +
                "board=" + board +
                ", teamColor=" + teamColor +
                ", move=" + move +
                '}';
    }
}
