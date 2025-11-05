package academy.util;

import academy.maze.dto.Point;

public final class PointParser {
    private PointParser() {}

    public static Point parseOrExit(String s) {
        if (s == null || !s.contains(",")) {
            System.err.println("Invalid point format: " + s + ", expected format: x,y");
            System.exit(2);
        }
        String[] parts = s.split(",", -1);
        if (parts.length != 2) {
            System.err.println("Invalid point format: " + s + ", expected format: x,y");
            System.exit(2);
        }
        try {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            return new Point(x, y);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid point format: " + s + ", expected format: x,y");
            System.exit(2);
            return new Point(0, 0); // unreachable
        }
    }
}
