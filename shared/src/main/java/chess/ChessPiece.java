package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
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
    public ChessGame.TeamColor getTeamColor () {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType () {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.BISHOP) {
            return this.bishopMove (board, myPosition);
        }
        return null;
    }

    public Collection<ChessMove> bishopMove (ChessBoard board, ChessPosition myPosition) {
        //A bishop moves diagonally across the board in either direction
        HashSet<ChessMove> bishopMoves = new HashSet<> ();
        int counter = 1;

        //Checking upper right diagonal
        while (isValidPosition(myPosition.getRow () + counter, myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () + counter),null);
            System.out.println ("====");
            bishopMoves.add(moves);
            System.out.println (moves);
            counter += 1;
        }
        counter = 1;
        //Checking lower right diagonal
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () + counter), null);
            System.out.println ("====");
            bishopMoves.add (moves);
            System.out.println (moves);
            counter += 1;
        }
        counter = 1;
        //Checking lower left diagonal
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () - counter), null);
            System.out.println ("====");
            bishopMoves.add (moves);
            System.out.println (moves);
            counter += 1;
        }
        counter = 1;
        //Checking upper left diagonal
        while (isValidPosition(myPosition.getRow () + counter, myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn () - counter), null);
            System.out.println ("====");
            bishopMoves.add (moves);
            System.out.println (moves);
            counter += 1;
        }




        return bishopMoves;


    }

    private boolean isValidPosition (int row, int col) {
        return row >= 1 && row < 9 && col >= 1 && col < 9;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode () {
        return Objects.hash (pieceColor, type);
    }
}
