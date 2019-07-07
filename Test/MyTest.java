import org.junit.*;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MyTest {
    @Test
    public void validLocationKnightTest() {
        Board b = new Board();
        Piece knight = b.getPiece(1,0);
        assertEquals("is knight", "Kn", knight.getName());
        ArrayList<Point> pts = new ArrayList<>();
        pts.add(new Point(2, 2));
        pts.add(new Point(0, 2));

        assertEquals("equal validPoints", pts, knight.getValidLocations());
    }
    @Test
    public void PawnMovementTest() {
        Board b = new Board();
        Piece pawn = b.getPiece(2, 1);
        pawn.moveTo(new Point(2, 2));
        assertEquals("pawn moved", new Point(2, 2), pawn.l);

        ArrayList<Point> pts = new ArrayList<>();
        pts.add(new Point(2, 3));
        assertEquals("equal validPoints", pts, pawn.getValidLocations());

        Piece pawn2 = b.getPiece(5, 1);
        b.movePiece(new Point(5,1), new Point(5,3));
        assertEquals("pawn2 moved", new Point(5, 3), pawn2.l);
        assertEquals("pawn2 moved", null, b.getPiece(5,1) );

        ArrayList<Point> pts2 = new ArrayList<>();
        pts2.add(new Point(5, 4));
        assertEquals("equal validPoints", pts2, pawn2.getValidLocations());
    }
    @Test
    public void inBoardTest() {
        Board b = new Board();
        assertTrue("in board", b.inBoard(3, 4));
        assertTrue("0,0 in board", b.inBoard(0, 0));
        assertTrue("7,7 in board", b.inBoard(7, 7));
        assertFalse("4,8 not in board", b.inBoard(4, 8));
        assertFalse("8,8 not in board", b.inBoard(8, 8));
        assertFalse("-1,-1 not in board", b.inBoard(-1, -1));



    }

    @Test
    public void openTest() {
        Board b = new Board();
        assertFalse("not open", b.open(0, 0));
        assertTrue("3,3 open", b.open(3, 3));
        assertFalse("7,6 open", b.open(7, 6));
    }
    @Test
    public void rookMovementLocationsTest() {
        Board b = new Board();
        Piece rook = b.getPiece(0,0);
        b.movePiece(new Point(0,0), new Point(4,4));
        assertEquals("rook didn't move", new Point(0,0), rook.l);
        assertEquals("no place to go", new ArrayList<Point>(), rook.getValidLocations());
        Piece pawn = b.getPiece(0,1);
        b.movePiece(new Point(0,1), new Point(0,3));
        assertEquals("pawn moved", "P", b.getPiece(0,3).getName());
        assertEquals("pawn moved", null, b.getPiece(0,1));
        assertEquals("rook has place to move", 2, rook.getValidLocations().size());
        ArrayList<Point> pts = new ArrayList<>();
        pts.add(new Point(0,1));
        pts.add(new Point(0,2));
        assertEquals("equal valid locations", pts, rook.getValidLocations());
    }

    @Test
    public void knightMovementLocationsTest() {
        Board b = new Board();
        Piece knight = b.getPiece(1, 0);
        b.movePiece(new Point(1, 0), new Point(2, 2));
        assertEquals("knight moved", new Point(2, 2), knight.l);
        ArrayList<Point> pts = new ArrayList<>();
        pts.add(new Point(0,3));
        pts.add(new Point(1, 4));
        pts.add(new Point(3, 4));
        pts.add(new Point(4, 3));
        pts.add(new Point(1, 0));
        assertTrue("correct places1", knight.getValidLocations().containsAll(pts));
        assertTrue("correct places2", pts.containsAll(knight.getValidLocations()));


    }
    @Test
    public void checkmateTest() {
        Board b = new Board();
        b.movePiece(new Point(5, 1), new Point(5, 2));
        b.movePiece(new Point(4, 6), new Point(4, 5));
        b.movePiece(new Point(6, 1), new Point(6, 3));
        b.movePiece(new Point(3, 7), new Point(7, 3));
        Piece king = b.getKing(false);
        assertTrue("white king is checked", b.attackedByOpposite(king.l.x, king.l.y, false));
        b.movePiece(new Point(7, 3), new Point(6, 3));
     //   assertFalse("white king is not checked", b.whiteIsChecked());
     //   assertNotEquals("white pieces can move", 0, b.getPiece(0, 1).getValidLocations().size());


    }

    @Test
    public void checkTest() {
        Board b = new Board();
        b.movePiece(new Point(5, 1), new Point(5, 2));
        b.movePiece(new Point(4, 6), new Point(4, 5));
        b.movePiece(new Point(6, 1), new Point(6, 3));
        b.movePiece(new Point(3, 6), new Point(3, 4));
        b.movePiece(new Point(4, 1), new Point(4, 2));
        b.movePiece(new Point(3, 7), new Point(7, 3));
        Piece king = b.getKing(false);
     //   assertTrue("white king is checked", b.attackedByOpposite(king.l.x, king.l.y, false));
        //assertNotEquals("white king has place to move", 0, king.getValidLocations().size());
         //  assertFalse("white king is not checked", b.whiteIsChecked());
         //  assertNotEquals("white pieces can move", 0, b.getPiece(0, 1).getValidLocations().size());


    }


}
