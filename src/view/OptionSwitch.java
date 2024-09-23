package view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Option Switch Interface
 */
public class OptionSwitch extends JDialog {
    JPanel jp = new JPanel();

    public boolean Gradual;
    public boolean DeveloperM;
    public boolean InquiryWindow;

    public OptionSwitch(JFrame JF, boolean[] options) {
        super(JF,"Dialog",true);
        setTitle("Option");
        setResizable(false);
        setSize(100, 200);
        setLocationRelativeTo(null); // Center the window.
//        setLayout(null);

        this.Gradual = options[0];
        this.DeveloperM = options[1];
        this.InquiryWindow = options[2];

        JCheckBox CHKBox1 = new JCheckBox(" Gradual ", Gradual);
        JCheckBox CHKBox2 = new JCheckBox("Developer", DeveloperM);
        JCheckBox CHKBox3 = new JCheckBox("INQ Window", InquiryWindow);

        ActionListener actionListener = e -> {
            Gradual = CHKBox1.isSelected();
            DeveloperM = CHKBox2.isSelected();
            InquiryWindow = CHKBox3.isSelected();
        };
        CHKBox1.addActionListener(actionListener);
        CHKBox2.addActionListener(actionListener);
        CHKBox3.addActionListener(actionListener);
        CHKBox1.setFocusPainted(false);
        CHKBox2.setFocusPainted(false);
        CHKBox3.setFocusPainted(false);

        jp.add(CHKBox1);
        jp.add(CHKBox2);
        jp.add(CHKBox3);

        add(jp);
        setVisible(true);
    }


    public boolean[] returnOPS() {
        boolean[] options = new boolean[3];
        options[0] = Gradual;
        options[1] = DeveloperM;
        options[2] = InquiryWindow;
        return options;
    }

}
