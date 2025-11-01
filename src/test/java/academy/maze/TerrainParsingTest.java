package academy.maze;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.SoftAssertions;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.TerrainType;
import academy.util.MazeIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TerrainParsingTest {

    @Test
    void givenMixedSymbols_whenRead_thenTerrainAndCellsParsedCorrectly() throws Exception {
        String content = "#.~\n" +
                " ^ \n";
        Path tmp = Files.createTempFile("maze-terrain-", ".txt");
        Files.writeString(tmp, content);

        Maze m = MazeIO.read(new File(tmp.toString()));

        CellType[][] c = m.cells();
        TerrainType[][] t = m.terrain();
        assertThat(c).hasDimensions(2, 3);
        SoftAssertions softly = new SoftAssertions();

        // Row 0: ['#','.', '~'] => [WALL, PATH, PATH]
        softly.assertThat(c[0][0]).isEqualTo(CellType.WALL);
        softly.assertThat(c[0][1]).isEqualTo(CellType.PATH);
        softly.assertThat(c[0][2]).isEqualTo(CellType.PATH);
        softly.assertThat(t[0][0]).isEqualTo(TerrainType.NORMAL); // ignored on wall
        softly.assertThat(t[0][1]).isEqualTo(TerrainType.PAVEMENT);
        softly.assertThat(t[0][2]).isEqualTo(TerrainType.SAND);

        // Row 1: [' ', '^', ' '] => [PATH, PATH, PATH]
        softly.assertThat(c[1][0]).isEqualTo(CellType.PATH);
        softly.assertThat(c[1][1]).isEqualTo(CellType.PATH);
        softly.assertThat(c[1][2]).isEqualTo(CellType.PATH);
        softly.assertThat(t[1][0]).isEqualTo(TerrainType.NORMAL);
        softly.assertThat(t[1][1]).isEqualTo(TerrainType.SWAMP);
        softly.assertThat(t[1][2]).isEqualTo(TerrainType.NORMAL);
        softly.assertAll();
    }

    @Test
    void givenUnknownSymbols_whenRead_thenTreatedAsNormal() throws Exception {
        String content = " a \n" +
                " b \n";
        Path tmp = Files.createTempFile("maze-terrain-unk-", ".txt");
        Files.writeString(tmp, content);
        Maze m = MazeIO.read(new File(tmp.toString()));
        TerrainType[][] t = m.terrain();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(t[0][1]).isEqualTo(TerrainType.NORMAL);
        softly.assertThat(t[1][1]).isEqualTo(TerrainType.NORMAL);
        softly.assertAll();
    }

    @Test
    void givenDefaultConstructor_whenCostAt_thenNormalCostOne() {
        CellType[][] cells = new CellType[][] {
                {CellType.PATH}
        };
        Maze m = new Maze(cells);
        assertThat(m.costAt(0, 0)).isEqualTo(1);
    }
}


