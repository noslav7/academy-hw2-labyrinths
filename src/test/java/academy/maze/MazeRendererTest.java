package academy.maze;

import org.assertj.core.api.SoftAssertions;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.dto.TerrainType;
import academy.util.MazeRenderer;
import org.junit.jupiter.api.Test;

class MazeRendererTest {

    @Test
    void givenPathAndSurfaces_whenRender_thenPlusOnPathAndSurfacesKept() {
        // 3x3 grid, center is PAVEMENT '.'
        CellType[][] cells = new CellType[3][3];
        TerrainType[][] terrain = new TerrainType[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                cells[y][x] = CellType.PATH;
                terrain[y][x] = TerrainType.NORMAL;
            }
        }
        terrain[1][1] = TerrainType.PAVEMENT;
        Maze m = new Maze(cells, terrain);

        Path path = new Path(new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, 1)});
        String rendered = MazeRenderer.renderWithPath(m, path, new Point(0, 0), new Point(1, 1));

        // Start 'O' and End 'X' override path at ends; '+' appears on middle point
        String[] lines = rendered.split("\n");
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(lines[0].charAt(0)).isEqualTo('O');
        softly.assertThat(lines[1].charAt(1)).isEqualTo('X');
        softly.assertThat(lines[0].charAt(1)).isEqualTo('+');

        // Non-path PAVEMENT cell remains '.' (if not overridden by O/X/+)
        // In this case, end at (1,1) is 'X', so check another '.' placement
        // Set another pavement and ensure it's rendered
        terrain[2][2] = TerrainType.PAVEMENT;
        String rendered2 = MazeRenderer.renderWithPath(new Maze(cells, terrain), new Path(new Point[0]), new Point(0, 0), new Point(1, 1));
        String[] lines2 = rendered2.split("\n");
        softly.assertThat(lines2[2].charAt(2)).isEqualTo('.');
        softly.assertAll();
    }
}


