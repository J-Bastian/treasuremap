package treasure.map.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import treasure.map.model.Position;

public abstract class AbstractFileParser<T> {

    private static final String COMMENT_LINE_PREFIX = "#";

    private static final Pattern COLUMN_SEPARATOR_REGEX = Pattern.compile("\s");

    private static final Pattern POSITION_PART_SEPARATOR_REGEX = Pattern.compile("-");

    private static final int POSITION_PART_AMOUNT = 2;

    public T parse(final Path path) throws IOException {
        T accumulator = getAccumulator();
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> consumeLine(line, accumulator));
        }

        return accumulator;
    }

    protected abstract T getAccumulator();

    private void consumeLine(final String line, final T accumulator) {
        if (line.isBlank()) {
            return;
        }

        if (line.startsWith(COMMENT_LINE_PREFIX)) {
            return;
        }

        String[] columns = COLUMN_SEPARATOR_REGEX.split(line);

        consume(columns, accumulator);
    }

    protected abstract void consume(String[] columns, T accumulator);

    protected Position parsePosition(final String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Received an empty raw position");
        }

        String[] positionParts = POSITION_PART_SEPARATOR_REGEX.split(string);

        if (positionParts.length != POSITION_PART_AMOUNT) {
            throw new IllegalArgumentException("Raw position <" + string + "> is not valid");
        }

        try {
            int column = Integer.parseInt(positionParts[0]);
            int line = Integer.parseInt(positionParts[1]);
            return new Position(line, column);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Position <" + string + "> is not valid");
        }

    }

}
