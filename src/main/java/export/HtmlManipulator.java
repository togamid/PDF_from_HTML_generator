package export;

import objects.TestingResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class HtmlManipulator {

    private Document baseDoc;

    public void loadBaseHtml(String path) throws Exception {
        File input = new File(path);
        baseDoc = Jsoup.parse(input, "UTF-8", "http://example.com/");
    }


    public String insertValues(TestingResult result) {
        Document doc = baseDoc.clone();

        doc.select("#name").first().text(result.lastname+", "+result.firstname);
        doc.select("#birthdate").first().text(result.birthdate);
        doc.select("#street").first().text(result.street);
        doc.select("#city").first().text(result.city);
        doc.select("#telefone").first().text(result.telnr);
        doc.select("#email").first().text(result.email);
        doc.select("#date").first().text(result.date);
        doc.select("#time").first().text(result.time);
        doc.select("#test_used").first().text(result.usedTest);
        switch(result.result){
            case "N":
                doc.select("#negative").first().text("__X__");
                break;
            case "P":
                doc.select("#positive").first().text("__X__");
                break;
            case "U":
                doc.select("#undecided").first().text("__X__");
                break;

        }
        return doc.toString();

    }
}
