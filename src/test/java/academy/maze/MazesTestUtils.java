package academy.maze;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import org.assertj.core.api.SoftAssertions;

public final class MazesTestUtils {
    private MazesTestUtils() {}

    public static Maze mazeFrom(String... lines) {
        int h = lines.length;
        int w = lines[0].length();
        CellType[][] cells = new CellType[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                cells[y][x] = lines[y].charAt(x) == '#' ? CellType.WALL : CellType.PATH;
            }
        }
        return new Maze(cells);
    }

    public static void assertValid(Maze m, Path path, Point start, Point end) {
        CellType[][] c = m.cells();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(path.points()).as("path must not be empty").isNotEmpty();
            if (path.points().length > 0) {
                softly.assertThat(path.points()[0]).as("first point equals start").isEqualTo(start);
                softly.assertThat(path.points()[path.points().length - 1]).as("last point equals end").isEqualTo(end);
            }
            for (Point pt : path.points()) {
                softly.assertThat(c[pt.y()][pt.x()]).as("point %s is PATH", pt).isEqualTo(CellType.PATH);
            }
            for (int i = 1; i < path.points().length; i++) {
                int dx = Math.abs(path.points()[i].x() - path.points()[i - 1].x());
                int dy = Math.abs(path.points()[i].y() - path.points()[i - 1].y());
                softly.assertThat(dx + dy).as("step %s->%s is 4-neighbor", path.points()[i - 1], path.points()[i]).isEqualTo(1);
            }
        });
    }
}


