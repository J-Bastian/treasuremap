package treasure.map.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import treasure.map.model.MapDimension;
import treasure.map.model.Position;
import treasure.map.model.Treasure;

public class MapFileAccumulator {

    private final List<Treasure> treasures = new ArrayList<>();

    private final Set<Position> mountains = new HashSet<>();

    private MapDimension mapDimension;

    public MapFileAccumulator(final MapDimension mapDimension) {
        this.mapDimension = mapDimension;
    }

    public MapFileAccumulator() {
        this(null);
    }

    public void addMountain(final Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Mountain position is null");
        }

        validatePosition(position);

        mountains.add(position);

    }

    private void validatePosition(final Position position) {
        if (mapDimension != null && !mapDimension.contains(position)) {
            throw new IllegalArgumentException(position + " is not within the map");
        }

        if (mountains.contains(position)) {
            throw new IllegalArgumentException(position + " already contains a mountain");
        }

        for (Treasure treasure : treasures) {
            if (Objects.equals(position, treasure.position())) {
                throw new IllegalArgumentException(position + " already contains a treasure");
            }
        }
    }

    public void setMapDimension(final MapDimension mapDimension) {
        if (this.mapDimension != null) {
            throw new UnsupportedOperationException("Map dimension are already set");
        }

        this.mapDimension = mapDimension;

        mountains.forEach(this::validatePosition);
        treasures.forEach(treasure -> validatePosition(treasure.position()));
    }

    public void addTreasure(final Treasure treasure) {
        if (treasure == null) {
            throw new IllegalArgumentException("Treasure is null");
        }

        validatePosition(treasure.position());

        if (treasure.amount() <= 0) {
            throw new IllegalArgumentException("Treasure amount <" + treasure.amount() + "> should be strictly positive");
        }

        treasures.add(treasure);
    }

    public MapDimension getMapDimension() {
        return mapDimension;
    }

    public List<Treasure> getTreasures() {
        return treasures;
    }

    public Set<Position> getMountains() {
        return mountains;
    }
}
