import javax.swing.*;
import java.awt.*;

public class Game implements Runnable{
    public void run() {
        final JFrame frame = new JFrame();
        frame.setLocationByPlatform(true);
        frame.setName("Hu's chess game");


        final JLabel status = new JLabel("running.");
        final JPanel status_bar = new JPanel();
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.add(status_bar);
        frame.add(chessBoard, BorderLayout.CENTER);
        status_bar.add(status);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        final JButton resign = new JButton("resign");
        final JButton reset = new JButton("reset");
        reset.addActionListener(e -> chessBoard.reset());
        resign.addActionListener(e -> chessBoard.resign());
        control_panel.add(reset);
        control_panel.add(resign);

        frame.pack();
        frame.setVisible(true);



    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
