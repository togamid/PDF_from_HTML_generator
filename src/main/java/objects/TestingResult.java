package objects;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestingResult {
    //private static final int numberOfValues = 12;
    private static final int numberOfValues = 15;
    private String pathPDF;
    public final String lastname;
    public final String firstname;
    public final String street;
    public final String city;
    public final String birthdate;
    public final String telnr;
    public final String email;
    public final String date;
    public final String time;
    public final String result;
    public final String resultLong;
    public final String usedTest;
    public final String testManufacturer;
    public TestingResult(String[] values){
        /*
        this.lastname = values[0];
        this.firstname = values[1];
        this.street = values[2];
        this.city = values[3];
        this.birthdate = values[4];
        this.telnr = values[5];
        this.email= values[6];
        this.date = values[7];
        this.time = values[8];
        this.result = values[9];
        this.usedTest = values[10];
        this.testManufacturer = values[11];
         */
        this.lastname = values[1];
        this.firstname = values[2];
        this.birthdate = values[3];
        this.street = values[4] + " " + values[5];
        this.city = values[6] + " " + values[7];
        this.telnr = values[8];
        this.email= values[9];
        this.date = values[10];
        this.time = values[11];
        this.result = values[12];
        this.usedTest = values[13];
        this.testManufacturer = values[14];

        switch (this.result) {
            case "N":
                resultLong = "negativ";
                break;
            case "P":
                resultLong = "positiv";
                break;
            case "U":
                resultLong = "nicht auswertbar";
                break;
            default:
                resultLong = "N/A";
        }
    }

    public String getPDFLocation(){
            return pathPDF;
    }

    //returns the Path were the PDF should be placed
    public String generatePDFLocation(String directory){
        Path directoryPath = Paths.get(directory);
        if(!Files.isDirectory(directoryPath)){
            throw new RuntimeException("[Error] Couldn't find output directory: " + directory);
        }
        String path = directoryPath.toString() + File.separator +lastname+"_"+ firstname + ".pdf";
        int i = 0;
        while(Files.exists(Paths.get(path))){
            path = directoryPath.toString() + File.separator +lastname+"_"+ firstname +i+ ".pdf";
            i++;
        }
        this.pathPDF = path;
        return path;
    }

    public static List<TestingResult> parseStrings(List<String[]> stringArrayList){
        List<TestingResult> results = new ArrayList<>();
        for(String[] entry : stringArrayList){
            if(entry.length < numberOfValues){
                System.out.println("[Error] Skipped invalid entry! Length: "+ entry.length);
            } else {
                results.add(new TestingResult(entry));
            }
        }
        return results;
    }
}
