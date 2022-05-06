package treasure.map.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import treasure.map.utils.MapUtils;

public class TreasureMap {

    private final MapDimension mapDimension;

    private final List<Adventurer> adventurers;

    private final Map<Position, Integer> treasureAmountByPosition;

    private final Set<Position> mountains;

    public TreasureMap(final MapDimension mapDimension,
                       final List<Treasure> treasures,
                       final Set<Position> mountains,
                       final List<Adventurer> adventurers) {
        this.mapDimension = mapDimension;
        this.treasureAmountByPosition = MapUtils.toMap(treasures, Treasure::position, Treasure::amount);
        this.mountains = mountains;
        this.adventurers = adventurers;
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    public PositionStatus isPositionAvailable(final Position position) {
        if (position == null) {
            return PositionStatus.FORBIDDEN;
        }

        if (!mapDimension.contains(position)) {
            return PositionStatus.FORBIDDEN;
        }

        if (mountains.contains(position)) {
            return PositionStatus.FORBIDDEN;
        }

        return adventurers.stream()
                          // Check if we have an andventurer on the wanted position
                          .filter(adventurer -> position.equals(adventurer.getPosition()))
                          .findFirst()
                          // If the adventurer may still move, just wait
                          .map(adventurer -> adventurer.isOutOfMove() ? PositionStatus.FORBIDDEN : PositionStatus.AVAILABLE_LATER)
                          // No adventurer => available
                          .orElse(PositionStatus.CURRENTLY_AVAILABLE);

    }

    public boolean isDone() {
        return adventurers.stream()
                          .allMatch(adventurer -> adventurer.isOutOfMove() && !hasTreasure(adventurer.getPosition()));
    }

    public boolean hasTreasure(final Position position) {
        return treasureAmountByPosition.containsKey(position);
    }

    public Treasure retrieveTreasure(final Position position) {
        Integer treasureAmount = treasureAmountByPosition.get(position);

        if (treasureAmount == null || treasureAmount == 0) {
            throw new IllegalStateException("Tried to retrieve a treasure from " + position + " but there is none");
        }

        if (treasureAmount == 1) {
            treasureAmountByPosition.remove(position);
        } else {
            treasureAmount--;
            treasureAmountByPosition.put(position, treasureAmount);
        }

        return new Treasure(position, 1);
    }

    public boolean hasTreasures() {
        return !treasureAmountByPosition.isEmpty();
    }

    public List<Treasure> getRemainingTreasures() {
        return treasureAmountByPosition.entrySet()
                                       .stream()
                                       .map(entry -> new Treasure(entry.getKey(), entry.getValue()))
                                       .collect(Collectors.toList());
    }

}
