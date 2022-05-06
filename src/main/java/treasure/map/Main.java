package treasure.map;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import treasure.map.service.Processor;

public class Main {

    public static void main(final String[] args) throws IOException {
        if (args == null || args.length < 2) {
            System.err.println("Usage: TreasureMap <mapFile> <adventurerFile>");
            return;
        }

        Path mapFile = Paths.get(args[0]);
        Path adventurerFile = Paths.get(args[1]);

        Processor processor = new Processor();
        List<String> results = processor.process(mapFile, adventurerFile);

        results.forEach(System.out::println);
    }

}
