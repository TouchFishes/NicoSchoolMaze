package view;

import javax.swing.*;
import java.awt.*;

public class AlgoSelInterface extends JDialog {
    private final JTextField textField = new JTextField(20);
    private final JComboBox<String> comboBox = new JComboBox<>();

    public int AlgoChoice = -1;    // -1 is exiting without choosing, 1 is default(ARA*)

    public AlgoSelInterface(JFrame JF){
        super(JF,"Algorithm Selection",true);
        setTitle("Algorithm Selection");
        comboBox.addItem(" ARA* ");
        comboBox.addItem(" DFS ");

        JButton button = new JButton("Confirm");
        button.addActionListener(e -> {
            String Algo = (String) comboBox.getSelectedItem();

            textField.setText("Now Algo: " + Algo);

            if (Algo != null) {
                switch (Algo) {
                    case " ARA* ":   AlgoChoice = 1; break;
                    case " DFS ":  AlgoChoice = 2; break;
                }
            }

            JOptionPane.showMessageDialog(this, "Set successfully!");
        });
        comboBox.addActionListener(e -> textField.setText("index:" + comboBox.getSelectedIndex() + " " + comboBox.getSelectedItem()));

        comboBox.setEditable(true);
        textField.setEditable(false);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout());
        add(comboBox);
        add(button);
        add(textField);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }


}

