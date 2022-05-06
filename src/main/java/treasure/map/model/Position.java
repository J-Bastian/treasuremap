package treasure.map.model;

public record Position(int line, int column) {

    public Position withPreviousLine() {
        return new Position(line - 1, column);
    }

    public Position withNextLine() {
        return new Position(line + 1, column);
    }

    public Position withPreviousColumn() {
        return new Position(line, column - 1);
    }

    public Position withNextColumn() {
        return new Position(line, column + 1);
    }
}