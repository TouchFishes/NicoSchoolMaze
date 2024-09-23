package view;

import controller.MouseController;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import model.BlockComponent;
import responseView.ResponseFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

/**
 * Panel that contains all components
 */
public class Field extends JComponent {
    public final GameFrame GFrame;
    private final int BLOCKSIZE;
    public final MouseController mouseController = new MouseController(this);
    public JLabel ParameterText;
    public boolean[] OPTIONS = new boolean[3];  // Setting Table
    public int ALGO = 1;
    public boolean CTRL = false;    // Ctrl key is pressed
    public boolean WIN = false;

    public FieldPoint[] SelfNext_Points;    // Nico's next position
    public LinkedList<BlockComponent> FinalPath = new LinkedList<>();  // final Path

    public BlockComponent[][] Maze;
    public int n, m, e;     // lineNumber n，columnNumber m，algoNumber e
    public int p;           // times of Magic Attack
    public int[][] magicTable;  // MA TimeTable, 4 paras fer line: MA time t,MA position (i,j),if is executed (0:No, 1:Yes,successful, 2:Yes,fail)
    public int k;               // times of Inquire
    public int[] inquiryList;   //Inquiry TimeTable


    public Field(GameFrame GFrame, int width, int height, int BlockSize, int n, int m, int e) {
        setSize(width, height);
        setLayout(null);
        this.GFrame = GFrame;
        this.n = n;
        this.m = m;
        this.e = e;
        this.BLOCKSIZE = BlockSize;

        Maze = new BlockComponent[n][m];

        parametersPassing();
        createEmptyField();
        initiateField();

        addKeyListener();
        setVisible(true);
        requestFocus();

    }

    private void parametersPassing() {
        mouseController.Maze = Maze;
        mouseController.findPath.nme = new int[]{n, m, e};
        mouseController.findPath.magicTable = magicTable;
        mouseController.findPath.inquiryList = inquiryList;
    }


    void updateParameterText() {
        ParameterText.setText("<html>n = " + n + "<br/>m = " + m + "<br/>e = " + e);
    }

    void showParameters() {
        StringBuilder MagicAttackText = new StringBuilder();
        MagicAttackText.append("<html>[Magic Attack Timetable]<br/>");
        for (int[] table : magicTable) {
            String executed = "";
            if (table[3] == 1)  executed = " Y ";
            if (table[3] == 2)  executed = " N ";
            MagicAttackText.append("e = ").append(table[0]).append(" - ( ").append(table[1]).append(" , ").append(table[2]).append(" )").append(executed).append("<br/>");
        }
        JOptionPane.showMessageDialog(this, MagicAttackText.toString(), "Parameters: Magic Attack", JOptionPane.INFORMATION_MESSAGE);

        StringBuilder InquiryText = new StringBuilder();
        InquiryText.append("<html>[Inquiry Timetable]<br/>");
        for (int t1 : inquiryList)
            InquiryText.append("e = ").append(t1).append("<br/>");
        JOptionPane.showMessageDialog(this, InquiryText.toString(), "Parameters: Inquiry", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Establish Maze on Empty Field
     */
    public void createEmptyField() {
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[0].length; j++) {
                putBlockOnField(new BlockComponent(new FieldPoint(i, j), calculatePoint(i, j), mouseController, 0, BLOCKSIZE, Maze));
            }
        }
    }
    public void putBlockOnField(BlockComponent block) {
        int row = block.getFieldPoint().getX();
        int col = block.getFieldPoint().getY();
        if (Maze[row][col] != null) {
            remove(Maze[row][col]);
        }
        Maze[row][col] = block;
        add(block);
    }
    private Point calculatePoint(int row, int col) {
        return new Point(col * BLOCKSIZE, row * BLOCKSIZE);
    }

