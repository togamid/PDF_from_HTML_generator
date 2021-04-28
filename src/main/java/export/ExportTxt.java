package export;

import objects.TestingResult;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExportTxt {

    public static void exportData(List<TestingResult> results, String outputDirectory) throws Exception{
        Path directory = Paths.get(outputDirectory);
        if(!Files.isDirectory(directory)){
            throw new Exception("[Error] Couldn't find output directory: " + outputDirectory);
        }
        for(TestingResult result : results){
            System.out.println(result.lastname);
            PrintWriter writer = new PrintWriter(new FileWriter(directory.toString()+ File.separator +result.lastname+".txt"));

            writer.printf("Mein tolles zertifikat! mein name ist %s", result.lastname);
            writer.close();
        }

    }
}
