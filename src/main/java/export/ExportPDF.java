package export;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import objects.TestingResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExportPDF {

    public static int exportAsPdf(List<TestingResult> results, String outputDirectory, String htmlPath ) throws Exception {
        Path directory = Paths.get(outputDirectory);
        if(!Files.isDirectory(directory)){
            throw new Exception("[Error] Couldn't find output directory: " + outputDirectory);
        }

        HtmlManipulator manipulator = new HtmlManipulator();
        manipulator.loadBaseHtml(htmlPath);
        int i = 0;
        for(TestingResult result : results){
            Document document = new Document();
            FileOutputStream outputStream = new FileOutputStream(directory.toString()+ File.separator +result.lastname+"_"+ result.firstname +".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            String html = manipulator.insertValues(result);

            InputStream is = new ByteArrayInputStream(html.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    is);
            document.close();
            i++;
        }
        return i;
    }
}
