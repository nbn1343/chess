package ui;

public class ChessboardPrinter {

  public static void printChessboard(String playerColor) {
    String[][] chessboard = new String[8][8];

    String[] initialRow = {"R", "N", "B", "Q", "K", "B", "N", "R"};
    for (int i = 0; i < 8; i++) {
      if (playerColor.equalsIgnoreCase("white")) {
        chessboard[0][i] = "b" + initialRow[i];
        chessboard[1][i] = "bp";
        chessboard[6][i] = "wp";
        chessboard[7][i] = "w" + initialRow[i];
      } else {
        chessboard[0][i] = "w" + initialRow[i];
        chessboard[1][i] = "wp";
        chessboard[6][i] = "bp";
        chessboard[7][i] = "b" + initialRow[i];
      }
    }

    System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
    System.out.print(EscapeSequences.moveCursorToLocation(1, 1));
    System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

    if (playerColor.equalsIgnoreCase("white")) {
      System.out.print("    a  b  c  d  e  f  g  h     \n");
    } else {
      System.out.println("    h  g  f  e  d  c  b  a     ");
    }

    for (int i = 0; i < 8; i++) {
      System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
      if (playerColor.equalsIgnoreCase("white")) {
        System.out.print(" " + (i + 1) + " ");
      } else {
        System.out.print(" " + (8 - i) + " ");
      }

      for (int j = 0; j < 8; j++) {
        if ((i + j) % 2 == 0) {
          System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        } else {
          System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        }

        String square = chessboard[i][j];
        if (square != null) {
          if (square.startsWith("w")) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
          } else {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
          }
          System.out.print(" " + square.substring(1) + " ");
        } else {
          System.out.print("   ");
        }

        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
      }

      System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
      if (playerColor.equalsIgnoreCase("white")) {
        System.out.println(" " + (i + 1) + " ");
      } else {
        System.out.println(" " + (8 - i) + " ");
      }
    }

    if (playerColor.equalsIgnoreCase("white")) {
      System.out.println("    a  b  c  d  e  f  g  h     ");
    } else {
      System.out.println("    h  g  f  e  d  c  b  a     ");
    }
  }

  public static void main(String[] args) {

    printChessboard("black");
    System.out.println (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    printChessboard("white");
  }
}


