package academy.maze.impl;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import org.junit.jupiter.api.Test;

class DijkstraSolverTest {

    @Test
    void findsShortestPathOnSimpleMaze() {
        Maze m = mazeFrom(
            "#####",
            "#   #",
            "# # #",
            "#   #",
            "#####"
        );
        Point start = new Point(1, 1);
        Point end = new Point(3, 3);
        DijkstraSolver dijkstra = new DijkstraSolver();
        Path p = dijkstra.solve(m, start, end);
        assertValid(m, p, start, end);
        // expected minimal edges = 4 (points = 5)
        assertThat(p.points().length - 1).isEqualTo(4);
    }

    @Test
    void returnsEmptyWhenNoPath() {
        Maze m = mazeFrom(
            "#####",
            "# # #",
            "#####"
        );
        Point start = new Point(1, 1);
        Point end = new Point(3, 1);
        DijkstraSolver dijkstra = new DijkstraSolver();
        Path p = dijkstra.solve(m, start, end);
        assertThat(p.points()).isEmpty();
    }

    @Test
    void startEqualsEndProducesSinglePoint() {
        Maze m = mazeFrom(
            "#####",
            "#   #",
            "#####"
        );
        Point start = new Point(2, 1);
        DijkstraSolver dijkstra = new DijkstraSolver();
        Path p = dijkstra.solve(m, start, start);
        assertThat(p.points()).hasSize(1);
        assertThat(p.points()[0]).isEqualTo(start);
    }

    private static void assertValid(Maze m, Path path, Point start, Point end) {
        assertThat(path.points()).isNotEmpty();
        assertThat(path.points()[0]).isEqualTo(start);
        assertThat(path.points()[path.points().length - 1]).isEqualTo(end);
        CellType[][] c = m.cells();
        for (Point pt : path.points()) {
            assertThat(c[pt.y()][pt.x()]).isEqualTo(CellType.PATH);
        }
        for (int i = 1; i < path.points().length; i++) {
            int dx = Math.abs(path.points()[i].x() - path.points()[i - 1].x());
            int dy = Math.abs(path.points()[i].y() - path.points()[i - 1].y());
            assertThat(dx + dy).isEqualTo(1);
        }
    }

    private static Maze mazeFrom(String... lines) {
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
}
