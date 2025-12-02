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
        Path safePath = java.util.Objects.requireNonNull(path, "path must not be null");
        Point[] pointsArray = java.util.Objects.requireNonNull(safePath.points(), "path points must not be null");
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(path).as("path must not be null").isNotNull();
            softly.assertThat(pointsArray).as("path points must not be null").isNotNull();
            softly.assertThat(pointsArray).as("path must not be empty").isNotEmpty();
            if (pointsArray.length > 0) {
                softly.assertThat(pointsArray[0]).as("first point equals start").isEqualTo(start);
                softly.assertThat(pointsArray[pointsArray.length - 1])
                        .as("last point equals end")
                        .isEqualTo(end);
            }
            for (Point ptRaw : pointsArray) {
                Point pt = java.util.Objects.requireNonNull(ptRaw, "path point must not be null");
                CellType[] row =
                        java.util.Objects.requireNonNull(c[pt.y()], () -> "row %s must not be null".formatted(pt.y()));
                softly.assertThat(row)
                        .as("row %s for point %s must not be null", pt.y(), pt)
                        .isNotNull();
                CellType cell =
                        java.util.Objects.requireNonNull(row[pt.x()], () -> "cell %s must not be null".formatted(pt));
                if (cell != CellType.PATH) {
                    softly.fail("point %s expected PATH but was %s", pt, cell);
                }
            }
            for (int i = 1; i < pointsArray.length; i++) {
                Point currentRaw = pointsArray[i];
                Point previousRaw = pointsArray[i - 1];
                Point current = java.util.Objects.requireNonNull(currentRaw, "current path point must not be null");
                Point previous = java.util.Objects.requireNonNull(previousRaw, "previous path point must not be null");
                int dx = Math.abs(current.x() - previous.x());
                int dy = Math.abs(current.y() - previous.y());
                softly.assertThat(dx + dy)
                        .as("step %s->%s is 4-neighbor", previous, current)
                        .isEqualTo(1);
            }
        });
    }
}
