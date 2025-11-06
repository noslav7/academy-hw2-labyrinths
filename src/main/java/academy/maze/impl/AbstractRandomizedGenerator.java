package academy.maze.impl;

import academy.maze.Generator;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.util.Mazes;
import java.util.Random;

abstract class AbstractRandomizedGenerator implements Generator {
    protected final Random random;

    protected AbstractRandomizedGenerator() {
        this(new Random());
    }

    protected AbstractRandomizedGenerator(Random random) {
        this.random = random;
    }

    protected void validateDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid size");
        }
    }

    protected Maze smallMazeOrNull(int width, int height) {
        if (width < 3 || height < 3) {
            return Mazes.openAll(width, height, random);
        }
        return null;
    }

    protected CellType[][] newWallGrid(int width, int height) {
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = CellType.WALL;
            }
        }
        return cells;
    }
}
