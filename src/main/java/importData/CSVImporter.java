package importData;


import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class CSVImporter {

    public static List<String[]> importCSV(String path) throws Exception{
        Reader reader = new BufferedReader(new FileReader(path));
        return readAll(reader);
    }

    private static List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(new CSVParserBuilder().withSeparator(';').build() ).build();
        //CSVReader csvReader = new CSVReader(reader);
        List<String[]> list;
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        if(list.get(0)[0].equalsIgnoreCase("Nachname") || list.get(0)[0].equalsIgnoreCase("Antwort Nr.")){
            list.remove(0);
        }
        return list;
    }
}
