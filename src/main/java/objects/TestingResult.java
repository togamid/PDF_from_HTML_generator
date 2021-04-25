package objects;

import java.util.ArrayList;
import java.util.List;

public class TestingResult {
    private static final int numberOfValues = 11;
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
    public final String usedTest;
    public TestingResult(String[] values){
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

    }

    public static List<TestingResult> parseStrings(List<String[]> stringArrayList){
        List<TestingResult> results = new ArrayList<>();
        for(String[] entry : stringArrayList){
            if(entry.length < numberOfValues){
                System.out.println("[Error] Skipped invalid entry!");
            }
            results.add(new TestingResult(entry));
        }

        return results;
    }
}
