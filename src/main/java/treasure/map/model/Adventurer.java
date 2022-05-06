package treasure.map.model;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class Adventurer {

    private final String name;

    private final Deque<Move> remainingMoves;

    private final List<Treasure> foundTreasures = new ArrayList<>();

    private Position position;

    private Orientation orientation;

    private boolean tryingToMove = false;

    public Adventurer(final String name,
                      final Deque<Move> remainingMoves,
                      final Position position,
                      final Orientation orientation) {
        this.name = name;
        this.remainingMoves = remainingMoves;
        this.position = position;
        this.orientation = orientation;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(final Orientation orientation) {
        this.orientation = orientation;
    }

    public Optional<Move> getNextMove() {
        if (tryingToMove) {
            throw new IllegalStateException("Adventurer " + name + " is already moving");
        }

        if (remainingMoves.isEmpty()) {
            return Optional.empty();
        }

        this.tryingToMove = true;
        return Optional.of(remainingMoves.peek());
    }

    public boolean isOutOfMove() {
        return remainingMoves.isEmpty();
    }

    public void abortMove() {
        stopMoving();

        this.tryingToMove = false;
    }

    public void commitMove() {
        stopMoving();
        remainingMoves.pop();
    }

    private void stopMoving() {
        if (!tryingToMove) {
            throw new IllegalStateException("Adventurer " + name + " is not moving");
        }

        this.tryingToMove = false;
    }

    public void addNewTreasure(final Treasure treasure) {
        this.foundTreasures.add(treasure);
    }

    public int getFoundTreasureAmount() {
        return this.foundTreasures.size();
    }

    @Override
    public String toString() {
        return "Adventurer [name=" + name + ", remainingMoves=" + remainingMoves + ", foundTreasures=" + foundTreasures + ", position=" + position + ", orientation=" + orientation + ", tryingToMove="
                + tryingToMove + "]";
    }

}
