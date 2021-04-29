
import email.Email;
import export.ExportPDF;

import importData.CSVImporter;
import objects.TestingResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    private static String pathInput;
    private static String pathTemplate;
    private static String pathOutputDirectory;

    public static void main(String[] args){

        //get the needed paths from the user through a dialogue
        try {
            getPaths();
        } catch (Exception e){
            System.out.println("[Error] Something went wrong during the initialisation dialogue: " + e.getMessage());
        }

        //load the values from the CSV
        List<String[]> values;
        try {
            values = CSVImporter.importCSV(pathInput);
        }
        catch (Exception e)
        {
            System.out.println("[Error] Something went wrong with the importing: " + e.getMessage());
            return;
        }
        //parse them into testingResult objects
        List<TestingResult> results = TestingResult.parseStrings(values);

        //insert them into the template HTML and export as PDF
        int PDFs_exported = 0;
        try {
            PDFs_exported = ExportPDF.exportAsPdf(results, pathOutputDirectory, pathTemplate);
        }
        catch (Exception e) {
            System.out.println("[Error] Something went wrong with the exporting: " + e.getMessage());
        }

        Email.initSession("username", "password", "smtp_host"); //TODO, richtigen Server nehmen
        int emails_sent = Email.sendEmails(results);


        System.out.println("\n");
        System.out.println("Fertig! Bitte schaue bei einigen PDFs nach, ob alles geklappt hat. Es wurden " + PDFs_exported +" PDFs erzeugt und "+ emails_sent+" Emails gesendet.");
    }

    private static void getPaths() throws Exception{
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Den Ort einer Datei findet man am leichtesten, indem man mit Rechtsklick auf sie -> Eigenschaften -> Dort Ort und Name zusammenkopieren.");
        String tmp;
        do {
            System.out.print("\nBitte geben Sie den Pfad zum input CSV an: ");
            tmp = reader.readLine();
            if(!tmp.endsWith(".csv")){
                System.out.println("Dies ist kein Pfad zu einem CSV. Dieser müsste mit .csv enden. bitte geben sie ihn erneut an.");
            }
        }while(tmp.isEmpty());
        pathInput = tmp;



        do {
            System.out.print("\nBitte geben Sie den Pfad zum HTML Template an: ");
            tmp = reader.readLine();
            if(!tmp.endsWith(".html")){
                System.out.println("Dies ist kein Pfad zu einem HTML file. Dieser müsste mit .html enden. bitte geben sie ihn erneut an.");
            }
        }while(tmp.isEmpty());
        pathTemplate = tmp;


        do {
            System.out.print("\nBitte geben Sie den Pfad zum Ausgabeverzeichnis an: ");
            tmp = reader.readLine();
        }while(tmp.isEmpty());
        pathOutputDirectory = tmp;
        System.out.println("\n");
    }
}
