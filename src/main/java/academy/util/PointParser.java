package academy.util;

import academy.maze.dto.Point;

public final class PointParser {
    private PointParser() {}

    public static Point parse(String s) {
        if (s == null || !s.contains(",")) {
            throw invalidFormat(s);
        }
        String[] parts = s.split(",", -1);
        if (parts.length != 2) {
            throw invalidFormat(s);
        }
        try {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            return new Point(x, y);
        } catch (NumberFormatException ex) {
            throw invalidFormat(s);
        }
    }

    private static IllegalArgumentException invalidFormat(String s) {
        return new IllegalArgumentException("Invalid point format: " + s + ", expected format: x,y");
    }
}
