package academy.maze.impl;

import academy.maze.Solver;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/** Жадный поиск по наилучшей эвристике (Manhattan). Не учитывает веса поверхностей. */
public class GreedyBestFirstSolver implements Solver {
    @Override
    public Path solve(Maze maze, Point start, Point end) {
        int h = maze.cells().length;
        if (h == 0) return new Path(new Point[0]);
        int w = maze.cells()[0].length;

        PriorityQueue<int[]> open = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        Map<Integer, Integer> came = new HashMap<>();
        boolean[][] closed = new boolean[h][w];

        open.add(new int[] {start.x(), start.y(), heuristic(start.x(), start.y(), end.x(), end.y())});

        while (!open.isEmpty()) {
            int[] cur = open.poll();
            int cx = cur[0], cy = cur[1];
            if (cx == end.x() && cy == end.y()) return reconstruct(came, cx, cy, w);
            if (closed[cy][cx]) continue;
            closed[cy][cx] = true;
            for (int[] d : DIRS) {
                int nx = cx + d[0], ny = cy + d[1];
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                if (maze.cells()[ny][nx] == CellType.WALL) continue;
                int k = key(nx, ny, w);
                if (closed[ny][nx]) continue;
                came.put(k, key(cx, cy, w));
                open.add(new int[] {nx, ny, heuristic(nx, ny, end.x(), end.y())});
            }
        }
        return new Path(new Point[0]);
    }

    private Path reconstruct(Map<Integer, Integer> came, int ex, int ey, int w) {
        List<Point> rev = new ArrayList<>();
        Integer cur = key(ex, ey, w);
        while (cur != null) {
            rev.add(new Point(cur % w, cur / w));
            cur = came.get(cur);
        }
        int n = rev.size();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) pts[i] = rev.get(n - 1 - i);
        return new Path(pts);
    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private int key(int x, int y, int w) {
        return y * w + x;
    }

    private static final int[][] DIRS = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
}
