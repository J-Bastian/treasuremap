package treasure.map.model;

public record MapDimension(int height, int width) {

    public boolean contains(final Position position) {
        if (position == null) {
            return false;
        }

        if (position.line() <= 0 || position.column() <= 0) {
            return false;
        }

        if (position.line() > height || position.column() > width) {
            return false;
        }

        return true;
    }

}
