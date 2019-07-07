import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Piece {
    String name;
    Board board;
    Point l;
    boolean isBlack;


    Piece(int x, int y, Board board, Boolean isBlack) {
        this.l = new Point(x, y);
        this.board = board;
        this.isBlack = isBlack;


    }
    Piece(Point p, Board board, Boolean isBlack) {
        this.l = p;
        this.board = board;
        this.isBlack = isBlack;

    }

    public boolean moveTo(Point n) {

        if (getValidLocations().contains(n)) {
            l = n;
            return true;
        } else {
            return false;
        }
    }

    public boolean moveTo(int x, int y) {

        if (getValidLocations().contains(new Point(x,y))) {
            l = new Point(x,y);
            return true;
        } else {
            return false;
        }
    }

    public  ArrayList<Point> getValidLocations() {

        ArrayList<Point> out = getRange();
        Piece king = board.getKing(isBlack);

        if (board.inRangeByOpposite(king.l.x, king.l.y, isBlack)) {
            Iterator ite = out.iterator();
            while (ite.hasNext()) {
                Point p = (Point) ite.next();
                Board newBoard = new Board(board.getBoard(), !isBlack, board.getPrevBoard());
                Piece newKing = newBoard.getKing(isBlack);

                Piece temp = newBoard.getPiece(l.x, l.y);
                newBoard.forceMove(temp, p);


                if (newBoard.attackedByOpposite(newKing.l.x, newKing.l.y, isBlack)) {
                    ite.remove();
                }
            }
        }
        return out;
    }

    public abstract ArrayList<Point> getRange();
    public String getName() {
        return name;
    }
    public abstract ArrayList<Point> rangeWithoutBlocking();
    public abstract Piece deepCopy(Board b);

}

//=====================================================================
//=====================================================================
class Knight extends Piece {
    final String name;
    BufferedImage img;

    public Knight(Point l, Board board, boolean isBlack) {
        super(l, board, isBlack);
        name = "Kn";


    }


    public Knight(int x, int y, Board board, boolean b) {
        super(x, y, board, b);
        name = "Kn";



    }
//

    @Override
    public ArrayList<Point> getRange() {

        int[][] directions = new int[][] {{1, 2}, {1, -2}, {-1, -2}, {-1, 2}, {2, -1}, {-2, 1}, {2, 1}, {-2, -1}};
        ArrayList<Point> out = new ArrayList<>();
        for (int[] d : directions) {
            Point p = new Point(l.x + d[0], l.y + d[1]);
           // System.out.println(p);

            if (!board.inBoard(p)) {
                continue;
            }
          //  System.out.println(board.open(p));
            if (board.open(p)) {
                out.add(new Point(p));
            } else {
                if (board.getPiece(p).isBlack != isBlack) {
                    out.add(new Point(p));
                }

            }
        }
        return out;
    }

    @Override
    public ArrayList<Point> rangeWithoutBlocking() {
        int[][] directions = new int[][] {{1, 2}, {1, -2}, {-1, -2}, {-1, 2}, {2, -1}, {-2, 1}, {2, 1}, {-2, -1}};
        ArrayList<Point> out = new ArrayList<>();
        for (int[] d : directions) {
            Point p = new Point(l.x + d[0], l.y + d[1]);

            if (!board.inBoard(p)) {
                continue;
            }
            if (board.open(p)) {
                out.add(new Point(p));
            } else {
                if (board.getPiece(p).isBlack == isBlack) {
                    continue;
                }
                out.add(new Point(p));

            }
        }
        return out;
    }

    public String getName() {
        return name;
    }

    @Override
    public Knight deepCopy(Board b) {
        return new Knight(l, b , isBlack);
    }

    @Override
    public String toString() {
        return "Knight{" +
                "name='" + name + '\'' +
                '}';
    }
}

class Pawn extends Piece {

    final String name;
    boolean initial;
    public void promote(Piece newPiece) {
        board.getBoard()[l.x][l.y] = newPiece;
        if (isBlack) {
            board.black.remove(this);
            board.white.add(newPiece);
        } else {
            board.white.remove(this);
            board.white.add(newPiece);
        }

    }


    public Pawn(Point l, Board board, boolean b, boolean initial) {

        super(l, board, b);
        name = "P";
        this.initial = initial;




    }


