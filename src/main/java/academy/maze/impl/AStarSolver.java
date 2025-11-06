package academy.maze.impl;

import academy.maze.Solver;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.util.PathUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarSolver implements Solver {
    @Override
    public Path solve(Maze maze, Point start, Point end) {
        int h = maze.cells().length;
        if (h == 0) return new Path(new Point[0]);
        int w = maze.cells()[0].length;

        PriorityQueue<int[]> open = new PriorityQueue<>((a, b) -> {
            int cf = Integer.compare(a[2], b[2]);
            if (cf != 0) return cf;
            int ch = Integer.compare(a[3], b[3]);
            if (ch != 0) return ch;
            int cy = Integer.compare(a[1], b[1]);
            if (cy != 0) return cy;
            return Integer.compare(a[0], b[0]);
        });
        Map<Integer, Integer> came = new HashMap<>();
        Map<Integer, Integer> g = new HashMap<>();
        Set<Integer> closed = new HashSet<>();

        int sKey = PathUtils.key(start.x(), start.y(), w);
        int eKey = PathUtils.key(end.x(), end.y(), w);
        g.put(sKey, 0);
        open.add(new int[] {
            start.x(),
            start.y(),
            heuristic(start.x(), start.y(), end.x(), end.y()),
            heuristic(start.x(), start.y(), end.x(), end.y())
        });

        while (!open.isEmpty()) {
            int[] cur = open.poll();
            int cx = cur[0], cy = cur[1];
            int cKey = PathUtils.key(cx, cy, w);
            if (cKey == eKey) {
                return PathUtils.reconstructPath(came, cKey, w);
            }
            if (!closed.add(cKey)) continue;
            for (int[] d : DIRS) {
                int nx = cx + d[0], ny = cy + d[1];
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                if (maze.cells()[ny][nx] == CellType.WALL) continue;
                int nKey = PathUtils.key(nx, ny, w);
                int tentative = g.getOrDefault(cKey, Integer.MAX_VALUE / 4) + maze.costAt(nx, ny);
                if (tentative < g.getOrDefault(nKey, Integer.MAX_VALUE / 4)) {
                    came.put(nKey, cKey);
                    g.put(nKey, tentative);
                    int hscore = heuristic(nx, ny, end.x(), end.y());
                    int f = tentative + hscore;
                    open.add(new int[] {nx, ny, f, hscore});
                }
            }
        }
        return new Path(new Point[0]);
    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static final int[][] DIRS = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
}
