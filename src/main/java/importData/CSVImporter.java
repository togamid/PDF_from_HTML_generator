package importData;


import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class CSVImporter {

    public static List<String[]> importCSV(String path) throws Exception{
        Reader reader = new BufferedReader(new FileReader(path)); /*Paths.get(
        ClassLoader.getSystemResource(path).toURI())*/
        return readAll(reader);
    }



    private static List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list;
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        list.remove(0);
        return list;
    }


}
