package controller;

import model.BlockComponent;
import view.Field;


public class MouseController {
    public final Field gameField;
    public final FindPath findPath;
    public BlockComponent[][] Maze;

    public boolean Mouse_Pressed_L;
    public boolean Mouse_Pressed_R;


    public MouseController(Field gameField) {
        this.gameField = gameField;
        this.findPath = new FindPath();
    }


    public void onTouch(BlockComponent block) {
        block.repaint();

    }
    public void offTouch(BlockComponent block) {
        block.repaint();
    }


    public void onClick_L(BlockComponent block) {
        Mouse_Pressed_L = true;
        if (!gameField.OPTIONS[1])
            return;
        if (gameField.CTRL) {
            if (block.getSTATE() == 1)
                Maze[block.getFieldPoint().getX()][block.getFieldPoint().getY()].setSTATE(0);
            return;
        }

        if (block.getSTATE() == 0) {
            cleanTempInAll(true, false);
            block.repaint();
            Maze[0][0].setSTATE(0);
            Maze[block.getFieldPoint().getX()][block.getFieldPoint().getY()].setSTATE(2);
            findPath();
            return;
        }
        if (block.getSTATE() == 2)
            findPath();
    }

    public void onClick_R(BlockComponent block) {
        Mouse_Pressed_R = true;
        if (!gameField.OPTIONS[1])
            return;
        if (gameField.CTRL) {
            if (block.getSTATE() == 0)
                Maze[block.getFieldPoint().getX()][block.getFieldPoint().getY()].setSTATE(1);
            return;
        }

        if (block.getSTATE() == 0) {
            cleanTempInAll(false, true);
            block.repaint();
            Maze[Maze.length-1][Maze[0].length-1].setSTATE(0);
            Maze[block.getFieldPoint().getX()][block.getFieldPoint().getY()].setSTATE(3);
            findPath();
            return;
        }
        if (block.getSTATE() == 3)
            findPath();
    }

    /**
     * Clean all temp start and end point (Developer Mode)
     */
    private void cleanTempInAll(boolean cleanStart, boolean cleanEnd) {
        for (BlockComponent[] b_ : Maze)
            for (BlockComponent b : b_) {
                b.passed = false;
                if (b.getSTATE() == 4)
                    b.setSTATE(0);
                if (cleanStart && b.getSTATE() == 2) {
                    Maze[b.getFieldPoint().getX()][b.getFieldPoint().getY()].setSTATE(0);
                    Maze[0][0].setSTATE(2);
                } else if (cleanEnd && b.getSTATE() == 3) {
                    Maze[b.getFieldPoint().getX()][b.getFieldPoint().getY()].setSTATE(0);
                    Maze[Maze.length - 1][Maze[0].length - 1].setSTATE(3);
                }
//                findPath();
            }
    }


    public void findPath() {
        findPath(true);
    }
    public void findPath(boolean doPaint) {
        findPath.doPaint = doPaint;
        findPath.Gradual = gameField.OPTIONS[0];
        findPath.DLM = gameField.OPTIONS[1];
        findPath.Algo = gameField.ALGO;
        gameField.SelfNext_Points = findPath.solve(Maze, gameField.e);
//        StdOut.println("Self & Next:" + Arrays.toString(gameField.SelfNext_Points));
    }



}
