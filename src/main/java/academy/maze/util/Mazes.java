package academy.maze.util;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.Random;

public final class Mazes {
    private Mazes() {}

    public static Maze openAll(int width, int height) {
        return openAll(width, height, new Random());
    }

    public static Maze openAll(int width, int height, Random random) {
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.PATH;
        return new Maze(cells, TerrainRandomizer.randomize(cells, random));
    }
}


