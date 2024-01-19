package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[9][9];


    public ChessBoard () {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece (ChessPosition position, ChessPiece piece) {
        board[position.getRow ()][position.getColumn ()] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece (ChessPosition position) {
        return board[position.getRow ()][position.getColumn ()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard () {
        //QUEENS
        addPiece (new ChessPosition (8,4),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece (new ChessPosition (1,4),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        //KINGS
        addPiece (new ChessPosition (8,5),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece (new ChessPosition (1,5),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        //BISHOPS
        addPiece (new ChessPosition (8,3),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece (new ChessPosition (8,6),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece (new ChessPosition (1,3),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece (new ChessPosition (1,6),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //KNIGHTS
        addPiece (new ChessPosition (8,2),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece (new ChessPosition (8,7),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece (new ChessPosition (1,2),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece (new ChessPosition (1,7),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        //ROOKS
        addPiece (new ChessPosition (8,1),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece (new ChessPosition (8,8),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece (new ChessPosition (1,1),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece (new ChessPosition (1,8),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //PAWNS
        addPiece (new ChessPosition (7,1),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (7,2),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (7,3),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (7,4),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (7,5),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (7,6),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (7,7),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (7,8),new ChessPiece (ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,1),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,2),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,3),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,4),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,5),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,6),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,7),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece (new ChessPosition (2,8),new ChessPiece (ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
    printBoard ();

    }



    public void printBoard() {
        for (int row = 8; row >= 1; row--) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null) {
                    char pieceChar = piece.getPieceType().name().charAt(0);
                    char displayChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? Character.toLowerCase(pieceChar) : pieceChar;
                    System.out.print("|" + displayChar);
                } else {
                    System.out.print("| ");
                }
            }
            System.out.println("|");
        }
    }


    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals (board, that.board);
    }

    @Override
    public int hashCode () {
        return Arrays.hashCode (board);
    }

    @Override
    public String toString () {
        return "ChessBoard{" +
                "board=" + Arrays.toString (board) +
                '}';
    }
}

