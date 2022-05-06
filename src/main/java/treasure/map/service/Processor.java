package treasure.map.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import treasure.map.model.TreasureMap;

public class Processor {

    public List<String> process(final Path mapFile, final Path adventurerFile) throws IOException {
        List<String> results = new ArrayList<>();

        WorldParser worldParser = new WorldParser(mapFile, adventurerFile);
        TreasureMap treasureMap = worldParser.parse();

        WorldManager worldManager = new WorldManager(treasureMap);

        AtomicInteger tickAmount = new AtomicInteger();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(() -> {
            if (worldManager.hasMoreTick()) {
                results.add("Tick");
                worldManager.tick();
                tickAmount.incrementAndGet();
            } else {
                executorService.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);

        try {
            future.get();
        } catch (Exception e) {
            // noop
        }

        results.add("Final state (after " + tickAmount + " ticks):");

        if (treasureMap.hasTreasures()) {
            results.add("Remaining treasures " + treasureMap.getRemainingTreasures());
        } else {
            results.add("No remaining treasures on the map");
        }

        results.add("Adventurers state:");
        treasureMap.getAdventurers()
                   .stream()
                   .map(adventurer -> "Adventurer " + adventurer.getName() + " is at " + adventurer.getPosition() + " and has picked up " + adventurer.getFoundTreasureAmount() + " treasure(s)")
                   .forEach(results::add);

        return results;
    }

}
