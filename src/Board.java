import java.awt.*;
import java.util.ArrayList;

public class Board {
    private Piece[][] board;
    ArrayList<Piece> white;

    private Piece blackKing;
    private Piece whiteKing;
    ArrayList<Piece> black;
    private boolean whiteTurn;
    private Piece[][] prevBoard;
    public Board() {
        white = new ArrayList<>();
        black = new ArrayList<>();
        board = new Piece[8][8];
        whiteTurn = true;
        prevBoard = null;

        String[] setup = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};
        for (int i = 0; i <= 7; i++) {
            String piece = setup[i];
            if (piece .equals("Rook")) {
                board[i][7] = new Rook(i, 7, this, true, true);
                board[i][6] = new Pawn(i, 6, this, true, true);

                board[i][0] = new Rook(i, 0, this, false, true);
                board[i][1] = new Pawn(i, 1, this, false, true);
            }

            if (piece .equals("Knight")) {
                board[i][7] = new Knight(i, 7, this, true);
                board[i][6] = new Pawn(i, 6, this, true, true);

                board[i][0] = new Knight(i, 0, this, false);
                board[i][1] = new Pawn(i, 1, this, false, true);
            }

            if (piece .equals("Bishop")) {
                board[i][7] = new Bishop(i, 7, this, true);
                board[i][6] = new Pawn(i, 6, this, true, true);

                board[i][0] = new Bishop(i, 0, this, false);
                board[i][1] = new Pawn(i, 1, this, false, true);
            }

            if (piece .equals("Queen")) {
                board[i][7] = new Queen(i, 7, this, true);
                board[i][6] = new Pawn(i, 6, this, true, true);

                board[i][0] = new Queen(i, 0, this, false);
                board[i][1] = new Pawn(i, 1, this, false, true);
            }

            if (piece .equals("King")) {
                board[i][7] = new King(i, 7, this, true, true);
                board[i][6] = new Pawn(i, 6, this, true, true);

                blackKing = board[i][7];

                board[i][0] = new King(i, 0, this, false, true);
                board[i][1] = new Pawn(i, 1, this, false, true);

                whiteKing = board[i][0];
            }
        }
        for (Piece[] i : board) {
            for (Piece j : i) {
                if (j != null) {
                    if (j.isBlack) {
                        black.add(j);
                    } else {
                        white.add(j);
                    }
                }
            }
        }
    }

    public Piece[][] getPrevBoard() {
        return prevBoard;
    }

    public Board(Piece[][] b, boolean whiteTurn, Piece[][] prev) {
        white = new ArrayList<>();
        black = new ArrayList<>();
        board = new Piece[8][8];
        this.whiteTurn = whiteTurn;

        prevBoard = prev;
        board = clone(b);


        for (Piece[] i : board) {
            for (Piece j : i) {
                if (j != null) {
                    if (j.isBlack) {
                        black.add(j);
                        if (j.getName().equals("K")) {
                            blackKing = j;
                        }
                    } else {
                        white.add(j);

                        if (j.getName().equals("K")) {
                            whiteKing = j;
                        }
                    }
                }
            }
        }
    }

    public boolean movePiece(Point p1, Point p2) {
        if (getPiece(p1) == null) {
            return false;
        }
        if (getPiece(p1).isBlack == whiteTurn) {
            return false;
        }

        if (getPiece(p1).moveTo(p2)) {

            if (!open(p2)) {
                boolean isBlack = getPiece(p1).isBlack;
                if (isBlack) {
                    white.remove(getPiece(p2));

                } else {
                    black.remove(getPiece(p2));
                }
            }
            prevBoard = clone(board);

            board[p2.x][p2.y] = getPiece(p1);
            board[p1.x][p1.y] = null;
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }

    public Piece[][] getBoard() {
        return board;
    }
    public boolean attackedByOpposite(int x, int y, boolean isBlack) {
        if (!isBlack) {
            for (Piece i : black) {

                if (i.getRange().contains(new Point(x, y))) {
                    return true;
                }
            }
            return false;
        } else {

            for (Piece i : white) {
                if (i.getRange().contains(new Point(x, y))) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean inRangeByOpposite(int x, int y, boolean isBlack) {
        if (!isBlack) {
            for (Piece i : black) {
                if (i.rangeWithoutBlocking().contains(new Point(x, y))) {
                    return true;
                }
            }
            return false;
        } else {

            for (Piece i : white) {
                if (i.rangeWithoutBlocking().contains(new Point(x, y))) {
                    return true;
                }
            }
            return false;
        }
    }
    public boolean hasPlaceToMove(boolean isBlack) {
        if (isBlack) {
            for (Piece i : black) {
                if (i.getValidLocations().size() != 0) {
                    return true;
                }
            }
            return false;
        } else {

            for (Piece i : white) {
                if (i.getValidLocations().size() != 0) {
                    return true;
                }
            }
            return false;
        }

    }
    public boolean checked(boolean isBlack) {
        if (isBlack) {
            return attackedByOpposite(blackKing.l.x, blackKing.l.y, true);
        } else {
            return attackedByOpposite(whiteKing.l.x, whiteKing.l.y, false);
        }
    }

    /**
     * checks if there's a checkmate or stalemate
     * @return 0 if no checkmate, 1 if black is the target of checkmate, 2 if white,
     * and 4 if stalemate.
     */
    public int checkMateChecker() {

        if (whiteTurn) {
            if (!hasPlaceToMove(false)) {
                if (checked(false)) {
                    return 2;
                }
                return 4;
            }
            return 0;
        } else {

            if (!hasPlaceToMove(true)) {
                if (checked(true)) {
                    return 2;
                }
                return 4;
            }
            return 0;
        }

    }


    public Piece getPiece(Point l) {
        return board[l.x][l.y];
    }
    public Piece getPiece(int x, int y) {
        return board[x][y];
    }
    public boolean inBoard(int x, int y) {
        return x<= 7 && x >= 0 && y <= 7 && y >= 0;
    }
    public boolean open(int x, int y) {
        return board[x][y] == null;
    }

    public boolean open(Point p) {
        return board[p.x][p.y] == null;
    }
    public void forceMove(Piece object, Point p) {

        Piece other = getPiece(p);
        if (other != null) {
            if (other.isBlack) {
                black.remove(other);

            } else {
                white.remove(other);
            }
        }
        board[object.l.x][object.l.y] = null;
        board[p.x][p.y] = object;

        object.l = p;

    }



    public Piece getKing(boolean isBlack) {
        if (isBlack) {
            return blackKing;
        } else {
            return whiteKing;
        }
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean inBoard(Point p) {
        return p.x<= 7 && p.x >= 0 && p.y <= 7 && p.y >= 0;
    }
    public Piece[][] clone(Piece[][] b) {
        Piece[][] board = new Piece[8][8];
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (b[i][j] != null) {
                    board[i][j] = b[i][j].deepCopy(this);
                }
            }
        }
        return board;
    }
}
