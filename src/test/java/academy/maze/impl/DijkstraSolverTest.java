package academy.maze.impl;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.MazesTestUtils;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DijkstraSolverTest {

    @ParameterizedTest(name = "[{index}] start={0},{1} end={2},{3} => len={4}")
    @CsvSource({"1,1, 3,3, 4", "1,1, 1,1, 0"})
    void givenSimpleMaze_whenSolveDijkstra_thenExpectedLengthIsAsSpecified(
            int sx, int sy, int ex, int ey, int expectedLen) {
        Maze m = MazesTestUtils.mazeFrom("#####", "#   #", "# # #", "#   #", "#####");
        Point start = new Point(sx, sy);
        Point end = new Point(ex, ey);
        DijkstraSolver dijkstra = new DijkstraSolver();
        Path p = dijkstra.solve(m, start, end);
        MazesTestUtils.assertValid(m, p, start, end);
        assertThat(p.points().length - 1).isEqualTo(expectedLen);
    }

    @Test
    void givenBlockedMaze_whenSolveDijkstra_thenPathIsEmpty() {
        Maze m = MazesTestUtils.mazeFrom("#####", "# # #", "#####");
        Point start = new Point(1, 1);
        Point end = new Point(3, 1);
        DijkstraSolver dijkstra = new DijkstraSolver();
        Path p = dijkstra.solve(m, start, end);
        assertThat(p.points()).isEmpty();
    }

    @Test
    void givenStartEqualsEnd_whenSolveDijkstra_thenSinglePointPath() {
        Maze m = MazesTestUtils.mazeFrom("#####", "#   #", "#####");
        Point start = new Point(2, 1);
        DijkstraSolver dijkstra = new DijkstraSolver();
        Path p = dijkstra.solve(m, start, start);
        assertThat(p.points()).hasSize(1);
        assertThat(p.points()[0]).isEqualTo(start);
    }

    // helpers moved to TestMazes
}
