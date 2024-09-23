package controller;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;


public class ARAStarMethod {
    FindPath.Point start;
    FindPath.Point target;
    public int e;
    FindPath.Point[][] map;
    LinkedList<FindPath.Point> open;
    LinkedList<FindPath.Point> close;
    LinkedList<FindPath.Point> inCon;
    int[][] MagicData;
    int[] consultData;
    LinkedList<FindPath.Point> RealWay;
    LinkedList<FindPath.Point> lastAnswer;



    private int ManDis(FindPath.Point now) {
        int x = Math.abs(now.x - target.x);
        int y = Math.abs(now.y - target.y);
        return Math.max(x, y);
    }

    private FindPath.Point findMin(LinkedList<FindPath.Point> list) {
//        if (list.size() == 0) return null;
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

    private int fFunction(FindPath.Point now) {
        return now.cost + e * ManDis(now);
    }

    private static boolean judgeIn(int x, int y, LinkedList<FindPath.Point> list) {
        for (FindPath.Point point : list) {
            if (point.x == x && point.y == y) return true;
        }
        return false;
    }

    private LinkedList<FindPath.Point> AStar(int e) {

        open.add(start);
        this.e = e;
        int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};

        while (open != null&&open.size()!=0) {
            FindPath.Point test = findMin(open);
            open.remove(test);
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
                    for (FindPath.Point p = target; p != null; p = p.previous) {
                        result.addFirst(p);
                    }
                    if(result.get(0)==start)
                        return result;
                }

                if (!judgeIn(x2, y2, open)) {
                    map[x2][y2].setPrevious(test);
                    open.add(map[x2][y2]);
                } else  {
                    if (!judgeIn(x2, y2, inCon)) inCon.add(map[x2][y2]);
                    if (map[x2][y2].cost > test.cost + 1) map[x2][y2].setPrevious(test);
                }
            }
        }
        return null;
    }

    public void initial() {
        String data = StdIn.readLine();
        int[] nme = new int[3];
        for (int i = 0; i < 3; i++) nme[i] = Integer.parseInt(data.split(" ")[i]);
        int n = nme[0];
        int m = nme[1];
        int[][] map = new int[n][m];
        for (int i = 0; i < n; i++) {
            String Map = StdIn.readLine();
            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(Map.split(" ")[j]);
            }
        }
        int MagicTimes = Integer.parseInt(StdIn.readLine());
        int[] MagicE = new int[MagicTimes];
        int[] MagicLocationX = new int[MagicTimes];
        int[] MagicLocationY = new int[MagicTimes];
        int[][] MagicData = new int[3][];


        String[] Magic = new String[MagicTimes];
        for (int i = 0; i < MagicTimes; i++) {
            Magic[i] = StdIn.readLine();
        }
        for (int i = 0; i < MagicTimes; i++) {
            MagicE[i] = Integer.parseInt(Magic[i].split(" ")[0]);
            MagicLocationX[i] = Integer.parseInt(Magic[i].split(" ")[1]);
            MagicLocationY[i] = Integer.parseInt(Magic[i].split(" ")[2]);
        }
        MagicData[0] = MagicE;
        MagicData[1] = MagicLocationX;
        MagicData[2] = MagicLocationY;


        int consultTimes = StdIn.readInt();
        int[] consultE = new int[consultTimes];
        for (int i = 0; i < consultTimes; i++) {
            consultE[i] = StdIn.readInt();
        }
        StdOut.println();
        initial(nme, map, MagicData, consultE);
    }


    public void initial(int[] nme, int[][] mapData, int[][] MagicData, int[] consultData) {

        map = new FindPath.Point[nme[0]][nme[1]];
        this.e = nme[2];
        this.MagicData = MagicData;
        this.consultData = consultData;

        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[0].length; j++) {
                map[i][j] = new FindPath.Point(i, j, null);
                map[i][j].cost = Integer.MAX_VALUE;
                if (mapData[i][j] == 1) {
                    map[i][j].reachable = false;
                }
                if (mapData[i][j] == 0) {
                    map[i][j].reachable = true;
                }
            }
        }
        start = map[0][0];
        target = map[map.length - 1][map[0].length - 1];
        open = new LinkedList<>();
        close = new LinkedList<>();
        inCon = new LinkedList<>();
    }


    private String returnResult(LinkedList<FindPath.Point> list) {
        if(list == null)
            return null;
        StringBuilder result = new StringBuilder();
        for (FindPath.Point point : list) {
            result.append(point.x).append(" ").append(point.y).append(" ");
        }
        return result.toString();
    }

    private void SetAsStart(FindPath.Point newStart) {
        start.previous = newStart;
        newStart.previous = null;
        start.cost = 1;
        newStart.cost = 0;
        start = newStart;
    }

    public LinkedList<FindPath.Point> ARA(int initialE) {
        RealWay = new LinkedList<>();


        LinkedList<FindPath.Point> result= AStar(initialE) ;

        LinkedList<FindPath.Point> test = new LinkedList<>();

        while (start!=target) {

            RealWay.add(start);
            for (int i = 0; i < MagicData[0].length; i++) {
                if (initialE == MagicData[0][i]) {
                    int BlockX = MagicData[1][i];
                    int BlockY = MagicData[2][i];
                    FindPath.Point Block = map[BlockX][BlockY];
                    if (Block == start) {
                           continue;
                    }
                    map[BlockX][BlockY].reachable = false;
                    if (judgeIn(BlockX, BlockY, close) || judgeIn(BlockX, BlockY, open) || judgeIn(BlockX, BlockY, inCon)) {
                        open = new LinkedList<>();
                        close = new LinkedList<>();
                        inCon = new LinkedList<>();
                        lastAnswer=new LinkedList<>();
                        open.add(start);

                        result = AStar(initialE);
                    }
                }
            }

            for (int consultDatum : consultData) {
                if (consultDatum == initialE) {
                    if (result == null) {
                        StdOut.println("No Way");
                    } else {
                        if (lastAnswer == null) {
                            lastAnswer = result;
                        }
                        if (lastAnswer.size() > result.size()) {
                            result = lastAnswer;
                        }
                        StdOut.println(result.size());
                        StdOut.println(returnResult(result));



                    }
                }
            }

            if(result!=null&&result.size()>1&&result.get(1)!=null){
                SetAsStart(result.get(1));
            }
            if(result!=null){
                result.poll();
            }

            for (int i = 0; i < inCon.size(); i++) open.add(inCon.remove());
            if (initialE>1) initialE--;
        }
        RealWay.add(start);
        StdOut.println(RealWay.size());
        StdOut.println(returnResult(RealWay));
        return test;
    }

    public static void main(String[] args) {
        ARAStarMethod solve = new ARAStarMethod();
        solve.initial();
        solve.ARA(solve.e);
    }


}


