package pl.pw.wsd.wsdparking.city;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CityMapLoader {

    private static final Map<Character, FieldType> fieldTypes = new HashMap<>();

    static {
        fieldTypes.put('s', FieldType.STREET);
        fieldTypes.put('p', FieldType.PARKING);
    }

    public CityMap loadFromFile(String filePath) {
        try(BufferedReader reader = newReader(filePath)) {
            return readCityMap(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedReader newReader(String filePath) {
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private CityMap readCityMap(BufferedReader reader) throws IOException {
        CityMap cityMap = new CityMap();
        String line;
        int y = 0;
        while((line = reader.readLine()) != null) {
            for (int x = 0; x < line.length(); x++) {
                FieldType fieldType = fieldTypes.get(line.charAt(x));
                cityMap.set(new Position(x, y), fieldType);
            }
            y++;
        }
        return cityMap;
    }
}
