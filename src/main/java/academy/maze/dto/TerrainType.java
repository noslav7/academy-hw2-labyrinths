package academy.maze.dto;

/**
 * Тип поверхности клетки и её стоимость входа. Символы в файлах/рендере: ' ' - обычная клетка (NORMAL) '~' - песок
 * (SAND) '^' - болото (SWAMP) '.' - хорошее покрытие (PAVEMENT)
 */
public enum TerrainType {
    NORMAL(1, ' '),
    SAND(3, '~'),
    SWAMP(5, '^'),
    PAVEMENT(0, '.');

    private final int moveCost;
    private final char symbol;

    TerrainType(int moveCost, char symbol) {
        this.moveCost = moveCost;
        this.symbol = symbol;
    }

    public int cost() {
        return moveCost;
    }

    public char symbol() {
        return symbol;
    }

    public static TerrainType fromChar(char c) {
        return switch (c) {
            case '~', '░' -> SAND;
            case '^', '▒' -> SWAMP;
            case '.', '∙' -> PAVEMENT;
            default -> NORMAL; // ' ', '+', '·' и любые прочие символы считаем NORMAL
        };
    }
}
