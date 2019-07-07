import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class GameBoard extends JFrame {

    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;

    public GameBoard() {
        setSize(1200, 800);
        setLocationByPlatform(true);
        setTitle("hello");
        add(new ChessBoard(), BorderLayout.CENTER);
        pack();
    }
    public void reset() {

        setSize(800, 400);
        setLocationByPlatform(true);
        setTitle("hello");
        add(new ChessBoard());
        pack();
    }
}
class ChessBoard extends JComponent {
    public boolean playing = true; // whether the game is running

    int w = 700;
    int h = 700;
    Board b;
    Piece[][] board;
    Point pointSelected = null;
    int size = h / 8;
    Map<String, BufferedImage> getImg;

    public void resign() {
        getGraphics().drawString("You lost.", 0, 350);
        getGraphics().drawString("Click anywhere to begin a new game", 0, 450);

        b = new Board();
        board = b.getBoard();
        playing = true;

    }

    public void reset() {

        b = new Board();
        board = b.getBoard();
        playing = true;
        repaint();


    }

    public ChessBoard() {
        setFont(new Font("Serif", Font.BOLD, 40));
        b = new Board();
        board = b.getBoard();
        getImg = new TreeMap<>();
        try {
            BufferedImage img = ImageIO.read(new File("files/BlackKing.png"));
            getImg.put("K b", img);
            getImg.put("K w", ImageIO.read(new File("files/WhiteKing.png")));
            getImg.put("Kn w", ImageIO.read(new File("files/WhiteKnight.png")));
            getImg.put("Kn b", ImageIO.read(new File("files/BlackKnight.png")));
            getImg.put("P b", ImageIO.read(new File("files/BlackPawn.png")));
            getImg.put("P w", ImageIO.read(new File("files/WhitePawn.png")));
            getImg.put("B w", ImageIO.read(new File("files/WhiteBishop.png")));
            getImg.put("B b", ImageIO.read(new File("files/BlackBishop.png")));
            getImg.put("R b", ImageIO.read(new File("files/BlackRook.png")));
            getImg.put("R w", ImageIO.read(new File("files/WhiteRook.png")));
            getImg.put("Q b", ImageIO.read(new File("files/BlackQueen.png")));
            getImg.put("Q w", ImageIO.read(new File("files/WhiteQueen.png")));




        } catch (IOException e) {
            System.out.println("Internal Error: " + e.getMessage());

        }
        addMouseListener(new MouseHandler());


    }

    public void paintSquareAtLocation(Graphics g, Color c, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(c);
        Rectangle2D rect = new Rectangle2D.Double(x * size, (y) * size, size, size);
        g2.fill(rect);

    }
    public void paintCanvas(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(238, 238, 210));

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if ((i + j) % 2 == 1) {
                    g2.setColor(new Color(118, 150, 86));

                } else {
                    g2.setColor(new Color(238, 238, 210));
                }

                Rectangle2D rect = new Rectangle2D.Double(i * size, j * size, size, size);

