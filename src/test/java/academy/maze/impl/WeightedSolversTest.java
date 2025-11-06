package academy.maze.impl;

import academy.maze.MazesTestUtils;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.dto.TerrainType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class WeightedSolversTest {

    @Test
    void givenHighCostShortcutAndLowCostDetour_whenSolve_thenWeightedAlgorithmsChooseCheaper() {
        // 3x3 all PATH
        int w = 3, h = 3;
        CellType[][] cells = new CellType[h][w];
        TerrainType[][] terrain = new TerrainType[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                cells[y][x] = CellType.PATH;
                terrain[y][x] = TerrainType.NORMAL; // cost 1
            }
        }
        // Make (1,0) SWAMP (5), (0,1),(1,1),(2,1) PAVEMENT (0)
        terrain[0][1] = TerrainType.SWAMP; // expensive shortcut
        terrain[1][0] = TerrainType.PAVEMENT;
        terrain[1][1] = TerrainType.PAVEMENT;
        terrain[1][2] = TerrainType.PAVEMENT;
        // End cell (2,0) remains NORMAL (1)

        Maze m = new Maze(cells, terrain);
        Point start = new Point(0, 0);
        Point end = new Point(2, 0);

        AStarSolver aStar = new AStarSolver();
        DijkstraSolver dijkstra = new DijkstraSolver();
        BreadthFirstSolver bfs = new BreadthFirstSolver();
        GreedyBestFirstSolver greedy = new GreedyBestFirstSolver();

        Path pA = aStar.solve(m, start, end);
        Path pD = dijkstra.solve(m, start, end);
        Path pB = bfs.solve(m, start, end);
        Path pG = greedy.solve(m, start, end);

        MazesTestUtils.assertValid(m, pA, start, end);
        MazesTestUtils.assertValid(m, pD, start, end);
        MazesTestUtils.assertValid(m, pB, start, end);
        MazesTestUtils.assertValid(m, pG, start, end);

        int costA = pathCost(m, pA);
        int costD = pathCost(m, pD);
        int costB = pathCost(m, pB);
        int costG = pathCost(m, pG);

        // Optimal cost should be 1 (entering end NORMAL) via down-right-right-up with zero-cost pavements in between
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(costA).isEqualTo(1);
        softly.assertThat(costD).isEqualTo(1);

        // BFS/Greedy ignore weights -> choose 2-step straight path with cost 5(SWAMP) + 1(NORMAL) = 6
        softly.assertThat(costB).isEqualTo(6);
        softly.assertThat(costG).isEqualTo(6);
        softly.assertAll();
    }

    private static int pathCost(Maze m, Path p) {
        int sum = 0;
        Point[] pts = p.points();
        for (int i = 1; i < pts.length; i++) {
            sum += m.costAt(pts[i].x(), pts[i].y());
        }
        return sum;
    }
}
