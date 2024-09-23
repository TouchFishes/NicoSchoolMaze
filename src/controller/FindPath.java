package controller;

import edu.princeton.cs.algs4.StdOut;
import model.BlockComponent;
import view.FieldPoint;

import java.util.LinkedList;

public class FindPath {
    private BlockComponent[][] Maze;
    private int[][] maze;
    public int[] nme;
    public int[][] magicTable;
    public int[] inquiryList;
    public boolean showPath;
    public boolean Gradual = false;
    public boolean DLM = false;
    public int Algo;
    LinkedList<FindPath.Point> open;
    LinkedList<FindPath.Point> close;

    public boolean doPaint = false;

    public FindPath() {}

    private void cleanPath() {
        for (BlockComponent[] b_ : Maze)
            for (BlockComponent b : b_) {
                b.justPath = b.isPath;
                b.isPath = false;
                b.nowVisiting = false;
                b.hasBeenVisited = false;
                b.pathFindingFail = false;
            }
    }

    void refreshMaze(boolean pathExisted) {
        if (!doPaint) return;

        for (BlockComponent[] b_ : Maze)
            for (BlockComponent b : b_) {
                b.pathFindingFail = !pathExisted;
                if (Gradual || DLM || b.isPath || b.justPath || b.passed)
                    b.repaint();
            }
    }


    /**
     * Update Maze, and find path
     */
    public FieldPoint[] solve(BlockComponent[][] Maze, int e) {
        this.Maze = Maze;
        this.maze = new int[Maze.length][Maze[0].length];
        for (int i = 0; i < Maze.length; i++)
            for (int j = 0; j < Maze[0].length; j++)
                maze[i][j] = Maze[i][j].getSTATE();

        cleanPath();

        LinkedList<Point> path = null;
        switch (Algo) {
            case 2:
                BFS bfs = new BFS(Maze, maze, Gradual, this);
                path = bfs.solve();
                break;
            case 1:
                ARAStar araStar = new ARAStar(Maze, maze, Gradual, this,open,close);
                path = araStar.ARA(e);
                break;
        }

        tryAnswerInquiry(path);

        if (path != null) {
            Point SelfPoint = path.poll();
            Point NextPoint = path.poll();
            if (SelfPoint != null && NextPoint != null) {
                FieldPoint[] SelfNext_Points = new FieldPoint[2];
                SelfNext_Points[0] = new FieldPoint(SelfPoint.x, SelfPoint.y);
                SelfNext_Points[1] = new FieldPoint(NextPoint.x, NextPoint.y);
                return SelfNext_Points;
            }
        }
        return null;
    }


    private void tryAnswerInquiry(LinkedList<Point> path) {
        if (showPath && path != null) {
            StdOut.println(path.size());
            for (Point p : path) {
                StdOut.print(p.x + " ");
                StdOut.print(p.y + " ");
            }
            StdOut.println();
        }
    }


    static class Point implements Comparable<Point> {
        public final int x;
        public final int y;
        public Point previous;
        public int cost;

        boolean reachable;

        Point(int x, int y, Point previous) {
            this.x = x;
            this.y = y;
            this.previous = previous;
            if (previous == null) cost = 0;
            else this.cost = previous.cost + 1;
        }

        public void setPrevious(Point previous) {
            this.previous = previous;
            this.cost = previous.cost + 1;
        }

        public String toString() {
            return String.format("(%s,%s)", x, y);
        }
        public int compareTo(Point p) {
            return Integer.compare(this.cost,p.cost);
        }




    }


}
