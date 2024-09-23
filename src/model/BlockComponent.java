package model;

import view.FieldPoint;
import controller.MouseController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * Step Block
 */
public class BlockComponent extends JComponent {
    private final MouseController mouseController;
    private final FieldPoint fieldPoint;
    private BlockState blockState;
    private BlockComponent[][] Maze;

    private int STATE;        // STATE: 0-PATH, 1-WALL, 2-Start, 3-End
    public boolean touched;
    public boolean mouseValid;

    public boolean isPath;          // the path
    public boolean justPath;        // the path that only in the last round
    public boolean passed;          // have been passed
    public boolean finalPass;       // the final pass way
    public boolean nowVisiting;     // now visiting
    public boolean hasBeenVisited;  // has been visited
    public boolean pathFindingFail; // path finding fail
    public boolean isAttackedS;     // be attacked
    public boolean isAttackedFail;  // invalid attack
    public boolean open_set;        // be in open_set


    public BlockComponent(FieldPoint fieldPoint, Point location, MouseController mouseController, int State, int size, BlockComponent[][] Maze) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);

        this.mouseController = mouseController;
        this.fieldPoint = fieldPoint;
        this.STATE = State;
        this.Maze = Maze;
        this.mouseValid = true;
        this.justPath = false;
        this.touched = false;
        this.passed = false;
        this.finalPass = false;
        this.isAttackedS = false;
        this.isAttackedFail = false;
        this.open_set = false;

        refreshBlockState();
    }

    private void refreshBlockState() {
        switch (STATE) {
            case 0: blockState = BlockState.ACCESS;break;
            case 1: blockState = BlockState.WALL;break;
            case 2: blockState = BlockState.START;break;
            case 3: blockState = BlockState.END;break;
            case 4: blockState = BlockState.WIN;break;
        }
    }

    public void setSTATE(int state) {
        STATE = state;
        refreshBlockState();
        repaint();
    }
    public int getSTATE() {
        return STATE;
    }

    public FieldPoint getFieldPoint() {
        return fieldPoint;
    }


    /**
     * @param e response for mouse event <br>
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (!mouseValid)
            return;

        if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1)   // BUTTON1 左键
            mouseController.onClick_L(this);
        if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3)   // BUTTON3 右键
            mouseController.onClick_R(this);
        if (e.getID() == MouseEvent.MOUSE_RELEASED && e.getButton() == MouseEvent.BUTTON1)
            mouseController.Mouse_Pressed_L = false;
        if (e.getID() == MouseEvent.MOUSE_RELEASED && e.getButton() == MouseEvent.BUTTON3)
            mouseController.Mouse_Pressed_R = false;

        if (e.getID() == MouseEvent.MOUSE_ENTERED) {
            if (mouseController.Mouse_Pressed_L)
                mouseController.onClick_L(this);
            else if (mouseController.Mouse_Pressed_R)
                mouseController.onClick_R(this);
            else {
                touched = true;
                mouseController.onTouch(this);
            }
        } else if ((e.getID() == MouseEvent.MOUSE_EXITED)) {
            touched = false;
            mouseController.offTouch(this);
        }
    }

    public void transformSD(BlockComponent b) {
        isPath = b.isPath;
        justPath = b.justPath;
        passed = b.passed;
        finalPass = b.finalPass;
        nowVisiting = b.nowVisiting;
        hasBeenVisited = b.hasBeenVisited;
        pathFindingFail = b.pathFindingFail;
        isAttackedS = b.isAttackedS;
        isAttackedFail = b.isAttackedFail;
        open_set = b.open_set;
    }




    /**
     * repaint this block
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.setColor(blockState.getColor());
        boolean gradual = mouseController.gameField.OPTIONS[0];
        if (gradual && nowVisiting && STATE == 0)
            g.setColor(new Color(192, 249, 255));
        if (gradual && hasBeenVisited && STATE == 0)
            g.setColor(new Color(224, 255, 249));
        if (passed && STATE == 0)
            g.setColor(new Color(198, 255, 204));
        if (finalPass && STATE == 0) {
            g.setColor(new Color(122, 225, 131));
        }
        if (mouseController.findPath.showPath && isPath) {
            if (STATE == 0)
                g.setColor(new Color(121, 146, 255));
            else if (STATE == 2)
                g.setColor(new Color(37, 69, 255));
        }
        if (pathFindingFail) {
            if (STATE == 2 || STATE == 3)
                g.setColor(new Color(252, 83, 105));
            if (gradual && hasBeenVisited && STATE == 0)
                g.setColor(new Color(255, 176, 181));
        }
        if (open_set && STATE == 0)
            g.setColor(new Color(165, 153, 255));


        if (touched) {
            if (blockState == BlockState.WALL)
                g.setColor(new Color(30, 30, 30, 255));
            else if (blockState == BlockState.ACCESS) {
                g.setColor(new Color(210, 210, 210));
            }
        }
        g.fillRect(0, 0, this.getWidth(), this.getHeight());


        if (isAttackedS) {
            g.setColor(new Color(255, 61, 91, 254));
            g.fillRect(this.getWidth()/3, this.getHeight()/3, this.getWidth()/3, this.getHeight()/3);
        }
        if (isAttackedFail) {
            g.setColor(new Color(255, 163, 196, 254));
            g.fillRect(this.getWidth()/3, this.getHeight()/3, this.getWidth()/3, this.getHeight()/3);
        }



        if (isPath) {
            g.setColor(new Color(1, 164, 192));
            if (mouseController.findPath.showPath)
                g.setColor(new Color(132, 255, 189));
            drawPathLine(g);
        }


    }

    private void drawPathLine(Graphics g) {
//        if (fieldPoint.getX() > 0 && Maze[fieldPoint.getX()-1][fieldPoint.getY()].isPath)
//            g.fillRect(this.getWidth()/2-1, 0, 3, this.getHeight()/2);
//        if (fieldPoint.getX() < Maze.length-1 && Maze[fieldPoint.getX()+1][fieldPoint.getY()].isPath)
//            g.fillRect(this.getWidth()/2-1, this.getHeight()/2, 3, this.getHeight()/2);
//        if (fieldPoint.getY() > 0 && Maze[fieldPoint.getX()][fieldPoint.getY()-1].isPath)
//            g.fillRect(0, this.getHeight()/2-1, this.getWidth()/2, 3);
//        if (fieldPoint.getY() < Maze[0].length-1 && Maze[fieldPoint.getX()][fieldPoint.getY()+1].isPath)
//            g.fillRect(this.getWidth()/2, this.getHeight()/2-1, this.getWidth()/2, 3);

        if (fieldPoint.getX() > 0 && Maze[fieldPoint.getX()-1][fieldPoint.getY()].isPath)
            g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight()/2);
        if (fieldPoint.getX() < Maze.length-1 && Maze[fieldPoint.getX()+1][fieldPoint.getY()].isPath)
            g.drawLine(this.getWidth()/2, this.getHeight(), this.getWidth()/2, this.getHeight()/2);
        if (fieldPoint.getY() > 0 && Maze[fieldPoint.getX()][fieldPoint.getY()-1].isPath)
            g.drawLine(0, this.getHeight()/2, this.getWidth()/2, this.getHeight()/2);
        if (fieldPoint.getY() < Maze[0].length-1 && Maze[fieldPoint.getX()][fieldPoint.getY()+1].isPath)
            g.drawLine(this.getWidth(), this.getHeight()/2, this.getWidth()/2, this.getHeight()/2);

        if (fieldPoint.getX() > 0 && fieldPoint.getY() > 0 && Maze[fieldPoint.getX()-1][fieldPoint.getY()-1].isPath)
            g.drawLine(0, 0, this.getWidth()/2, this.getHeight()/2);
        if (fieldPoint.getX() < Maze.length-1 && fieldPoint.getY() > 0 && Maze[fieldPoint.getX()+1][fieldPoint.getY()-1].isPath)
            g.drawLine(0, this.getHeight(), this.getWidth()/2, this.getHeight()/2);
        if (fieldPoint.getX() > 0 && fieldPoint.getY() < Maze[0].length-1 && Maze[fieldPoint.getX()-1][fieldPoint.getY()+1].isPath)
            g.drawLine(this.getWidth(), 0, this.getWidth()/2, this.getHeight()/2);
        if (fieldPoint.getX() < Maze.length-1 && fieldPoint.getY() < Maze[0].length-1 && Maze[fieldPoint.getX()+1][fieldPoint.getY()+1].isPath)
            g.drawLine(this.getWidth(), this.getHeight(), this.getWidth()/2, this.getHeight()/2);

    }




    @Override
    public String toString() {
//        return STATE + "(" + getFieldPoint().getX() + "," + getFieldPoint().getY() + ")";
//        return STATE + "(" + getX() + "," + getY() + ")";
        return STATE + "";
    }




}
