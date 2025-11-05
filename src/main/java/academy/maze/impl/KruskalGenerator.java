package academy.maze.impl;

import academy.maze.Generator;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.util.Mazes;
import academy.maze.util.TerrainRandomizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Генерация лабиринта алгоритмом Краскала по сетке «комнат» на нечётных координатах. */
public class KruskalGenerator implements Generator {
    private final Random random;

    public KruskalGenerator() {
        this(new Random());
    }

    KruskalGenerator(Random random) {
        this.random = random;
    }

    @Override
    public Maze generate(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid size");
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.WALL;

        if (width < 3 || height < 3) {
            return Mazes.openAll(width, height, random);
        }

        // список «комнат» на нечётных координатах
        List<int[]> rooms = new ArrayList<>();
        for (int y = 1; y < height; y += 2) {
            for (int x = 1; x < width; x += 2) {
                rooms.add(new int[] {x, y});
                cells[y][x] = CellType.PATH;
            }
        }

        // ребра между соседними комнатами на расстоянии 2 (и стена между ними)
        List<int[]> edges = new ArrayList<>(); // [x1,y1,x2,y2,bx,by]
        for (int y = 1; y < height; y += 2) {
            for (int x = 1; x < width; x += 2) {
                if (x + 2 < width) edges.add(new int[] {x, y, x + 2, y, x + 1, y});
                if (y + 2 < height) edges.add(new int[] {x, y, x, y + 2, x, y + 1});
            }
        }
        Collections.shuffle(edges, random);

        // DSU на индексах комнат
        int rw = (width + 1) / 2; // число комнат по горизонтали
        int rh = (height + 1) / 2; // по вертикали
        int[] parent = new int[rw * rh];
        int[] rank = new int[rw * rh];
        for (int i = 0; i < parent.length; i++) parent[i] = i;

        for (int[] e : edges) {
            int x1 = e[0], y1 = e[1], x2 = e[2], y2 = e[3], bx = e[4], by = e[5];
            int a = roomIndex(x1, y1, rw);
            int b = roomIndex(x2, y2, rw);
            if (find(a, parent) != find(b, parent)) {
                union(a, b, parent, rank);
                cells[by][bx] = CellType.PATH; // пробиваем стену
            }
        }

        return new Maze(cells, TerrainRandomizer.randomize(cells, random));
    }

    private int roomIndex(int x, int y, int rw) {
        return (y - 1) / 2 * rw + (x - 1) / 2;
    }

    private int find(int x, int[] p) {
        return p[x] == x ? x : (p[x] = find(p[x], p));
    }

    private void union(int a, int b, int[] p, int[] r) {
        int ra = find(a, p), rb = find(b, p);
        if (ra == rb) return;
        if (r[ra] < r[rb]) p[ra] = rb;
        else if (r[ra] > r[rb]) p[rb] = ra;
        else {
            p[rb] = ra;
            r[ra]++;
        }
    }
}
