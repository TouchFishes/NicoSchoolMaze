package view;

import edu.princeton.cs.algs4.In;

import javax.swing.*;
import java.awt.*;

/**
 * Setting Interface
 */
public class SettingInterface extends JDialog {
    private final int WIDTH;
    private final int HEIGHT;
    private final int ButtonWidth;
    private final int ButtonHeight;
    private final JFrame JF;

    public boolean[] OPTIONS;
    public int ALGO;


    public SettingInterface(JFrame JF, boolean[] OPTIONS, int ALGO) {
        super(JF,"Setting",true);
        this.JF = JF;
        this.WIDTH = 180;
        this.HEIGHT = 240;
        this.ButtonWidth = 120;
        this.ButtonHeight = 36;
        this.OPTIONS = OPTIONS;
        this.ALGO = ALGO;
        setTitle("Setting");
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setLayout(null);

        addACButton();
        addMCSButton();
        addInformationButton();

        setVisible(true);
    }



    private void addACButton() {
        JButton button = new JButton("Algo.");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 2);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("", Font.PLAIN, 16));
        add(button);
        button.addActionListener((e) -> {
            AlgoSelInterface AlgoS = new AlgoSelInterface(JF);
            if (AlgoS.AlgoChoice != -1)
                ALGO = AlgoS.AlgoChoice;
        });
    }

    private void addMCSButton() {
        JButton button = new JButton("Mode");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 10);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("", Font.PLAIN, 16));
        add(button);
        button.addActionListener((e) -> {
            OptionSwitch OPS = new OptionSwitch(JF, OPTIONS);
            OPTIONS = OPS.returnOPS();
        });
    }


    private void addInformationButton() {
        JButton button = new JButton("Hint");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 24);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("", Font.BOLD, 16));
        button.setContentAreaFilled(false);
        add(button);
        button.addActionListener((e) -> {
            try {
                In fin = new In("./resource/text/Hint.txt");
                String message = fin.readAll();
                JOptionPane.showMessageDialog(null, message, "Hint", JOptionPane.INFORMATION_MESSAGE);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Cannot find the Hint file!\nPath: \n    ./resource/text/Hint.txt    ", "File loss", JOptionPane.INFORMATION_MESSAGE);
//                ex.printStackTrace();
            }
        });
    }




}
