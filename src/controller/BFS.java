package controller;

import model.BlockComponent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class BFS {
    private FindPath findPath;
    private BlockComponent[][] Maze;
    private int[][] maze;
    public boolean Gradual;

    public BFS(BlockComponent[][] Maze, int[][] maze, boolean gradual, FindPath findPath) {
        this.Maze = Maze;
        this.maze = maze;
        this.Gradual = gradual;
        this.findPath =findPath;
    }

    public LinkedList<FindPath.Point> solve() {
        int sx = -1;
        int sy = -1;
        for (int x = 0; x < maze.length; x++)
            for (int y = 0; y < maze[x].length; y++)
                if (maze[x][y] == 2) {
                    sx = x;
                    sy = y;
                    break;
                }
        if (sx == -1) {
            return null;
        }

        Queue<FindPath.Point> queue = new LinkedList<>();
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        LinkedList<FindPath.Point> result = new LinkedList<>();

        queue.add(new FindPath.Point(sx, sy, null));
        visited[sx][sy] = true;

        int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};

        if (Gradual) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!queue.isEmpty()) {
                        FindPath.Point current = queue.poll();
                        Maze[current.x][current.y].nowVisiting = false;
                        Maze[current.x][current.y].hasBeenVisited = true;

                        for (int i = 0; i < 8; i++) {
                            int x2 = current.x + dx[i];
                            int y2 = current.y + dy[i];

                            if (x2 < 0 || x2 >= maze.length || y2 < 0 || y2 >= maze[0].length || visited[x2][y2] || maze[x2][y2] == 1)
                                continue;
                            if (maze[x2][y2] == 3) {
                                result.add(new FindPath.Point(x2, y2, current));
                                Maze[x2][y2].isPath = true;
                                for (FindPath.Point p = current; p != null; p = p.previous) {
                                    result.addFirst(p);
                                    Maze[p.x][p.y].isPath = true;
                                }
                                findPath.refreshMaze(true);
                                timer.cancel();
                                return;
                            }
                            queue.add(new FindPath.Point(x2, y2, current));
                            visited[x2][y2] = true;
                            Maze[x2][y2].nowVisiting = true;
                        }
                        findPath.refreshMaze(true);
//                        StdOut.println(current);
                    } else {
                        findPath.refreshMaze(false);
                        timer.cancel();
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 20);
            return result;
        } else {
            while (!queue.isEmpty()) {
                FindPath.Point current = queue.poll();
                Maze[current.x][current.y].nowVisiting = false;
                Maze[current.x][current.y].hasBeenVisited = true;

                for (int i = 0; i < 8; i++) {
                    int x2 = current.x + dx[i];
                    int y2 = current.y + dy[i];

                    if (x2 < 0 || x2 >= maze.length || y2 < 0 || y2 >= maze[0].length || visited[x2][y2] || maze[x2][y2] == 1)
                        continue;
                    if (maze[x2][y2] == 3) {
                        result.add(new FindPath.Point(x2, y2, current));
                        Maze[x2][y2].isPath = true;
                        for (FindPath.Point p = current; p != null; p = p.previous) {
                            result.addFirst(p);
                            Maze[p.x][p.y].isPath = true;
                        }
                        findPath.refreshMaze(true);
                        return result;
                    }
                    queue.add(new FindPath.Point(x2, y2, current));
                    visited[x2][y2] = true;
                    Maze[x2][y2].nowVisiting = true;
                }
                findPath.refreshMaze(true);
//                StdOut.println(current);
            }
            findPath.refreshMaze(false);
            return null;
        }
    }
}
