package academy.maze.impl;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.MazesTestUtils;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AStarSolverTest {

    @Test
    void givenSimpleMaze_whenSolveAStar_thenLengthEqualsDijkstra() {
        Maze m = MazesTestUtils.mazeFrom("#####", "#   #", "# # #", "#   #", "#####");
        Point start = new Point(1, 1);
        Point end = new Point(3, 3);
        AStarSolver aStar = new AStarSolver();
        DijkstraSolver dijkstra = new DijkstraSolver();

        Path pa = aStar.solve(m, start, end);
        Path pd = dijkstra.solve(m, start, end);

        MazesTestUtils.assertValid(m, pa, start, end);
        MazesTestUtils.assertValid(m, pd, start, end);
        assertThat(pa.points().length).isEqualTo(pd.points().length);
    }

    @ParameterizedTest(name = "[{index}] start={0},{1} end={2},{3} => len={4}")
    @CsvSource({"1,1, 3,3, 4", "1,1, 1,1, 0"})
    void givenSimpleMaze_whenSolveAStar_thenExpectedLengthIsAsSpecified(
            int sx, int sy, int ex, int ey, int expectedLen) {
        Maze m = MazesTestUtils.mazeFrom("#####", "#   #", "# # #", "#   #", "#####");
        Point start = new Point(sx, sy);
        Point end = new Point(ex, ey);
        AStarSolver aStar = new AStarSolver();
        Path pa = aStar.solve(m, start, end);
        MazesTestUtils.assertValid(m, pa, start, end);
        assertThat(pa.points().length - 1).isEqualTo(expectedLen);
    }

    @Test
    void givenBlockedMaze_whenSolveAStar_thenPathIsEmpty() {
        Maze m = MazesTestUtils.mazeFrom("#####", "# # #", "#####");
        Point start = new Point(1, 1);
        Point end = new Point(3, 1);
        AStarSolver aStar = new AStarSolver();
        Path p = aStar.solve(m, start, end);
        assertThat(p.points()).isEmpty();
    }

    @Test
    void givenStartEqualsEnd_whenSolveAStar_thenSinglePointPath() {
        Maze m = MazesTestUtils.mazeFrom("#####", "#   #", "#####");
        Point start = new Point(2, 1);
        AStarSolver aStar = new AStarSolver();
        Path p = aStar.solve(m, start, start);
        assertThat(p.points()).hasSize(1);
        assertThat(p.points()[0]).isEqualTo(start);
    }

    // helpers moved to TestMazes
}
