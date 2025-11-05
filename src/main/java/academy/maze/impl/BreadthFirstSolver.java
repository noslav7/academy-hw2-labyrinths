package academy.maze.impl;

import academy.maze.Solver;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Поиск в ширину (не учитывает веса поверхности, все шаги равны по стоимости). */
public class BreadthFirstSolver implements Solver {
    @Override
    public Path solve(Maze maze, Point start, Point end) {
        int h = maze.cells().length;
        if (h == 0) return new Path(new Point[0]);
        int w = maze.cells()[0].length;

        boolean[][] seen = new boolean[h][w];
        Map<Integer, Integer> prev = new HashMap<>();
        ArrayDeque<int[]> dq = new ArrayDeque<>();

        seen[start.y()][start.x()] = true;
        dq.add(new int[] {start.x(), start.y()});

        while (!dq.isEmpty()) {
            int[] cur = dq.pollFirst();
            int cx = cur[0], cy = cur[1];
            if (cx == end.x() && cy == end.y()) break;
            for (int[] d : DIRS) {
                int nx = cx + d[0], ny = cy + d[1];
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                if (maze.cells()[ny][nx] == CellType.WALL) continue;
                if (seen[ny][nx]) continue;
                seen[ny][nx] = true;
                prev.put(key(nx, ny, w), key(cx, cy, w));
                dq.add(new int[] {nx, ny});
            }
        }
        if (!seen[end.y()][end.x()]) return new Path(new Point[0]);
        // reconstruct
        List<Point> rev = new ArrayList<>();
        int k = key(end.x(), end.y(), w);
        while (true) {
            rev.add(new Point(k % w, k / w));
            Integer p = prev.get(k);
            if (p == null) break;
            k = p;
        }
        int n = rev.size();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) pts[i] = rev.get(n - 1 - i);
        return new Path(pts);
    }

    private int key(int x, int y, int w) {
        return y * w + x;
    }

    private static final int[][] DIRS = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
}
