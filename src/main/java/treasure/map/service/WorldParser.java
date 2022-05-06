package treasure.map.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import treasure.map.model.Adventurer;
import treasure.map.model.MapDimension;
import treasure.map.model.Position;
import treasure.map.model.TreasureMap;
import treasure.map.parser.AdventurerFileParser;
import treasure.map.parser.MapFileParser;
import treasure.map.vo.MapFileAccumulator;

public record WorldParser(Path mapFile, Path adventurerFile) {

    public TreasureMap parse() throws IOException {
        MapFileParser mapFileParser = new MapFileParser();
        MapFileAccumulator mapFileAccumulator = mapFileParser.parse(mapFile);

        AdventurerFileParser adventurerFileParser = new AdventurerFileParser();
        List<Adventurer> adventurers = adventurerFileParser.parse(adventurerFile);

        return toTreasureMap(mapFileAccumulator, adventurers);
    }

    private TreasureMap toTreasureMap(final MapFileAccumulator mapFileAccumulator, final List<Adventurer> adventurers) {
        MapDimension mapDimension = mapFileAccumulator.getMapDimension();
        if (mapDimension == null) {
            throw new IllegalArgumentException("Missing map dimension");
        }

        Set<Position> mountains = mapFileAccumulator.getMountains();

        for (Adventurer adventurer : adventurers) {
            Position adventurerStartPosition = adventurer.getPosition();
            if (!mapDimension.contains(adventurerStartPosition)) {
                throw new IllegalArgumentException("Adventurer " + adventurer.getName() + " is outside the map");
            }

            if (mountains.contains(adventurerStartPosition)) {
                throw new IllegalArgumentException("Adventurer " + adventurer.getName() + " is starting on a mountain");
            }

        }

        return new TreasureMap(mapDimension, mapFileAccumulator.getTreasures(), mountains, adventurers);
    }

}
