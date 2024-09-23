package responseView;

import controller.MouseController;
import model.BlockComponent;

import javax.swing.*;
import java.awt.*;

/**
 * To show a new Maze in a now frame on the Inquiry Time
 */
public class ResponseFrame extends JFrame {
    private ResponseField exhibitionField;
    private final MouseController mouseController;

    public int WIDTH;
    public int HEIGHT;
    public int BLOCKSIZE;
    private BlockComponent[][] Maze;

    public ResponseFrame(String FrameName, int width, int height, int blocksize, MouseController mouseController, BlockComponent[][] Maze) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.BLOCKSIZE = blocksize;
        this.mouseController = mouseController;

        cloneMaze(Maze);

        setTitle(FrameName);
        setSize(WIDTH, HEIGHT);

        getContentPane().setBackground(new Color(90, 90, 90));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(null);

        addField();
    }



    private void addField() {
        exhibitionField = new ResponseField(this, WIDTH, HEIGHT, Maze);
        exhibitionField.setLocation(16, 16);
        add(exhibitionField);
    }



    private void cloneMaze(BlockComponent[][] maze) {
        Maze = new BlockComponent[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++)
            for (int j = 0; j < maze[0].length; j++) {
                BlockComponent b = maze[i][j];
                BlockComponent q = new BlockComponent(b.getFieldPoint(), new Point(j * BLOCKSIZE, i * BLOCKSIZE), mouseController, b.getSTATE(), BLOCKSIZE, Maze);
                q.transformSD(b);
                q.mouseValid = false;
                Maze[i][j] = q;
            }
    }





}
