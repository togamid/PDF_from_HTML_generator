package export;

import objects.TestingResult;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportPDF {

    public static int exportAsPdf(List<TestingResult> results, String outputDirectory, String fillablePdfPath ) throws Exception {
        PDDocument baseDocument = PDDocument.load(new File(fillablePdfPath));
        PDAcroForm acroForm =  baseDocument.getDocumentCatalog().getAcroForm();
        acroForm.setXFA(null);

        int i = 0;
        for (TestingResult result : results) {
            try {
                acroForm.getField("name").setValue(result.lastname + ", " + result.firstname);
                acroForm.getField("birthdate").setValue(result.birthdate);
                acroForm.getField("test_name").setValue(result.usedTest);
                acroForm.getField("test_manufacturer").setValue(result.testManufacturer);
                acroForm.getField("date").setValue(result.date);
                acroForm.getField("time").setValue(result.time);
                acroForm.getField("date2").setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                String resultString = "N/A";
                switch (result.result) {
                    case "N":
                        resultString = "negativ";
                        break;
                    case "P":
                        resultString = "positiv";
                        break;
                    case "U":
                        resultString = "nicht auswertbar";
                        break;
                }

                acroForm.getField("result").setValue(resultString);
                baseDocument.save(result.generatePDFLocation(outputDirectory));
                i++;
                }
            catch(Exception e) {
                    System.out.println("PDF for " + result.lastname +", " + result.firstname + " could not be generated!");
            }
        }
        baseDocument.close();
        return i;
    }
}
