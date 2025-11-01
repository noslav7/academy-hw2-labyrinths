package academy.maze.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PrimGeneratorTest {

    @ParameterizedTest(name = "[{index}] invalid size w={0}, h={1}")
    @CsvSource({
        "0,5",
        "5,0",
        "-1,3"
    })
    void givenInvalidSize_whenGenerate_thenThrowIllegalArgument(int w, int h) {
        PrimGenerator gen = new PrimGenerator(new Random(1));
        assertThatThrownBy(() -> gen.generate(w, h)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "[{index}] small grid w={0}, h={1} opens all")
    @CsvSource({
        "1,1",
        "2,2"
    })
    void givenSmallGrid_whenGenerate_thenAllCellsArePath(int w, int h) {
        PrimGenerator gen = new PrimGenerator(new Random(1));
        Maze m = gen.generate(w, h);
        assertAllPath(m);
    }

    @Test
    void givenTypicalSize_whenGenerate_thenBordersAreWallsAndInsideHasPaths() {
        PrimGenerator gen = new PrimGenerator(new Random(123));
        int w = 21, h = 11;
        Maze m = gen.generate(w, h);
        CellType[][] c = m.cells();
        assertThat(c).hasDimensions(h, w);
        // borders are walls
        for (int x = 0; x < w; x++) {
            assertThat(c[0][x]).isEqualTo(CellType.WALL);
            assertThat(c[h - 1][x]).isEqualTo(CellType.WALL);
        }
        for (int y = 0; y < h; y++) {
            assertThat(c[y][0]).isEqualTo(CellType.WALL);
            assertThat(c[y][w - 1]).isEqualTo(CellType.WALL);
        }
        long pathCount = countPaths(c);
        assertThat(pathCount).isGreaterThan(0);
    }

    @Test
    void givenTwoOpenCells_whenSolveDijkstra_thenPathExistsFromStartToEnd() {
        PrimGenerator gen = new PrimGenerator(new Random(42));
        Maze m = gen.generate(21, 11);
        Point start = firstPath(m);
        Point end = lastPath(m);
        DijkstraSolver solver = new DijkstraSolver();
        Path path = solver.solve(m, start, end);
        assertThat(path.points()).isNotEmpty();
        assertThat(path.points()[0]).isEqualTo(start);
        assertThat(path.points()[path.points().length - 1]).isEqualTo(end);
    }

    @Test
    void givenSameSeed_whenGenerate_thenMazesAreIdentical() {
        int w = 21, h = 11;
        PrimGenerator g1 = new PrimGenerator(new Random(100));
        PrimGenerator g2 = new PrimGenerator(new Random(100));
        Maze m1 = g1.generate(w, h);
        Maze m2 = g2.generate(w, h);
        assertThat(equalCells(m1.cells(), m2.cells())).isTrue();
    }

    private static void assertAllPath(Maze m) {
        CellType[][] c = m.cells();
        for (CellType[] row : c) for (CellType cell : row) assertThat(cell).isEqualTo(CellType.PATH);
    }

    private static long countPaths(CellType[][] c) {
        long cnt = 0;
        for (CellType[] row : c) for (CellType cell : row) if (cell == CellType.PATH) cnt++;
        return cnt;
    }

    private static boolean equalCells(CellType[][] a, CellType[][] b) {
        if (a.length != b.length) return false;
        if (a.length == 0) return b.length == 0;
        if (a[0].length != b[0].length) return false;
        int h = a.length, w = a[0].length;
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) if (a[y][x] != b[y][x]) return false;
        return true;
    }

    private static Point firstPath(Maze m) {
        CellType[][] c = m.cells();
        for (int y = 0; y < c.length; y++) {
            for (int x = 0; x < c[0].length; x++) {
                if (c[y][x] == CellType.PATH) return new Point(x, y);
            }
        }
        return new Point(0, 0);
    }

    private static Point lastPath(Maze m) {
        CellType[][] c = m.cells();
        for (int y = c.length - 1; y >= 0; y--) {
            for (int x = c[0].length - 1; x >= 0; x--) {
                if (c[y][x] == CellType.PATH) return new Point(x, y);
            }
        }
        return new Point(0, 0);
    }
}


