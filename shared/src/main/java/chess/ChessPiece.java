package chess;

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
        } else if (type == PieceType.KING) {
            return this.kingMove (board, myPosition);
        } else if (type == PieceType.KNIGHT) {
            return this.knightMove (board, myPosition);
        } else if (type == PieceType.PAWN) {
            return this.pawnMove (board, myPosition);
        } else if (type == PieceType.QUEEN) {
            return this.queenMove (board, myPosition);
        } else if (type == PieceType.ROOK) {
            return this.rookMove (board, myPosition);
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
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () + counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            if (nextPiece == null) {
                bishopMoves.add(moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                bishopMoves.add(moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking lower right diagonal
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () + counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn () + counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                bishopMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                bishopMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking lower left diagonal
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn () - counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                bishopMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                bishopMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking upper left diagonal
        while (isValidPosition(myPosition.getRow () + counter, myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () - counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                bishopMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                bishopMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }

        return bishopMoves;


    }
    private Collection<ChessMove> kingMove (ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> kingMoves = new HashSet<> ();
        int counter = 1;
        ChessPiece currentPiece = board.getPiece (myPosition);
        //Up
        if (isValidPosition (myPosition.getRow () + counter,myPosition.getColumn ())) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn ()), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn ()));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        //Down
        } if (isValidPosition (myPosition.getRow () - counter,myPosition.getColumn ())) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn ()),null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn ()));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        //Right
        } if (isValidPosition (myPosition.getRow (),myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow (), myPosition.getColumn () + counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow (),myPosition.getColumn () + counter));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        //Left
        } if (isValidPosition (myPosition.getRow (),myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow (),myPosition.getColumn () - counter),null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow (),myPosition.getColumn () - counter));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        //Up 1 Right 1
        } if (isValidPosition (myPosition.getRow () + counter,myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn () + counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () + counter));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        //Up 1 Left 1
        } if (isValidPosition (myPosition.getRow () + counter,myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () - counter));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        //Down 1 Right 1
        } if (isValidPosition (myPosition.getRow () - counter,myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () + counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn () + counter));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        //Down 1 Left 1
        } if (isValidPosition (myPosition.getRow () - counter,myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn () - counter));
            if (nextPiece == null) {
                kingMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                kingMoves.add(moves);
            }
        }

        return kingMoves;
    }
    private Collection<ChessMove> knightMove(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> knightMoves = new HashSet<> ();
        ChessPiece currentPiece = board.getPiece (myPosition);

        //Up 1 Right 2
        if (isValidPosition (myPosition.getRow () + 1, myPosition.getColumn () + 2 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 2), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 2));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        //Up 1 Left 2
        } if (isValidPosition (myPosition.getRow () + 1, myPosition.getColumn () - 2 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 2), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 2));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        //Up 2 Right 1
        } if (isValidPosition (myPosition.getRow () + 2, myPosition.getColumn () + 1 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 2, myPosition.getColumn () + 1), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 2, myPosition.getColumn () + 1));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        //Up 2 Left 1
        } if (isValidPosition (myPosition.getRow () + 2, myPosition.getColumn () - 1 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 2, myPosition.getColumn () - 1), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 2, myPosition.getColumn () - 1));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        //Down 1 Right 2
        } if (isValidPosition (myPosition.getRow () - 1, myPosition.getColumn () + 2 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 2), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 2));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        //Down 1 Left 2
        } if (isValidPosition (myPosition.getRow () - 1, myPosition.getColumn () - 2 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 2), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 2));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        //Down 2 Right 1
        } if (isValidPosition (myPosition.getRow () - 2, myPosition.getColumn () + 1 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 2, myPosition.getColumn () + 1), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - 2, myPosition.getColumn () + 1));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        //Down 2 Left 1
        } if (isValidPosition (myPosition.getRow () - 2, myPosition.getColumn () - 1 )) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 2, myPosition.getColumn () - 1), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - 2, myPosition.getColumn () - 1));
            if (nextPiece == null) {
                knightMoves.add (moves);
            } else if (nextPiece.pieceColor != currentPiece.pieceColor) {
                knightMoves.add (moves);
            }
        }
        return knightMoves;
    }

    private Collection<ChessMove> pawnMove(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> pawnMoves = new HashSet<> ();
        ChessPiece currentPiece = board.getPiece (myPosition);

        //WHITE PIECES
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            //Forward 2 at start
            if (isValidPosition (myPosition.getRow () + 2, myPosition.getColumn () )) {
                if (myPosition.getRow () == 2) {
                    ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 2, myPosition.getColumn ()), null);
                    ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 2, myPosition.getColumn ()));
                    if (nextPiece == null) {
                        pawnMoves.add (moves);
                    }
                }
            //Forward 1
            } if (isValidPosition (myPosition.getRow () + 1, myPosition.getColumn () )) {
                ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn ()), null);
                ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn ()));
                if (nextPiece == null) {
                    if ((myPosition.getRow () + 1) == 8) {
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn ()), PieceType.BISHOP);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn ()), PieceType.QUEEN);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn ()), PieceType.KNIGHT);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn ()), PieceType.ROOK);
                        pawnMoves.add (moves);
                    }
                    pawnMoves.add (moves);
                }
            //Capture Right
            } if (isValidPosition (myPosition.getRow () + 1, myPosition.getColumn () + 1)) {
                ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 1), null);
                ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 1));
                if (nextPiece != null && nextPiece.pieceColor != currentPiece.pieceColor) {
                    if ((myPosition.getRow () + 1) == 8) {
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 1), PieceType.BISHOP);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 1), PieceType.QUEEN);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 1), PieceType.KNIGHT);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () + 1), PieceType.ROOK);
                        pawnMoves.add (moves);
                    }
                    pawnMoves.add (moves);
                }
            //Capture Left
            } if (isValidPosition (myPosition.getRow () + 1, myPosition.getColumn () - 1)) {
                ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 1), null);
                ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 1));
                if (nextPiece != null && nextPiece.pieceColor != currentPiece.pieceColor) {
                    if ((myPosition.getRow () + 1) == 8) {
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 1), PieceType.BISHOP);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 1), PieceType.QUEEN);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 1), PieceType.KNIGHT);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + 1, myPosition.getColumn () - 1), PieceType.ROOK);
                        pawnMoves.add (moves);
                    }
                    pawnMoves.add (moves);
                }
            }
        //BLACK PIECES
        } if (pieceColor == ChessGame.TeamColor.BLACK) {
            //Forward 2 at start
            if (isValidPosition (myPosition.getRow () - 2, myPosition.getColumn () ) )  {
                if (myPosition.getRow () == 7) {
                    ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 2, myPosition.getColumn ()), null);
                    ChessPiece nextPiece_1 = board.getPiece (new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn ()));
                    ChessPiece nextPiece_2 = board.getPiece (new ChessPosition (myPosition.getRow () - 2, myPosition.getColumn ()));
                    if (nextPiece_1 == null && nextPiece_2 == null) {
                        pawnMoves.add (moves);
                    }
                }
            //Forward 1
            } if (isValidPosition (myPosition.getRow () - 1, myPosition.getColumn () )) {
                ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn ()), null);
                ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn ()));
                if (nextPiece == null) {
                    if ((myPosition.getRow () - 1) == 1) {
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn ()), PieceType.BISHOP);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn ()), PieceType.QUEEN);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn ()), PieceType.KNIGHT);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn ()), PieceType.ROOK);
                        pawnMoves.add (moves);
                    }
                    pawnMoves.add (moves);
                }
            //Capture Right
            } if (isValidPosition (myPosition.getRow () - 1, myPosition.getColumn () + 1)) {
                ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 1), null);
                ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 1));
                if (nextPiece != null && nextPiece.pieceColor != currentPiece.pieceColor) {
                    if ((myPosition.getRow () - 1) == 1) {
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 1), PieceType.BISHOP);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 1), PieceType.QUEEN);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 1), PieceType.KNIGHT);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () + 1), PieceType.ROOK);
                        pawnMoves.add (moves);
                    }
                    pawnMoves.add (moves);
                }
            //Capture Left
            } if (isValidPosition (myPosition.getRow () - 1, myPosition.getColumn () - 1)) {
                ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 1), null);
                ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 1));
                if (nextPiece != null && nextPiece.pieceColor != currentPiece.pieceColor) {
                    if ((myPosition.getRow () - 1) == 1) {
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 1), PieceType.BISHOP);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 1), PieceType.QUEEN);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 1), PieceType.KNIGHT);
                        pawnMoves.add (moves);
                        moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - 1, myPosition.getColumn () - 1), PieceType.ROOK);
                        pawnMoves.add (moves);
                    }
                    pawnMoves.add (moves);
                }
            }
        }
        return pawnMoves;
    }

    private Collection<ChessMove> queenMove (ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> queenMoves = new HashSet<> ();
        int counter = 1;

        //Checking upper right diagonal
        while (isValidPosition(myPosition.getRow () + counter, myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () + counter),null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () + counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking lower right diagonal
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () + counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn () + counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking lower left diagonal
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn () - counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking upper left diagonal
        while (isValidPosition(myPosition.getRow () + counter, myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn () - counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking straight up
        while (isValidPosition(myPosition.getRow () + counter, myPosition.getColumn ())) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn ()), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn ()));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking straight down
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn ())) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn ()), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn ()));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking straight right
        while (isValidPosition(myPosition.getRow (), myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow (), myPosition.getColumn () + counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow (),myPosition.getColumn () + counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking straight left
        while (isValidPosition(myPosition.getRow (), myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow (), myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow (),myPosition.getColumn () - counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                queenMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                queenMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }

        return queenMoves;


    }

    private Collection<ChessMove> rookMove (ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> rookMoves = new HashSet<> ();
        int counter = 1;
        //Checking straight up
        while (isValidPosition(myPosition.getRow () + counter, myPosition.getColumn ())) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () + counter, myPosition.getColumn ()), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () + counter,myPosition.getColumn ()));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                rookMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                rookMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking straight down
        while (isValidPosition(myPosition.getRow () - counter, myPosition.getColumn ())) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow () - counter, myPosition.getColumn ()), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow () - counter,myPosition.getColumn ()));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                rookMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                rookMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking straight right
        while (isValidPosition(myPosition.getRow (), myPosition.getColumn () + counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow (), myPosition.getColumn () + counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow (),myPosition.getColumn () + counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                rookMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                rookMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }
        counter = 1;
        //Checking straight left
        while (isValidPosition(myPosition.getRow (), myPosition.getColumn () - counter)) {
            ChessMove moves = new ChessMove (myPosition, new ChessPosition (myPosition.getRow (), myPosition.getColumn () - counter), null);
            ChessPiece nextPiece = board.getPiece (new ChessPosition (myPosition.getRow (),myPosition.getColumn () - counter));
            ChessPiece currentPiece = board.getPiece (myPosition);
            System.out.println (nextPiece);
            if (nextPiece == null) {
                System.out.println ("====");
                rookMoves.add(moves);
                System.out.println (moves);
                counter += 1;
            } else if (nextPiece.pieceColor != currentPiece.pieceColor){
                rookMoves.add(moves);
                System.out.println (moves);
                break;
            } else {
                break;
            }
        }

        return rookMoves;
    }


    private boolean isValidPosition (int row, int col) {
        return (row >= 1 && row <= 8 && col >= 1 && col <= 8);
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

    @Override
    public String toString () {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
