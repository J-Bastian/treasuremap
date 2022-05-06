package treasure.map.service;

import java.util.Optional;
import java.util.function.Function;

import treasure.map.model.Adventurer;
import treasure.map.model.Move;
import treasure.map.model.Orientation;
import treasure.map.model.Position;
import treasure.map.model.PositionStatus;
import treasure.map.model.Treasure;
import treasure.map.model.TreasureMap;

public class WorldManager {

    private final TreasureMap treasureMap;

    public WorldManager(final TreasureMap treasureMap) {
        this.treasureMap = treasureMap;
    }

    public void tick() {
        for (Adventurer adventurer : treasureMap.getAdventurers()) {
            process(adventurer);
        }

    }

    public boolean hasMoreTick() {
        return !treasureMap.isDone();
    }

    private void process(final Adventurer adventurer) {
        if (isOnTreasure(adventurer)) {
            retrieveTreasure(adventurer);
        } else if (!adventurer.isOutOfMove()) {
            move(adventurer);
        }
    }

    private boolean isOnTreasure(final Adventurer adventurer) {
        Position position = adventurer.getPosition();

        return treasureMap.hasTreasure(position);
    }

    private void retrieveTreasure(final Adventurer adventurer) {
        Position position = adventurer.getPosition();
        Treasure treasure = treasureMap.retrieveTreasure(position);
        adventurer.addNewTreasure(treasure);
    }

    private void move(final Adventurer adventurer) {
        Optional<Move> nextMoveOpt = adventurer.getNextMove();

        if (nextMoveOpt.isEmpty()) {
            return;
        }

        Move nextMove = nextMoveOpt.get();

        switch (nextMove) {
        case ADVANCE:
            advance(adventurer);
            break;
        case TURN_LEFT:
            doTurn(adventurer, Orientation::rotateLeft);
            break;
        case TURN_RIGHT:
            doTurn(adventurer, Orientation::rotateRight);
            break;
        default:
            throw new UnsupportedOperationException("Move " + nextMove + " is not supported");
        }
    }

    private void advance(final Adventurer adventurer) {
        Position currentPosition = adventurer.getPosition();
        Orientation currentOrientation = adventurer.getOrientation();

        Position nextPosition = getNextPosition(currentPosition, currentOrientation);

        PositionStatus positionStatus = treasureMap.isPositionAvailable(nextPosition);
        if (positionStatus == PositionStatus.AVAILABLE_LATER) {
            adventurer.abortMove();
            return;
        }

        if (positionStatus == PositionStatus.CURRENTLY_AVAILABLE) {
            adventurer.setPosition(nextPosition);
        }

        // Mark the move as being done for forbidden & available
        adventurer.commitMove();
    }

    private Position getNextPosition(final Position position, final Orientation orientation) {
        switch (orientation) {
        case NORTH:
            return position.withPreviousLine();
        case EAST:
            return position.withNextColumn();
        case SOUTH:
            return position.withNextLine();
        case WEST:
            return position.withPreviousColumn();
        default:
            throw new UnsupportedOperationException("Orientation " + orientation + " is not supported");
        }
    }

    private void doTurn(final Adventurer adventurer, final Function<Orientation, Orientation> turnAction) {
        Orientation previousOrientation = adventurer.getOrientation();

        Orientation newOrientation = turnAction.apply(previousOrientation);

        adventurer.setOrientation(newOrientation);
        adventurer.commitMove();
    }

}
