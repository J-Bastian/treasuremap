package treasure.map.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import treasure.map.model.Adventurer;
import treasure.map.model.Move;
import treasure.map.model.Orientation;
import treasure.map.model.Position;

public class AdventurerFileParser extends AbstractFileParser<List<Adventurer>> {

    private static final int ADVENTURER_COLUMN_AMOUNT = 4;

    @Override
    protected List<Adventurer> getAccumulator() {
        return new ArrayList<>();
    }

    @Override
    protected void consume(final String[] columns, final List<Adventurer> accumulator) {
        if (columns.length != ADVENTURER_COLUMN_AMOUNT) {
            throw new IllegalArgumentException("Received an invalid amount of column (" + columns.length + "). Expected:" + ADVENTURER_COLUMN_AMOUNT);
        }

        String name = parseName(columns[0]);
        Position position = parsePosition(columns[1]);
        Orientation orientation = parseOrientation(columns[2]);
        Deque<Move> moves = parseMoves(columns[3]);

        Adventurer adventurer = new Adventurer(name, moves, position, orientation);

        accumulator.add(adventurer);
    }

    private String parseName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Received an empty adventurer name");
        }

        return name;
    }

    private Orientation parseOrientation(final String rawOrientation) {
        if (rawOrientation == null || rawOrientation.length() != 1) {
            throw new IllegalArgumentException("Received an invalid orientation:" + rawOrientation);
        }

        char shortOrientationValue = rawOrientation.charAt(0);

        return Orientation.fromShortValue(shortOrientationValue)
                          .orElseThrow(() -> new IllegalArgumentException("No orientation found for value " + shortOrientationValue));

    }

    private Deque<Move> parseMoves(final String rawMoves) {
        if (rawMoves == null || rawMoves.isBlank()) {
            throw new IllegalArgumentException("Received an invalid moves:" + rawMoves);
        }

        Deque<Move> moves = new ArrayDeque<>(rawMoves.length());
        for (char rawMove : rawMoves.toCharArray()) {
            Move move = Move.fromShortValue(rawMove)
                            .orElseThrow(() -> new IllegalArgumentException("No Move found for value " + rawMove));

            moves.add(move);
        }

        return moves;
    }
}
