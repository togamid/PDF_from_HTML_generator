package export;

import objects.TestingResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

public class HtmlManipulator {

    private Document baseDoc;

    public void loadBaseHtml(String path) throws Exception {
        File input = new File(path);
        baseDoc = Jsoup.parse(input, "UTF-8", "http://example.com/");
    }


    public String insertValues(TestingResult result) {
        Document doc = baseDoc.clone();
        if(!doc.select("#name").isEmpty()){
            doc.select("#name").first().text(result.lastname+", "+result.firstname);
        }
        if(!doc.select("#birthdate").isEmpty()){
            doc.select("#birthdate").first().text(result.birthdate);
        }
        if(!doc.select("#street").isEmpty()){
            doc.select("#street").first().text(result.street);
        }
        if(!doc.select("#city").isEmpty()){
            doc.select("#city").forEach(e -> e.text(result.city));
        }
        if(!doc.select("#telefone").isEmpty()){
            doc.select("#telefone").first().text(result.telnr);
        }
        if(!doc.select("#email").isEmpty()){
            doc.select("#email").first().text(result.email);
        }
        if(!doc.select("#date").isEmpty()){
            doc.select("#date").forEach(e -> e.text(result.date));
        }
        if(!doc.select("#time").isEmpty()){
            doc.select("#time").first().text(result.time);
        }
        if(!doc.select("#test_used").isEmpty()){
            doc.select("#test_used").first().text(result.usedTest);
        }
        if(!doc.select("#manufacturer").isEmpty()){
            doc.select("#manufacturer").first().text(result.testManufacturer);
        }
        if(!doc.select("#result").isEmpty()){
            Element resultElement = doc.select("#result").first();
            switch (result.result) {
                case "N":
                    resultElement.text("negativ");
                    break;
                case "P":
                    resultElement.text("positiv");
                    break;
                case "U":
                    resultElement.text("nicht auswertbar");
                    break;

            }
        }
        else {

            switch (result.result) {
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
        }
        return doc.toString();

    }
}
