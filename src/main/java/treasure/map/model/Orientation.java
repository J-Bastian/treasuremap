package treasure.map.model;

import java.util.Map;
import java.util.Optional;

import treasure.map.utils.MapUtils;

public enum Orientation {

    NORTH('N'),

    EAST('E'),

    SOUTH('S'),

    WEST('O');

    private static final Map<Character, Orientation> ORIENTATION_BY_SHORT_VALUE = MapUtils.toMap(Orientation.values(), Orientation::getShortValue);

    private final char shortValue;

    private Orientation(final char shortValue) {
        this.shortValue = shortValue;
    }

    public char getShortValue() {
        return shortValue;
    }

    public Orientation rotateRight() {
        switch (this) {
        case NORTH:
            return EAST;
        case EAST:
            return SOUTH;
        case SOUTH:
            return WEST;
        case WEST:
            return NORTH;
        default:
            throw new UnsupportedOperationException("Orientation " + this + " can not be rotated right");
        }
    }

    public Orientation rotateLeft() {
        switch (this) {
        case NORTH:
            return WEST;
        case WEST:
            return SOUTH;
        case SOUTH:
            return EAST;
        case EAST:
            return NORTH;
        default:
            throw new UnsupportedOperationException("Orientation " + this + " can not be rotated left");
        }
    }

    public static Optional<Orientation> fromShortValue(final char c) {
        Orientation o = ORIENTATION_BY_SHORT_VALUE.get(c);

        return Optional.ofNullable(o);
    }

}
