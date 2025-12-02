package academy.maze.impl;

import academy.maze.Solver;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.util.Directions;
import academy.maze.util.PathUtils;
import java.util.ArrayDeque;
import java.util.HashMap;
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
            for (Directions.Direction dir : Directions.CARDINAL) {
                int nx = cx + dir.dx(), ny = cy + dir.dy();
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                if (maze.cells()[ny][nx] == CellType.WALL) continue;
                if (seen[ny][nx]) continue;
                seen[ny][nx] = true;
                prev.put(PathUtils.key(nx, ny, w), PathUtils.key(cx, cy, w));
                dq.add(new int[] {nx, ny});
            }
        }
        if (!seen[end.y()][end.x()]) return new Path(new Point[0]);
        return PathUtils.reconstructPath(prev, PathUtils.key(end.x(), end.y(), w), w);
    }
}
