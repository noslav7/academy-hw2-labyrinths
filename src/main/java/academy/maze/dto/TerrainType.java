package academy.maze.dto;

/**
 * Тип поверхности клетки и её стоимость входа. Символы в файлах/рендере: ' ' - обычная клетка (NORMAL) '~' - песок
 * (SAND) '^' - болото (SWAMP) '.' - хорошее покрытие (PAVEMENT)
 */
public enum TerrainType {
    NORMAL(1, ' ', '·'),
    SAND(3, '~', '░'),
    SWAMP(5, '^', '▒'),
    PAVEMENT(0, '.', '∙');

    private final int moveCost;
    private final char symbol;
    private final char unicodeSymbol;

    TerrainType(int moveCost, char symbol, char unicodeSymbol) {
        this.moveCost = moveCost;
        this.symbol = symbol;
        this.unicodeSymbol = unicodeSymbol;
    }

    public int cost() {
        return moveCost;
    }

    public char symbol() {
        return symbol;
    }

    public char unicodeSymbol() {
        return unicodeSymbol;
    }

    public static TerrainType fromChar(char c) {
        return switch (c) {
            case ' ', '+', '·', 'O', 'X' -> NORMAL;
            case '~', '░' -> SAND;
            case '^', '▒' -> SWAMP;
            case '.', '∙' -> PAVEMENT;
            default -> throw new IllegalArgumentException("Unsupported terrain symbol: '" + c + '\'');
        };
    }
}
