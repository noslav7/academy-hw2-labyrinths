package academy.maze.impl;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepthFirstGenerator extends AbstractRandomizedGenerator {
    public DepthFirstGenerator() {}

    // package-private for tests
    DepthFirstGenerator(java.util.Random random) {
        super(random);
    }

    @Override
    public Maze generate(int width, int height) {
        validateDimensions(width, height);
        Maze small = smallMazeOrNull(width, height);
        if (small != null) {
            return small;
        }
        CellType[][] cells = newWallGrid(width, height);

        boolean[][] visitedRooms = new boolean[height][width];
        // Start from the first odd cell inside the border
        carveRooms(1, 1, cells, visitedRooms, width, height);
        return new Maze(cells);
    }

    // Recursive backtracker over a grid of "rooms" located at odd coordinates.
    private void carveRooms(int x, int y, CellType[][] cells, boolean[][] visitedRooms, int width, int height) {
        visitedRooms[y][x] = true;
        cells[y][x] = CellType.PATH;

        List<int[]> dirs = new ArrayList<>();
        dirs.add(new int[] {2, 0});
        dirs.add(new int[] {-2, 0});
        dirs.add(new int[] {0, 2});
        dirs.add(new int[] {0, -2});
        Collections.shuffle(dirs, random);

        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1 && !visitedRooms[ny][nx]) {
                // Open the wall between (x,y) and (nx,ny)
                int bx = x + d[0] / 2;
                int by = y + d[1] / 2;
                cells[by][bx] = CellType.PATH;
                carveRooms(nx, ny, cells, visitedRooms, width, height);
            }
        }
    }
}
