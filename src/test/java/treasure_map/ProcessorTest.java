package treasure_map;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import treasure.map.service.Processor;

public class ProcessorTest {

    @Test
    public void process_lucky() throws IOException {
        List<String> expectedResults = Arrays.asList("Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Final state (after 10 ticks):",
                "No remaining treasures on the map", "Adventurers state:", "Adventurer Lucky is at Position[line=2, column=4] and has picked up 4 treasure(s)");
        doTest("src/test/resources/lucky", expectedResults);
    }

    @Test
    public void process_all() throws IOException {
        List<String> expectedResults = Arrays.asList("Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Tick", "Final state (after 10 ticks):",
                "No remaining treasures on the map", "Adventurers state:", "Adventurer John is at Position[line=3, column=2] and has picked up 0 treasure(s)",
                "Adventurer Lucky is at Position[line=2, column=4] and has picked up 4 treasure(s)", "Adventurer Stucky is at Position[line=3, column=6] and has picked up 0 treasure(s)",
                "Adventurer Blocked is at Position[line=4, column=1] and has picked up 0 treasure(s)");
        doTest("src/test/resources/all", expectedResults);
    }

    private void doTest(final String folder, final List<String> expectedResults) throws IOException {
        Path mapFile = Paths.get(folder, "map.txt");
        Path adventurerFile = Paths.get(folder, "adventurers.txt");

        Processor processor = new Processor();
        List<String> actual = processor.process(mapFile, adventurerFile);

        Assertions.assertEquals(expectedResults, actual);
    }

}
