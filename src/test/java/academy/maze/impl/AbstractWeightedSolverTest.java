package academy.maze.impl;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.MazesTestUtils;
import academy.maze.Solver;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

abstract class AbstractWeightedSolverTest<S extends Solver> {

    protected abstract S solverUnderTest();

    @ParameterizedTest(name = "[{index}] start={0},{1} end={2},{3} => len={4}")
    @CsvSource({"1,1, 3,3, 4", "1,1, 1,1, 0"})
    void givenSimpleMaze_whenSolve_thenExpectedLengthIsAsSpecified(int sx, int sy, int ex, int ey, int expectedLen) {
        Maze m = MazesTestUtils.mazeFrom("#####", "#   #", "# # #", "#   #", "#####");
        Point start = new Point(sx, sy);
        Point end = new Point(ex, ey);
        Solver solver = Objects.requireNonNull(solverUnderTest(), "solverUnderTest()");
        Path path = solver.solve(m, start, end);
        MazesTestUtils.assertValid(m, path, start, end);
        assertThat(path.points().length - 1).isEqualTo(expectedLen);
    }

    @Test
    void givenBlockedMaze_whenSolve_thenPathIsEmpty() {
        Maze m = MazesTestUtils.mazeFrom("#####", "# # #", "#####");
        Point start = new Point(1, 1);
        Point end = new Point(3, 1);
        Solver solver = Objects.requireNonNull(solverUnderTest(), "solverUnderTest()");
        Path path = solver.solve(m, start, end);
        assertThat(path.points()).isEmpty();
    }

    @Test
    void givenStartEqualsEnd_whenSolve_thenSinglePointPath() {
        Maze m = MazesTestUtils.mazeFrom("#####", "#   #", "#####");
        Point start = new Point(2, 1);
        Solver solver = Objects.requireNonNull(solverUnderTest(), "solverUnderTest()");
        Path path = solver.solve(m, start, start);
        assertThat(path.points()).hasSize(1);
        assertThat(path.points()[0]).isEqualTo(start);
    }
}
