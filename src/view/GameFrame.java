package view;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import javax.swing.*;
import java.awt.*;

/**
 * Main Interface
 */
public class GameFrame extends JFrame {
    private Field gameField;
    private JLabel ParameterText;

    public int WIDTH;
    public int HEIGHT;
    public int BlockSize;

    private boolean FirstStep = true;   // First Step (e does not minus 1)


    public GameFrame(String FrameName) {
        int n = StdIn.readInt();
        int m = StdIn.readInt();
        int e = StdIn.readInt();

        setTitle(FrameName);
        calculateSIZE(n, m);
        setSize(WIDTH, HEIGHT);
        getContentPane().setBackground(new Color(90, 90, 90));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        addRNButton();
        addRushButton();
        addSettingButton();
        addParameterButton();
        addParameterText();
        addField(n, m, e);

        gameField.updateParameterText();
    }

    private void calculateSIZE(int n, int m) {
        BlockSize = 48;
        WIDTH = BlockSize * m +48 + m +64;
        HEIGHT = BlockSize * n +64 + n;

        if (m > 16) WIDTH = 762 +72;
        if (n > 16) HEIGHT = 762;
        if (m > 16 || n > 16) {
            BlockSize = Math.min((WIDTH - 72 - 64) / m, (HEIGHT - 64) / n);
            WIDTH = BlockSize * m +48 + m +72;
            HEIGHT = BlockSize * n +64 + n;
        }
    }



    private void addField(int n, int m, int e) {
        gameField = new Field(this, WIDTH, HEIGHT, BlockSize, n, m, e);
        gameField.setLocation(16, 16);
        gameField.ParameterText = this.ParameterText;
        add(gameField);
    }


    private void addRNButton() {
        JButton button = new JButton("A");
        button.setLocation(WIDTH -86, 10);
        button.setSize(54, 54);
        button.setFont(new Font("", Font.BOLD, 20));
        button.setForeground(Color.LIGHT_GRAY);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener((e) -> {
            gameField.MarchOn(FirstStep, true);
            if (FirstStep) {
                StdOut.println();
                FirstStep = false;
                button.setText("M");
            }
            gameField.requestFocus();
        });
        add(button);
    }


    private void addRushButton() {
        JButton Button = new JButton("R");
        Button.setLocation(WIDTH -86, 70);
        Button.setSize(54, 54);
        Button.setFont(new Font("", Font.BOLD, 20));
        Button.setForeground(Color.LIGHT_GRAY);
        Button.setContentAreaFilled(false);
        Button.setFocusPainted(false);
        Button.addActionListener((e) -> {
            if (FirstStep) {
                JOptionPane.showMessageDialog(this, "Please click button \"A\" First!");
            } else
                gameField.Rush();
            gameField.requestFocus();
        });
        add(Button);
    }

    private void addSettingButton() {
        JButton Button = new JButton("S");
        Button.setLocation(WIDTH -86, 130);
        Button.setSize(54, 54);
        Button.setFont(new Font("", Font.BOLD, 20));
        Button.setForeground(Color.LIGHT_GRAY);
        Button.setContentAreaFilled(false);
        Button.setFocusPainted(false);
        Button.addActionListener((e) -> {
            SettingInterface SI = new SettingInterface(this, gameField.OPTIONS, gameField.ALGO);
            gameField.OPTIONS = SI.OPTIONS;
            gameField.ALGO = SI.ALGO;
            gameField.requestFocus();
        });
        add(Button);
    }


    private void addParameterText() {
        ParameterText = new JLabel("...");
        ParameterText.setLocation(WIDTH -80, HEIGHT - 135);
        ParameterText.setSize(80, 80);
        ParameterText.setForeground(Color.LIGHT_GRAY);
        ParameterText.setFont(new Font("", Font.PLAIN, 17));
        add(ParameterText);
    }


    private void addParameterButton() {
        JButton button = new JButton("...");
        button.setLocation(WIDTH -100, HEIGHT - 120);
        button.setSize(20, 60);
        button.setFont(new Font("", Font.BOLD, 10));
        button.setForeground(Color.LIGHT_GRAY);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener((e) -> {
            gameField.showParameters();
            gameField.requestFocus();
        });
        add(button);
    }




}
