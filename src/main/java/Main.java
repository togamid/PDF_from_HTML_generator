import email.Email;
import export.ExportPDF;
import importData.CSVImporter;
import objects.TestingResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    private static String pathInput;
    private static String pathTemplate = "template.pdf";
    private static String pathOutputDirectory;
    private static String selectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    public static void main(String[] args){

        //get the needed paths from the user through a dialogue
        try {
            getPaths();
        } catch (Exception e){
            System.out.println("[Error] Something went wrong during the initialisation dialogue: " + e.getMessage());
            return;
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

        //filter unused dates
        results.removeIf(result -> !result.date.equalsIgnoreCase(selectedDate));

        //insert them into the template HTML and export as PDF
        int PDFs_exported = 0;
        try {
            PDFs_exported = ExportPDF.exportAsPdf(results, pathOutputDirectory, pathTemplate);
        }
        catch (Exception e) {
            System.out.println("[Error] Something went wrong with the exporting: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(PDFs_exported + " PDFs erzeugt. Versende E-Mails...");

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
                System.out.println("Dies ist kein Pfad zu einem CSV. Dieser m??sste mit .csv enden. bitte geben sie ihn erneut an.");
            }
        }while(tmp.isEmpty());
        pathInput = tmp;

        Path directoryPath = Paths.get(pathTemplate);
        if(!Files.exists(directoryPath)) {
            do {
                System.out.print("\nBitte geben Sie den Pfad zum PDF Template an: ");
                tmp = reader.readLine();
                if (!tmp.endsWith(".pdf")) {
                    System.out.println("Dies ist kein Pfad zu einem PDF file. Dieser m??sste mit .pdf enden. Bitte geben sSe ihn erneut an.");
                }
            } while (tmp.isEmpty());
            pathTemplate = tmp;
        }


        File outputDirectory = new File(LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")));
        if(!outputDirectory.exists()){
            outputDirectory.mkdir();
            pathOutputDirectory = outputDirectory.getPath();
        }else {
            do {
                System.out.println("\nDas Verzeichnis " + outputDirectory.getPath() + " existiert bereits. Wenn Sie es ausw??hlen wollen, geben Sie den Namen explizit an.");
                System.out.print("Bitte geben Sie den Pfad zum Ausgabeverzeichnis an: ");
                tmp = reader.readLine();
            }while(tmp.isEmpty());
            pathOutputDirectory = tmp;
        }
        
        System.out.print("\nAktuell ausgew??hltes Datum: "+ selectedDate +"\nWenn Sie ein anderes Datum ausw??hlen wollen, " +
                    "geben Sie dieses an (ansonsten einfach Enter dr??cken): ");
        tmp = reader.readLine();
        if(!tmp.isEmpty()) {
            selectedDate = tmp;
        }
        System.out.println("\n");
    }
}