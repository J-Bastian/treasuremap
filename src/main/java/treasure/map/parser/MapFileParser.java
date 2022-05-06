package treasure.map.parser;

import treasure.map.model.MapDimension;
import treasure.map.model.Position;
import treasure.map.model.Treasure;
import treasure.map.vo.MapFileAccumulator;

public class MapFileParser extends AbstractFileParser<MapFileAccumulator> {

    private static final int MOUNTAIN_COLUMN_AMOUNT = 2;

    private static final int MAP_COLUMN_AMOUNT = 3;

    private static final int TREASURE_COLUMN_AMOUNT = 3;

    @Override
    protected MapFileAccumulator getAccumulator() {
        return new MapFileAccumulator();
    }

    @Override
    protected void consume(final String[] columns, final MapFileAccumulator accumulator) {
        if (columns.length < 1) {
            throw new IllegalArgumentException("Received an empty columns array");
        }

        switch (columns[0]) {
        case "C":
            parseMap(columns, accumulator);
            break;
        case "T":
            parseTreasure(columns, accumulator);
            break;
        case "M":
            parseMountain(columns, accumulator);
            break;
        default:
            throw new IllegalArgumentException("Received an type in the map file <" + columns[0] + ">");
        }
    }

    private void parseMountain(final String[] columns, final MapFileAccumulator accumulator) {
        if (columns.length != MOUNTAIN_COLUMN_AMOUNT) {
            throw new IllegalArgumentException("Received an invalid amount of column (" + columns.length + "). Expected:" + MOUNTAIN_COLUMN_AMOUNT);
        }

        Position position = parsePosition(columns[1]);

        accumulator.addMountain(position);
    }

    private void parseTreasure(final String[] columns, final MapFileAccumulator accumulator) {
        if (columns.length != TREASURE_COLUMN_AMOUNT) {
            throw new IllegalArgumentException("Received an invalid amount of column (" + columns.length + "). Expected:" + TREASURE_COLUMN_AMOUNT);
        }

        Position position = parsePosition(columns[1]);

        try {
            int amount = Integer.parseInt(columns[2]);

            Treasure treasure = new Treasure(position, amount);
            accumulator.addTreasure(treasure);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Treasure amount <" + columns[2] + "> is not valid");
        }
    }

    private void parseMap(final String[] columns, final MapFileAccumulator accumulator) {
        if (columns.length != MAP_COLUMN_AMOUNT) {
            throw new IllegalArgumentException("Received an invalid amount of column (" + columns.length + "). Expected:" + MAP_COLUMN_AMOUNT);
        }

        try {
            int width = Integer.parseInt(columns[1]);
            int height = Integer.parseInt(columns[2]);
            MapDimension mapDimension = new MapDimension(height, width);
            accumulator.setMapDimension(mapDimension);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Map dimensions <" + columns[1] + "," + columns[2] + "> are not valid");
        }
    }

}
