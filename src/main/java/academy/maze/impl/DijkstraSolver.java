package academy.maze.impl;

import academy.maze.Solver;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.util.Directions;
import academy.maze.util.PathUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraSolver implements Solver {
    @Override
    public Path solve(Maze maze, Point start, Point end) {
        int h = maze.cells().length;
        if (h == 0) return new Path(new Point[0]);
        int w = maze.cells()[0].length;

        int[][] dist = new int[h][w];
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) dist[y][x] = Integer.MAX_VALUE / 4;
        Map<Integer, Integer> prev = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));

        dist[start.y()][start.x()] = 0;
        pq.add(new int[] {start.x(), start.y(), 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cx = cur[0], cy = cur[1];
            int cd = cur[2];
            if (cd != dist[cy][cx]) continue; // outdated entry
            if (cx == end.x() && cy == end.y()) break;
            for (Directions.Direction dir : Directions.CARDINAL) {
                int nx = cx + dir.dx(), ny = cy + dir.dy();
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                if (maze.cells()[ny][nx] == CellType.WALL) continue;
                int nd = dist[cy][cx] + maze.costAt(nx, ny);
                if (nd < dist[ny][nx]) {
                    dist[ny][nx] = nd;
                    prev.put(PathUtils.key(nx, ny, w), PathUtils.key(cx, cy, w));
                    pq.add(new int[] {nx, ny, nd});
                }
            }
        }
        if (dist[end.y()][end.x()] >= Integer.MAX_VALUE / 8) return new Path(new Point[0]);
        return PathUtils.reconstructPath(prev, PathUtils.key(end.x(), end.y(), w), w);
    }
}
