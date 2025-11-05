package academy.maze.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.TerrainType;
import java.util.Random;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KruskalGeneratorTest {

    @ParameterizedTest(name = "[{index}] invalid size w={0}, h={1}")
    @CsvSource({"0,5", "5,0", "-1,3"})
    void givenInvalidSize_whenGenerate_thenThrowIllegalArgument(int w, int h) {
        KruskalGenerator gen = new KruskalGenerator(new Random(1));
        assertThatThrownBy(() -> gen.generate(w, h)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenTypicalSize_whenGenerate_thenBordersAreWallsAndInsideHasPaths() {
        KruskalGenerator gen = new KruskalGenerator(new Random(123));
        int w = 21, h = 11;
        Maze m = gen.generate(w, h);
        CellType[][] c = m.cells();
        assertThat(c).hasDimensions(h, w);
        SoftAssertions softly = new SoftAssertions();
        // borders are walls
        for (int x = 0; x < w; x++) {
            softly.assertThat(c[0][x]).isEqualTo(CellType.WALL);
            softly.assertThat(c[h - 1][x]).isEqualTo(CellType.WALL);
        }
        for (int y = 0; y < h; y++) {
            softly.assertThat(c[y][0]).isEqualTo(CellType.WALL);
            softly.assertThat(c[y][w - 1]).isEqualTo(CellType.WALL);
        }
        long pathCount = countPaths(c);
        softly.assertThat(pathCount).isGreaterThan(0);
        softly.assertThat(hasSpecialTerrain(m)).isTrue();
        softly.assertAll();
    }

    @Test
    void givenSameSeed_whenGenerate_thenMazesAreIdentical() {
        int w = 21, h = 11;
        KruskalGenerator g1 = new KruskalGenerator(new Random(100));
        KruskalGenerator g2 = new KruskalGenerator(new Random(100));
        Maze m1 = g1.generate(w, h);
        Maze m2 = g2.generate(w, h);
        assertThat(equalCells(m1.cells(), m2.cells())).isTrue();
        assertThat(equalTerrain(m1.terrain(), m2.terrain())).isTrue();
    }

    private static long countPaths(CellType[][] c) {
        long cnt = 0;
        for (CellType[] row : c) for (CellType cell : row) if (cell == CellType.PATH) cnt++;
        return cnt;
    }

    private static boolean equalCells(CellType[][] a, CellType[][] b) {
        if (a.length != b.length) return false;
        if (a.length == 0) return b.length == 0;
        if (a[0].length != b[0].length) return false;
        int h = a.length, w = a[0].length;
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) if (a[y][x] != b[y][x]) return false;
        return true;
    }

    private static boolean equalTerrain(TerrainType[][] a, TerrainType[][] b) {
        if (a.length != b.length) return false;
        if (a.length == 0) return b.length == 0;
        if (a[0].length != b[0].length) return false;
        int h = a.length, w = a[0].length;
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) if (a[y][x] != b[y][x]) return false;
        return true;
    }

    private static boolean hasSpecialTerrain(Maze m) {
        TerrainType[][] terrain = m.terrain();
        CellType[][] cells = m.cells();
        for (int y = 0; y < terrain.length; y++) {
            for (int x = 0; x < terrain[y].length; x++) {
                if (cells[y][x] == CellType.PATH && terrain[y][x] != TerrainType.NORMAL) {
                    return true;
                }
            }
        }
        return false;
    }
}
