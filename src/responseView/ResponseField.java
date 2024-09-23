package responseView;

import model.BlockComponent;

import javax.swing.*;

public class ResponseField extends JComponent {
    public final ResponseFrame RFrame;

    public BlockComponent[][] Maze;

    public ResponseField(ResponseFrame RFrame, int width, int height, BlockComponent[][] Maze) {
        setSize(width, height);
        setLayout(null);
        this.RFrame = RFrame;
        this.Maze = Maze;

        createField();

    }

    private void createField() {
        for (BlockComponent[] b_ : Maze) {
            for (BlockComponent b : b_) {
                add(b);
                b.repaint();
            }
        }
    }


}