    /**
     * Initiate field based on the rest of input
     */
    private void initiateField() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                Maze[i][j].setSTATE(StdIn.readInt());
            }
        Maze[0][0].setSTATE(2);
        Maze[n-1][m-1].setSTATE(3);

        p = StdIn.readInt();
        magicTable = new int[p][4];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < 3; j++)
                magicTable[i][j] = StdIn.readInt();
            magicTable[i][3] = 0;
        }
        k =  StdIn.readInt();
        inquiryList = new int[k];
        for (int i = 0; i < k; i++)
            inquiryList[i] = StdIn.readInt();

//        StdOut.println(toString2(Maze));
    }


    /**
     * do one step
     * @param doPaint whether Algo needs to repaint blocks
     */
    public void MarchOn(boolean FIRST, boolean doPaint) {
        // test mode
        if (OPTIONS[1] || OPTIONS[0]) {
            FindPath(doPaint);
            return;
        }
        // normal mode
        if (!FIRST && e > 1) e--;
        tryMagic();
        tryInquiry();
        FindPath(doPaint);
        NicoMove();
    }



    public void FindPath(boolean doPaint) {
        mouseController.findPath(doPaint);
        updateParameterText();
    }


    /**
     * Nico takes one step
     */
    private void NicoMove() {
        if (SelfNext_Points == null)
            return;

        BlockComponent Self = Maze[SelfNext_Points[0].getX()][SelfNext_Points[0].getY()];
        Self.setSTATE(0);
        Self.passed = true;
        FinalPath.add(Self);

        BlockComponent Next = Maze[SelfNext_Points[1].getX()][SelfNext_Points[1].getY()];
        Next.setSTATE((Next.getSTATE() == 0) ? 2 : 4);

        if (Next.getSTATE() == 4) {
            WIN = true;
            FinalPath.add(Next);
            StdOut.println(FinalPath.size());
            for (BlockComponent b : FinalPath) {
                StdOut.print(b.getFieldPoint().getX() + " ");
                StdOut.print(b.getFieldPoint().getY() + " ");
                b.finalPass = true;
                b.repaint();
            }
            StdOut.println();
            JOptionPane.showMessageDialog(this, "Nico has successfully found the treasure!", "WIN", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * check and try magic attack (if so)
     */
    private void tryMagic() {
        for (int i = 0; i < p; i++)
            if (magicTable[i][0] == e) {
                BlockComponent b = Maze[magicTable[i][1]][magicTable[i][2]];
                magicTable[i][3] = (b.getSTATE() == 0) ? 1 : 2;
                if (b.getSTATE() == 2)
                    b.isAttackedFail = true;
                else if (b.getSTATE() == 0) {
                    b.setSTATE(1);
                    b.isAttackedS = true;
                }
                return;
            }
    }

    /**
     * check and try inquiry (if so)
     */
    private void tryInquiry() {
        mouseController.findPath.showPath = false;
        for (int t : inquiryList)
            if (t == e) {
                mouseController.findPath.showPath = true;
                tryShowMazeOnNewField();
                break;
            }
    }

    private void tryShowMazeOnNewField() {
        if (!OPTIONS[2])
            return;
        String frameName = "e = " + e;
        ResponseFrame RF = new ResponseFrame(frameName, getWidth()-72, getHeight(), BLOCKSIZE, mouseController, Maze);
        RF.setLocation(0, 0);
        RF.setVisible(true);
    }


    /**
     * Rush to the next checkpoint (Inquiry time position)
     */
    public void Rush() {
        if (OPTIONS[1] || OPTIONS[0])
            return;
        while(!WIN) {
            for (int t : inquiryList) {
                if (t == e-1) {
                    MarchOn(false, true);
                    return;
                }
            }
            MarchOn(false, false);
        }
    }


    private void addKeyListener() {
        addKeyListener(new KeyListener() {
            /**
             * @param e response for keyboard event <br>
             */
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    CTRL = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                    CTRL = false;
            }
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });
    }



    private static String toString2(BlockComponent[][] maze) {
        StringBuilder str = new StringBuilder();
        for (BlockComponent[] maze0 : maze) {
            for (BlockComponent maze00 : maze0)
                str.append(maze00).append(" ");
            str.append("\n");
        }
        return str.toString();
    }


}
