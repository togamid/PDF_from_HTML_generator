package export;

import objects.TestingResult;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportPDF {
    private static final String ownerKey = "password";

    public static int exportAsPdf(List<TestingResult> results, String outputDirectory, String fillablePdfPath ) throws Exception {
        //create a new Access permission to disallow modifications
        AccessPermission ap = new AccessPermission();
        ap.setCanExtractForAccessibility(true);
        ap.setCanPrint(true);
        ap.setCanPrintDegraded(true);
        ap.setCanAssembleDocument(false);
        ap.setCanExtractContent(false);
        ap.setCanModify(false);
        ap.setCanModifyAnnotations(false);
        ap.setCanFillInForm(false);
        ap.setReadOnly();
        StandardProtectionPolicy standardPP = new StandardProtectionPolicy(ownerKey, "", ap);
        standardPP.setEncryptionKeyLength(128);



        int i = 0;
        for (TestingResult result : results) {
             PDDocument document = PDDocument.load(new File(fillablePdfPath));

             PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
             acroForm.setXFA(null);

             //replace the font of the form fields with Helvetica so it is rendered correctly on Linux, Windows and Apple
             //This is ugly, but I didn't find a better way to do it
            PDFont font = PDType1Font.HELVETICA;
            PDResources res = acroForm.getDefaultResources();
            if (res == null) res = new PDResources();
            COSName fontName = res.add(font);
            acroForm.setDefaultResources(res);
            for(PDField field : acroForm.getFields()){
                COSDictionary dict = field.getCOSObject();
                COSString defaultAppearance = (COSString) dict.getDictionaryObject(COSName.DA);
                if(defaultAppearance != null){
                    String currentValue = dict.getString(COSName.DA);
                    dict.setString(COSName.DA,currentValue.replace("F3", fontName.getName()));
                }
            }
            acroForm.refreshAppearances();
             acroForm.setNeedAppearances(false);

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
                acroForm.flatten();
                document.protect(standardPP);
                document.save(result.generatePDFLocation(outputDirectory));
                document.close();
                i++;
                }
            catch(Exception e) {
                    System.out.println("PDF for " + result.lastname +", " + result.firstname + " could not be generated!");
                    System.out.println(e.getMessage());
            }
        }
        return i;
    }
}
