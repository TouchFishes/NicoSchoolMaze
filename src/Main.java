import controller.ARAStarMethod;
import view.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (args != null && args.length > 0 && args[0].equals("terminal")) {
            ARAStarMethod solve = new ARAStarMethod();
            solve.initial();
            solve.ARA(solve.e);
            return;
        }
        SwingUtilities.invokeLater(() -> {
            GameFrame mainFrame = new GameFrame("Nico Nico School Maze");
            mainFrame.setVisible(true);
            mainFrame.requestFocus();
        });
    }
}
