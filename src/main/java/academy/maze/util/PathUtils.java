package academy.maze.util;

import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PathUtils {
    private PathUtils() {}

    public static int key(int x, int y, int width) {
        return y * width + x;
    }

    public static Path reconstructPath(Map<Integer, Integer> parent, int endKey, int width) {
        List<Point> rev = new ArrayList<>();
        Integer cur = endKey;
        while (cur != null) {
            rev.add(new Point(cur % width, cur / width));
            cur = parent.get(cur);
        }
        int n = rev.size();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = rev.get(n - 1 - i);
        }
        return new Path(pts);
    }
}
