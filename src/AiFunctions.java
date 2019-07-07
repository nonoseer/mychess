import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class AiFunctions {
    private static int[][] pawnMatrix = new int[][] {
            {0, 0, 3, 10, 10, 3, 15, 0},
            {0, 0, 3, 10, 10, 3, 15, 0},
            {0, 0, 5, 14, 14, 5, 15, 0},
            {0, -5, 13, 18, 18, 7, 18, 0},
            {0, -5, 13, 18, 18, 7, 18, 0},
            {0, 0, 3, 5, 5, 3, 15, 0},
            {0, 0, 1, 2, 3, 3, 10, 0},
            {0, 0, 3, 8, 8, 3, 13, 0},


    };
    private static int[][] bishopMatrix = new int[][] {
            {-10, -5, -5, -5, -5, -5, -5, -10},
            {-10, 0, 0, 5, 5, 0, 0, -10},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 5, 0, 0, 5, 0, -5},
            {-5, 0, 5, 10, 10, 5, 0, -5},
            {-5, 0, 0, 10, 10, 0, 0, -5},
            {-10, 0, 0, 5, 5, 0, 0, -10},
            {-10, -5, -5, -5, -5, -5, -5, -10},
    };
    private static int[][] rookMatrix = new int[][] {

            {-5, -5, -5, -5, -5, -5, -5, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 3},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, -5, -5, -5, -5, -5, -5, -5},
    };
    private static int[][] knightMatrix = new int[][] {

            {-25, -8, -8, -8, -8, -8, -8, -25},
            {-10, -8, 0, 5, 5, 0, -8, -10},
            {-5, 0, 0, 20, 20, 0, 0, -5},
            {-5, 0, 5, 20, 20, 5, 0, -5},
            {-5, 0, 0, 20, 20, 0, 0, -5},
            {-5, 0, 0, 20, 20, 0, 0, -5},
            {-10, -8, 0, 5, 5, 0, -8, -10},
            {-25, -8, -8, -8, -8, -8, -8, -25},
    };
    static int[][] queenMatrix = new int[][] {

            {-10, -5, 0, 0, 0, -5, -5, -10},
            {-10, 0, 0, 5, 5, 0, 0, -10},
            {-5, 0, 0, 10, 10, 0, 0, -5},
            {-5, 0, 5, 10, 10, 5, 0, -5},
            {-5, 0, 5, 10, 10, 5, 0, -5},
            {-5, 0, 0, 10, 10, 0, 0, -5},
            {-10, 0, 0, 5, 5, 0, 0, -10},
            {-10, -5, 0, 0, 0, -5, -5, -10},
    };
    private static int[][] kingMatrix = new int[][] {
            {8, 15, 17, 0, 0, 12, 20, 12},
            {8, 10, 12, 0, 0, 12, 12, 10},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };
    private static Map<String, int[][]> getMatrix;
    static {
        getMatrix = new TreeMap<>();
        getMatrix.put("P", pawnMatrix);
        getMatrix.put("Kn", knightMatrix);
        getMatrix.put("K", kingMatrix);
        getMatrix.put("Q", queenMatrix);
        getMatrix.put("R", rookMatrix);
        getMatrix.put("B", bishopMatrix);
    }


    public static int evalBoardHelper(Board board, int depth, int alpha, int beta) {




        if (depth == 0 || board.checkMateChecker() != 0) {
            int score = 0;
            if (board.isWhiteTurn()) {
                if (board.checkMateChecker() == 1) {
                    return Integer.MIN_VALUE;
                } else if (board.checkMateChecker() == 2) {
                    return Integer.MAX_VALUE;
                }
            }
            for (Piece i : board.black) {
                if (board.isWhiteTurn()) {

                    score += getMatrix.get(i.getName())[i.l.x][i.l.y];
                } else {
                    score += getMatrix.get(i.getName())[7 - i.l.x][7 - i.l.y];
                }

                if (i.getName().equals("P")) {
                    score += 100;
                } else if (i.getName().equals("Kn") || i.getName().equals("B")) {
                    score += 300;
                } else if (i.getName().equals("R")) {
                    score += 500;
                } else if (i.getName().equals("Q")) {
                    score += 900;
                } else {
                    score += 2000;
                }

            }
            for (Piece i : board.white) {

                if (i.getName().equals("P")) {
                    score -= 100;
                } else if (i.getName().equals("Kn") || i.getName().equals("B")) {
                    score -= 300;
                } else if (i.getName().equals("R")) {
                    score -= 500;
                } else if (i.getName().equals("Q")) {
                    score -= 900;
                } else {
                    score -= 2000;
                }
            }

            return score;



        } else {
            if (board.isWhiteTurn()) {
                search:
                for (Piece i : board.white) {
                    ArrayList<Point> possiblePoints = i.getValidLocations();

                    for (Point j : possiblePoints) {
                        Board newBoard = new Board(board.getBoard(),
                                board.isWhiteTurn(), board.getPrevBoard());
                        newBoard.movePiece(i.l, j);
                        int score = evalBoardHelper(newBoard, depth - 1, alpha, beta);
                        if (score < beta) {
                            beta = score;
                        }
                        if (alpha >= beta) {
                            break search;
                        }
                    }
                }
                return beta;
            } else {

                search2:
                for (Piece i : board.black) {
                    ArrayList<Point> possiblePoints = i.getValidLocations();

                    for (Point j : possiblePoints) {
                        Board newBoard = new Board(board.getBoard(),
                                board.isWhiteTurn(), board.getPrevBoard());
                        newBoard.movePiece(i.l, j);

                        int score = evalBoardHelper(newBoard, depth - 1, alpha, beta);
                        if (score > alpha) {
                            alpha = score;
                        }
                        if (alpha >= beta) {
                            break search2;
                        }
                    }
                }
                return alpha;
            }
        }
    }

    public static Move bestMove(Board board) {
        Move bestMove = null;
        int bestScore;
        if (board.isWhiteTurn()) {
            bestScore = Integer.MAX_VALUE;
        } else {
            bestScore = Integer.MIN_VALUE;
        }
        int depth = 2;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        if (board.isWhiteTurn()) {
            for (Piece i : board.white) {
                ArrayList<Point> possiblePoints = i.getValidLocations();

                for (Point j : possiblePoints) {
                    Board newBoard = new Board(board.getBoard(),
                            board.isWhiteTurn(), board.getPrevBoard());

                    newBoard.movePiece(i.l, j);
                    int newScore = evalBoardHelper(newBoard, depth - 1, alpha, beta);

                    if (newScore < bestScore) {
                        bestScore = newScore;

                        bestMove = (Board b) -> b.movePiece(i.l, j);
                    }
                }
            }
        } else {
            for (Piece i : board.black) {
                ArrayList<Point> possiblePoints = i.getValidLocations();

                for (Point j : possiblePoints) {
                    Board newBoard = new Board(board.getBoard(),
                            board.isWhiteTurn(), board.getPrevBoard());

                    newBoard.movePiece(i.l, j);
                    int newScore = evalBoardHelper(newBoard, depth - 1, alpha, beta);
                    if (newScore > bestScore) {
                        bestScore = newScore;

                        bestMove = (Board b) -> b.movePiece(i.l, j);
                    }
                }
            }
        }

        return bestMove;
    }
}
