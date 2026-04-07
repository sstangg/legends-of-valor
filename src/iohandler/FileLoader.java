package iohandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    
public class FileLoader {

    public static <Item> List<Item> load(String fileName, LineParser<Item> parser) {
        List<Item> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                Item obj = parser.parse(line);
                if (obj != null) {
                    result.add(obj);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
