package academy.util;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.dto.TerrainType;

public final class MazeRenderer {
    private MazeRenderer() {}

    public enum GlyphStyle {
        ASCII,
        UNICODE
    }

    public static String renderWithPath(Maze maze, Path path, Point start, Point end) {
        return renderWithPath(maze, path, start, end, GlyphStyle.ASCII);
    }

    public static String renderWithPath(Maze maze, Path path, Point start, Point end, GlyphStyle style) {
        char[][] out = baseGrid(maze, style);
        int width = out.length == 0 ? 0 : out[0].length;
        int height = out.length;

        char pathChar = style == GlyphStyle.UNICODE ? '+' : '.';
        if (path != null) {
            for (Point p : path.points()) {
                if (isInBounds(p, width, height)) {
                    out[p.y()][p.x()] = pathChar;
                }
            }
        }

        if (isInBounds(start, width, height)) out[start.y()][start.x()] = 'O';
        if (isInBounds(end, width, height)) out[end.y()][end.x()] = 'X';

        return toText(out);
    }

    public static String renderMaze(Maze maze, GlyphStyle style, boolean includeBorder) {
        char[][] grid = baseGrid(maze, style);
        if (!includeBorder) {
            return toText(grid);
        }
        int height = grid.length;
        int width = height == 0 ? 0 : grid[0].length;
        StringBuilder sb = new StringBuilder();

        if (style == GlyphStyle.ASCII) {
            sb.append("#".repeat(width + 2)).append('\n');
            for (int y = 0; y < height; y++) {
                sb.append('#');
                sb.append(grid[y]);
                sb.append('#').append('\n');
            }
            sb.append("#".repeat(width + 2)).append('\n');
        } else {
            sb.append('┏').append("━".repeat(width)).append('┓').append('\n');
            for (int y = 0; y < height; y++) {
                sb.append('┃');
                sb.append(grid[y]);
                sb.append('┃').append('\n');
            }
            sb.append('┗').append("━".repeat(width)).append('┛').append('\n');
        }
        return sb.toString();
    }

    private static char[][] baseGrid(Maze maze, GlyphStyle style) {
        int height = maze.cells().length;
        int width = height == 0 ? 0 : maze.cells()[0].length;
        char[][] out = new char[height][width];
        TerrainType[][] terrain = maze.terrain();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maze.cells()[y][x] == CellType.WALL) {
                    out[y][x] = style == GlyphStyle.ASCII ? '#' : '█';
                } else {
                    TerrainType type =
                            terrain != null && terrain.length > y && terrain[y] != null && terrain[y].length > x
                                    ? terrain[y][x]
                                    : TerrainType.NORMAL;
                    out[y][x] = terrainSymbol(type, style);
                }
            }
        }
        return out;
    }

    private static char terrainSymbol(TerrainType type, GlyphStyle style) {
        if (style == GlyphStyle.ASCII) {
            return type.symbol();
        }
        return switch (type) {
            case NORMAL -> '·';
            case SAND -> '░';
            case SWAMP -> '▒';
            case PAVEMENT -> '∙';
        };
    }

    private static boolean isInBounds(Point p, int width, int height) {
        return p != null && p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
    }

    private static String toText(char[][] grid) {
        int height = grid.length;
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            sb.append(grid[y]);
            sb.append('\n');
        }
        return sb.toString();
    }
}
