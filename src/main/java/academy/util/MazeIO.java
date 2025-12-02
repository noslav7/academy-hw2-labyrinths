package academy.util;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.TerrainType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public final class MazeIO {
    private MazeIO() {}

    public static Maze read(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            if (lines.isEmpty()) return new Maze(new CellType[0][0]);
            int width = lines.get(0).length();
            int height = lines.size();
            CellType[][] cells = new CellType[height][width];
            TerrainType[][] terrain = new TerrainType[height][width];
            for (int y = 0; y < height; y++) {
                String line = lines.get(y);
                for (int x = 0; x < width; x++) {
                    char c = line.charAt(x);
                    if (WALL_GLYPHS.contains(c)) {
                        cells[y][x] = CellType.WALL;
                        terrain[y][x] = TerrainType.NORMAL;
                    } else {
                        cells[y][x] = CellType.PATH;
                        terrain[y][x] = TerrainType.fromChar(c);
                    }
                }
            }
            return new Maze(cells, terrain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Set<Character> WALL_GLYPHS = Set.of(
            '#', '█', '┏', '┓', '┗', '┛', '┳', '┻', '┯', '┷', '┠', '┨', '┝', '┥', '┞', '┟', '┢', '┡', '┤', '├', '┐',
            '┌', '┘', '└', '│', '┃', '─', '━');

    public static void write(File output, String text) {
        try {
            Path parent = output.toPath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(output.toPath(), text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
