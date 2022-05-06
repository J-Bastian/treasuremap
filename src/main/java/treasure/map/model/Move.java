package treasure.map.model;

import java.util.Map;
import java.util.Optional;

import treasure.map.utils.MapUtils;

public enum Move {

    ADVANCE('A'),

    TURN_RIGHT('D'),

    TURN_LEFT('G');

    private static final Map<Character, Move> MOVE_BY_SHORT_VALUE = MapUtils.toMap(Move.values(), Move::getShortValue);

    private final char shortValue;

    private Move(final char shortValue) {
        this.shortValue = shortValue;
    }

    public char getShortValue() {
        return shortValue;
    }

    public static Optional<Move> fromShortValue(final char c) {
        Move move = MOVE_BY_SHORT_VALUE.get(c);

        return Optional.ofNullable(move);
    }
}
