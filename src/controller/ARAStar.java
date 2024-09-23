package controller;


import model.BlockComponent;

import java.util.LinkedList;


public class ARAStar {
    private FindPath findPath;
    private BlockComponent[][] Maze;
    private int[][] maze;

    public int[][] magicTable;
    public int[] inquiryList;
    public boolean Gradual;

    FindPath.Point start;
    FindPath.Point target;
    FindPath.Point[][] map;
    LinkedList<FindPath.Point> open;
    LinkedList<FindPath.Point> close;
    LinkedList<FindPath.Point> inCon;

    int e;


    public ARAStar(BlockComponent[][] Maze, int[][] maze, boolean gradual, FindPath findPath, LinkedList<FindPath.Point> open, LinkedList<FindPath.Point> close) {
        this.Maze = Maze;
        this.maze = maze;
        this.Gradual = gradual;
        this.findPath = findPath;

        this.magicTable = findPath.magicTable;
        this.inquiryList = findPath.inquiryList;
        if(open!= null){
            this.open = open;
        }
        if(close != null){
            this.close = close;
        }
    }


    private int ManDis(FindPath.Point now) {
        int x = Math.abs(now.x - target.x);
        int y = Math.abs(now.y - target.y);
        return Math.max(x, y);
    }


    private int fFunction(FindPath.Point now) {
        return now.cost + e * ManDis(now);
    }

    private static boolean judgeIn(int x, int y, LinkedList<FindPath.Point> list) {
        for (FindPath.Point point : list) {
            if (point.x == x && point.y == y) return true;
        }
        return false;
    }


    private FindPath.Point findMin(LinkedList<FindPath.Point> list) {
        if (list.size() == 0||list.get(0)==null) return null;
        double test = fFunction(list.get(0));
        FindPath.Point result = list.get(0);
        for (FindPath.Point point : list) {
            if (fFunction(point) < test) {
                test = fFunction(point);
                result = point;
            }
        }
        return result;
    }



    private LinkedList<FindPath.Point> AStar(int e) {
        open.add(start);
        this.e = e;
        int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};

        while (open != null && open.size()!=0) {
            FindPath.Point test = findMin(open);
            if(test==null)
                return null;
            open.remove(test);
            Maze[test.x][test.y].hasBeenVisited=true;
            Maze[test.x][test.y].open_set=false;
            close.add(test);
            for (int i = 0; i < 8; i++) {

                int x2 = test.x + dx[i];
                int y2 = test.y + dy[i];

                if (x2 < 0 || x2 >= map.length || y2 < 0 || y2 >= map[0].length || judgeIn(x2, y2, close) || !map[x2][y2].reachable) {
                    continue;
                }


                if (map[x2][y2] == target) {
                    target.setPrevious(test);
                    LinkedList<FindPath.Point> result = new LinkedList<>();
                    Maze[x2][y2].isPath = true;
                    for (FindPath.Point p = target; p != null; p = p.previous) {
                        result.addFirst(p);
                        Maze[p.x][p.y].isPath = true;
                    }
                    findPath.refreshMaze(true);
                    return result;


                }
                if (!judgeIn(x2, y2, open)) {
                    map[x2][y2].setPrevious(test);
                    open.add(map[x2][y2]);
                    Maze[x2][y2].hasBeenVisited = true;

                } else  {
                    if (!judgeIn(x2, y2, inCon)) inCon.add(map[x2][y2]);
                    if (map[x2][y2].cost > test.cost + 1) map[x2][y2].setPrevious(test);
                }
            }

        }
        if(open==null||open.size()==0){
            findPath.refreshMaze(false);
        }
        return null;
    }

    private void initial0 () {
        map = new FindPath.Point[maze.length][maze[0].length];


        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                map[i][j] = new FindPath.Point(i, j, null);
                map[i][j].cost = Integer.MAX_VALUE;
                map[i][j].reachable = true;

                if (maze[i][j] == 1) {
                    map[i][j].reachable = false;
                }
                if (maze[i][j] == 2) {
                    start = map[i][j];
                }
                if (maze[i][j] == 3) {
                    target = map[i][j];
                }

            }
        }
        open = new LinkedList<>();
        close = new LinkedList<>();
        inCon = new LinkedList<>();
    }

    private void SetAsStart (FindPath.Point newStart){
        newStart.previous.previous = newStart;
        newStart.previous.previous.cost = 1;
        newStart.cost = 0;
        newStart.previous = null;
        start = newStart;
    }

    public LinkedList<FindPath.Point> ARA ( int initialE){
        initial0();
        LinkedList<FindPath.Point> result = AStar(initialE);
        if(start == null) return null;

        if(initialE > 1 || !start.equals(target)) {
            if (result!= null&&result.size() > 1) {
                FindPath.Point hasPassed = result.get(0);
                Maze[hasPassed.x][hasPassed.y].passed = true;
                SetAsStart(result.get(1));
            }
            for (int i = 0; i < close.size(); i++) {
                int x = close.get(i).x;
                int y = close.get(i).y;
                Maze[x][y].nowVisiting=false;
                Maze[x][y].hasBeenVisited = true;
            }

            for (int i = 0; i < inCon.size(); i++) open.add(inCon.remove());
        }
        return result;
    }


}