                g2.fill(rect);

            }
        }

    }
    public void paintBlocks(Graphics g) {
        if (pointSelected == null || b.open(pointSelected)) {
            return;
        }

        ArrayList<Point> pts = b.getPiece(pointSelected).getValidLocations();
        for (Point p : pts) {
            paintSquareAtLocation(g, new Color(64, 255, 0), p.x, 7 - p.y);
        }
    }
    public void paintPieces(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        for (Piece i : b.black) {
            String name = i.getName();
            int x = i.l.x * size ;
            int y = (7 - i.l.y) * size;
            g.drawImage(getImg.get(i.getName() + " b"), x, y, null);
        }

        g2.setColor(Color.RED);
        for (Piece i : b.white) {
            String name = i.getName();
            int x = i.l.x * size;
            int y = (7 - i.l.y) * size ;
//
//            Image img;
//            String IMAGE_FILE = Piece.getImageFile(i.getName(), i.isBlack);
//
//            img = Piece.getImage(i.getName(), i.isBlack);


            g.drawImage(getImg.get(i.getName() + " w"), x, y, null);


        }

//
//        for (int i = 0; i <= 7; i++) {
//            for (int j = 0; j <= 7; j++) {
//
//                if (board[i][7-j] != null) {
//                    if (board[i][7-j].isBlack) {
//                        g2.setColor(Color.BLACK);
//                    } else {
//                        g2.setColor(Color.RED);
//                    }
//                    String name = board[i][7 - j].getName();
//                    g2.drawString(name, (i * size + size / 2), (j * size + size / 2));
//                }
//            }
//        }
    }

    @Override
    public void paintComponent(Graphics g) {
        paintCanvas(g);
        paintBlocks(g);
        paintPieces(g);
        if (playing == false) {
            int result = b.checkMateChecker();

            if (result == 1) {
                System.out.println("checkp1");
                JOptionPane.showMessageDialog(null,
                        "You won! press reset to restart");



            } else if (result == 2) {
                System.out.println("checkp1");
                JOptionPane.showMessageDialog(null,
                        "You lost! press reset to restart");


            } else {
                System.out.println("checkp1");
                JOptionPane.showMessageDialog(null,
                        "Draw! press reset to restart");



            }
        }



    }


    private class MouseHandler extends MouseAdapter {


        @Override
        public void mousePressed(MouseEvent event) {
            System.out.println(playing + "  " + b.checkMateChecker());
            if (playing) {

                int x = event.getX();
                int y = event.getY();
                Point p = new Point(x / size, y / size);
                Move bestMove = null;

                if (pointSelected == null) {

                    if (b.getPiece(p.x, 7 - p.y) != null &&
                            b.getPiece(p.x, 7 - p.y).isBlack != b.isWhiteTurn()) {
                        pointSelected = new Point(p.x, 7 - p.y);

                    }
                } else {
                    if (b.getPiece(pointSelected).getName().equals("P")) {
                        int rank;
                        Piece piece = b.getPiece(pointSelected);

                        if (piece.isBlack) {
                            rank =  8 - piece.l.y;
                        } else {
                            rank =  piece.l.y + 1;
                        }
                        if (rank == 7) {
                            boolean moved = b.movePiece(pointSelected, new Point(p.x, 7 - p.y));
                            if (moved) {
                                String option = JOptionPane.showInputDialog("enter 1 to promote to queen, 2 to rook, 3 to bishop, and 4 to knight");
                                int op = Integer.parseInt(option);
                                if (op == 1) {
                                    b.getBoard()[piece.l.x][piece.l.y] =
                                            new Queen(piece.l.x, piece.l.y, b, piece.isBlack);

                                    b.black.remove(piece);
                                    b.black.add(b.getPiece(piece.l.x, piece.l.y));
                                } else if (op == 2) {
                                    b.getBoard()[piece.l.x][piece.l.y] =
                                            new Rook(piece.l.x, piece.l.y, b, piece.isBlack, false);

                                    b.black.remove(piece);
                                    b.black.add(b.getPiece(piece.l.x, piece.l.y));

                                } else if (op == 3) {
                                    b.getBoard()[piece.l.x][piece.l.y] =
                                            new Bishop(piece.l.x, piece.l.y, b, piece.isBlack);

                                    b.black.remove(piece);
                                    b.black.add(b.getPiece(piece.l.x, piece.l.y));
                                } else if (op == 4) {
                                    b.getBoard()[piece.l.x][piece.l.y] =
                                            new Knight(piece.l.x, piece.l.y, b, piece.isBlack);

                                    b.black.remove(piece);
                                    b.black.add(b.getPiece(piece.l.x, piece.l.y));
                                }


                                bestMove = AiFunctions.bestMove(b);

                                bestMove.move(b);

                            }
                            pointSelected = null;
                        }
                    }
                    boolean moved = b.movePiece(pointSelected, new Point(p.x, 7 - p.y));
                    repaint(100);
                    if (moved) {

                        bestMove = AiFunctions.bestMove(b);

                        bestMove.move(b);

                    }
                    pointSelected = null;

                }
                repaint();
                if (bestMove != null) {
                    bestMove.move(b);

                }
                int result = b.checkMateChecker();

                if (result != 0) {
                    playing = false;
                    repaint();


                }
            }
        }
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }
}
