package academy.maze.util;

import academy.maze.dto.CellType;
import academy.maze.dto.TerrainType;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class TerrainRandomizer {
    private TerrainRandomizer() {}

    public static TerrainType[][] randomize(CellType[][] cells, Random random) {
        if (cells.length == 0) return new TerrainType[0][0];
        Random rng = random != null ? random : ThreadLocalRandom.current();
        int height = cells.length;
        int width = cells[0].length;
        TerrainType[][] terrain = new TerrainType[height][width];
        int specialCount = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] == CellType.WALL) {
                    terrain[y][x] = TerrainType.NORMAL;
                    continue;
                }
                TerrainType type = roll(rng);
                terrain[y][x] = type;
                if (type != TerrainType.NORMAL) specialCount++;
            }
        }
        if (specialCount == 0) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (cells[y][x] == CellType.PATH) {
                        terrain[y][x] = TerrainType.SAND;
                        return terrain;
                    }
                }
            }
        }
        return terrain;
    }

    private static TerrainType roll(Random rng) {
        double r = rng.nextDouble();
        if (r < 0.6) return TerrainType.NORMAL;
        if (r < 0.8) return TerrainType.SAND;
        if (r < 0.95) return TerrainType.SWAMP;
        return TerrainType.PAVEMENT;
    }
}