    public Pawn(int x, int y, Board board, boolean b, boolean initial) {
        super(x, y, board, b);
        this.initial = initial;
        name = "P";


    }
    public int getRank() {
        if (isBlack) {
            return 8 - l.y;
        } else {
            return l.y + 1;
        }
    }
    @Override
    public ArrayList<Point> getValidLocations() {
        ArrayList<Point> out = super.getValidLocations();
        Point enPassantPoint = enPassantChecker();
        if (enPassantPoint != null) {
            out.add(enPassantPoint);
        }

        return out;


    }
    public Point enPassantChecker() {
        if (getRank() != 5) {
            return null;
        }

        Point pointToCheck = new Point(l.x - 1, l.y);
        Point pointToCheck2 = new Point(l.x + 1, l.y);
        int y;
        int ny;
        if (isBlack) {
             y = 1;
             ny = 2;
        } else {
            y = 6;
            ny = 5;
        }
        if (board.inBoard(pointToCheck)) {
            Piece captured = board.getPiece(pointToCheck);



            if (captured != null && captured.getName().equals("P") &&
                    board.getPiece(l.x - 1, y) == null) {
                if (board.getPrevBoard()[l.x - 1][l.y] == null &&
                        board.getPrevBoard()[l.x - 1][y] != null) {
                    return new Point(l.x - 1, ny);

                }
            }

        }
        if (board.inBoard(pointToCheck2)) {

            Piece captured = board.getPiece(pointToCheck2);

            if (captured != null && captured.getName().equals("P") &&
                    board.getPiece(l.x + 1, y) == null) {

                if (board.getPrevBoard()[l.x + 1][l.y] == null &&
                        board.getPrevBoard()[l.x + 1][y] != null) {

                    return new Point(l.x + 1, ny);
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<Point> getRange() {

        int k;
        if (isBlack) {
            k = -1;
        } else {
            k = 1;
        }

        ArrayList<Point> out = new ArrayList<>();

        Point p1 = new Point(l.x, l.y + k);
        Point p2 = new Point(l.x + 1, l.y + k);
        Point p3 = new Point(l.x - 1, l.y + k);
        Point p4 = new Point(l.x, l.y + 2 * k);
        if (board.inBoard(p1) && board.open(p1)) {
            out.add(p1);
        }
        if (board.inBoard(p2)) {
            if (!board.open(p2) && board.getPiece(p2).isBlack != isBlack) {
                out.add(p2);
            }
        }

        if (board.inBoard(p3)) {
            if (!board.open(p3) && board.getPiece(p3).isBlack != isBlack) {
                out.add(p3);
            }
        }
        if (initial) {
            if (out.contains(p1) && board.open(p4)) {
                out.add(p4);
            }
        }
        return out;
    }

    @Override
    public ArrayList<Point> rangeWithoutBlocking() {
        return getRange();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean moveTo(Point n) {



        if (getValidLocations().contains(n)) {
            if (board.open(n) && n.x != l.x) {
                if (isBlack) {
                    Piece toBeRemoved = board.getBoard()[n.x][n.y + 1];
                    board.white.remove(toBeRemoved);
                    board.getBoard()[n.x][n.y + 1] = null;
                    if (getRank() == 8) {
                        board.getBoard()[l.x][l.y] = new Queen(l.x, l.y, board, isBlack);

                        board.black.remove(this);
                        board.black.add(board.getPiece(l.x, l.y));

                    }


                } else {

                    Piece toBeRemoved = board.getBoard()[n.x][n.y - 1];
                    board.black.remove(toBeRemoved);
                    board.getBoard()[n.x][n.y - 1] = null;
                    if (getRank() == 8) {
                        board.getBoard()[l.x][l.y] = new Queen(l.x, l.y, board, isBlack);

                        board.black.remove(this);
                        board.black.add(board.getPiece(l.x, l.y));

                    }
                }

            }

            l = n;
            initial = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Pawn deepCopy(Board b) {
        return new Pawn(l, b, isBlack, initial);
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "name='" + name + '\'' +
                '}';
    }
}

class Queen extends Piece {
    final String name;
    public Queen(Point l, Board board, boolean b) {
        super(l, board, b);
        name = "Q";

    }

    public Queen(int x, int y, Board board, boolean b) {
        super(x, y, board, b);
        name = "Q";

    }

    @Override
    public ArrayList<Point> getRange() {

        int[][] directions = new int[][] {{1, 1}, {1, -1}, {-1, -1}, {-1, 1},
                {1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        ArrayList<Point> out = new ArrayList<>();

        for (int[] d : directions) {
            for (int i = 1; board.inBoard(l.x + d[0] * i, l.y + d[1] * i); i++) {
                Point p = new Point(l.x + d[0] * i, l.y + d[1] * i);
                if (board.open(p)) {
                    out.add(new Point(p));
                } else {
                    if (board.getPiece(p).isBlack != isBlack) {
                        out.add(new Point(p));
                    }
                    break;
                }
            }
        }
        return out;
    }

    @Override
    public ArrayList<Point> rangeWithoutBlocking() {

        int[][] directions = new int[][] {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        ArrayList<Point> out = new ArrayList<>();

        for (int[] d : directions) {
            for (int i = 1; board.inBoard(l.x + d[0] * i, l.y + d[1] * i); i++) {
                Point p = new Point(l.x + d[0] * i, l.y + d[1] * i);
                if (board.open(p)) {
                    out.add(new Point(p));
                } else {
                    if (board.getPiece(p).isBlack == isBlack) {
                        break;
                    }
                    out.add(new Point(p));
                }
            }
        }
        return out;
    }

    public String getName() {
        return name;
    }

    @Override
    public Queen deepCopy(Board b) {
        return new Queen(l, b, isBlack);
    }
}

class King extends Piece {
    final String name;
    private boolean initial;
    public King(Point l, Board board, boolean b, boolean initial) {
        super(l, board, b);
        name = "K";
        this.initial = initial;

    }

    public King(int x, int y, Board board, boolean b, boolean initial) {
        super(x, y, board, b);
        name = "K";
        this.initial = initial;

    }

    @Override
    public ArrayList<Point> getValidLocations() {
        ArrayList<Point> out = getRange();
        Piece king = board.getKing(isBlack);

        Iterator ite = out.iterator();
        while (ite.hasNext()) {
            Point p = (Point) ite.next();
            Board newBoard = new Board(board.getBoard(), !isBlack, board.getPrevBoard());
            Piece newKing = newBoard.getKing(isBlack);

            Piece temp = newBoard.getPiece(l.x, l.y);
            newBoard.forceMove(temp, p);


            if (newBoard.attackedByOpposite(newKing.l.x, newKing.l.y, isBlack)) {
                ite.remove();
            }
        }
        boolean allOpen = true;
        for (int i = (l.x + 1); i < 7; i++) {
            if (!board.open(i, l.y)) {
                allOpen = false;
            }
        }
        if (board.attackedByOpposite(l.x, l.y, isBlack)) {
            return out;
        }
        if (initial && allOpen) {
            if (board.getPiece(7, l.y) != null && board.getPiece(7, l.y).getName().equals("R")) {
                Rook r1 = (Rook) board.getPiece(7, l.y);
                if (r1.isInitial()) {

                    if(!board.attackedByOpposite(5, l.y, isBlack)
                            && !board.attackedByOpposite(6, l.y, isBlack)) {
                        out.add(new Point(6, l.y));
                    }

                }
            }
        }

        allOpen = true;
        for (int i = (l.x - 1); i > 0; i--) {
            if (!board.open(i, l.y)) {
                allOpen = false;
            }
        }

        if (initial && allOpen) {
            if (board.getPiece(0, l.y) != null && board.getPiece(0, l.y).getName().equals("R")) {
                Rook r2 = (Rook) board.getPiece(0, l.y);
                if (r2.isInitial()) {
                    if(!board.attackedByOpposite(3, l.y, isBlack)
                            && !board.attackedByOpposite(2, l.y, isBlack)) {
                        out.add(new Point(2, l.y));
                    }

                }
            }
        }
        return out;
    }

    @Override
    public ArrayList<Point> getRange() {

        int[][] directions = new int[][] {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        ArrayList<Point> out = new ArrayList<>();
        for (int[] d : directions) {

            Point p = new Point(l.x + d[0], l.y + d[1]);

            if (!board.inBoard(p)) {
                continue;
            }
            if (board.open(p)) {
                out.add(new Point(p));
            } else {
                if (board.getPiece(p).isBlack != isBlack) {
                    out.add(new Point(p));
                }
            }
        }
        return out;

    }

    @Override
    public ArrayList<Point> rangeWithoutBlocking() {

        int[][] directions = new int[][] {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        ArrayList<Point> out = new ArrayList<>();

        for (int[] d : directions) {

            Point p = new Point(l.x + d[0], l.y + d[1]);

            out.add(p);
        }
        return out;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean moveTo(Point n) {
        Point old = l;
        boolean out = super.moveTo(n);
        if (out) {
            if (n.x == old.x + 2) {
                Rook r1 = (Rook) board.getPiece(7, old.y);
                board.forceMove(r1, new Point(n.x - 1, n.y));
                r1.setInitial(false);


            } else if (n.x == l.x - 2) {

                Rook r2 = (Rook) board.getPiece(0, old.y);
                board.forceMove(r2, new Point(n.x + 1, n.y));
                r2.setInitial(false);

            }
        }
        if (out) {
            initial = false;
        }
        return out;
    }

    @Override
    public boolean moveTo(int x, int y) {

        boolean out = super.moveTo(x, y);

        if (out) {
            if (x == l.x + 2) {
                board.movePiece(new Point(7, l.y), new Point(x - 1, y));
            } else if (x == l.x - 2) {
                board.movePiece(new Point(0, l.y), new Point(x + 1, y));
            }
        }

        if (out) {
            initial = false;
        }
        return out;
    }

    @Override
    public King deepCopy(Board b) {
        return new King(l, b, isBlack, initial);
    }
}

class Bishop extends Piece {
    final String name;
    public Bishop(Point l, Board board, boolean b) {
        super(l, board, b);
        name = "B";

    }

    public Bishop(int x, int y, Board board, boolean b) {
        super(x, y, board, b);
        name = "B";

    }

    @Override
    public ArrayList<Point> getRange() {

        int[][] directions = new int[][] {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        ArrayList<Point> out = new ArrayList<>();

        for (int[] d : directions) {
            for (int i = 1; board.inBoard(l.x + d[0] * i, l.y + d[1] * i); i++) {
                Point p = new Point(l.x + d[0] * i, l.y + d[1] * i);
                if (board.open(p)) {
                    out.add(new Point(p));
                } else {
                    if (board.getPiece(p).isBlack != isBlack) {
                        out.add(new Point(p));
                    }
                    break;
                }
            }
        }
        return out;
    }

    @Override
    public ArrayList<Point> rangeWithoutBlocking() {

        int[][] directions = new int[][] {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        ArrayList<Point> out = new ArrayList<>();

        for (int[] d : directions) {
            for (int i = 1; board.inBoard(l.x + d[0] * i, l.y + d[1] * i); i++) {
                Point p = new Point(l.x + d[0] * i, l.y + d[1] * i);
                if (board.open(p)) {
                    out.add(new Point(p));
                } else {
                    if (board.getPiece(p).isBlack == isBlack) {
                        break;
                    }
                    out.add(p);
                }
            }
        }
        return out;
    }

    public String getName() {
        return name;
    }

    @Override
    public Bishop deepCopy(Board b) {
        return new Bishop(l, b, isBlack);
    }
}

class Rook extends Piece {
    private boolean initial;

    public boolean isInitial() {
        return initial;
    }

    final String name;
    public Rook(Point l, Board board, boolean b, boolean initial) {
        super(l, board, b);
        name = "R";
        this.initial = initial;

    }

    public Rook(int x, int y, Board board, boolean b, boolean initial) {
        super(x, y, board, b);
        name = "R";
        this.initial = initial;

    }


    @Override
    public ArrayList<Point> getRange() {


        int[][] directions = new int[][] {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        ArrayList<Point> out = new ArrayList<>();
        int k = 0;
        for (int[] d : directions) {
            for (int i = 1; board.inBoard(l.x + d[0] * i, l.y + d[1] * i); i++) {
                Point p = new Point(l.x + d[0] * i, l.y + d[1] * i);
                if (board.open(p)) {
                    out.add(new Point(p));
                } else {

                    if (board.getPiece(p).isBlack != isBlack) {
                        out.add(new Point(p));
                    }
                    break;
                }
            }
        }
        return out;
    }

    @Override
    public ArrayList<Point> rangeWithoutBlocking() {


        int[][] directions = new int[][] {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        ArrayList<Point> out = new ArrayList<>();
        int k = 0;
        for (int[] d : directions) {
            for (int i = 1; board.inBoard(l.x + d[0] * i, l.y + d[1] * i); i++) {
                Point p = new Point(l.x + d[0] * i, l.y + d[1] * i);
                if (board.open(p)) {
                    out.add(new Point(p));
                } else {

                    if (board.getPiece(p).isBlack == isBlack) {
                        break;
                    }
                    out.add(new Point(p));
                }
            }
        }
        return out;
    }

    @Override
    public boolean moveTo(Point n) {
        boolean out = super.moveTo(n);

        if (out) {
            initial = false;
        }
        return out;
    }

    @Override
    public boolean moveTo(int x, int y) {
        boolean out = super.moveTo(x, y);


        if (out) {
            initial = false;
        }
        return out;
    }

    @Override
    public Rook deepCopy(Board b) {
        return new Rook(l, b, isBlack, initial);
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public String getName() {
        return name;
    }
}

