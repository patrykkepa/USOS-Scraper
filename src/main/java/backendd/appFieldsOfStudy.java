package backendd;

import backendd.CSVwriter.CSVwriter;
import backendd.CSVwriter.CSVwriterInterface;
import backendd.Logger.MyLogger;
import backendd.Requester.DataRequester;
import backendd.Requester.HTMLgetter;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


public class appFieldsOfStudy {
    private static final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static final String directory = "./Files/FieldsOfStudyFiles/";
    private static final String logFilename = "fieldsOfStudy.log";
    private static final String csvFilename = "fieldsOfStudy.csv";
    private static String response;
    private static Document responseDOC;
    private static Elements dataArr;
    private static final JSONArray fieldsOfStudy = new JSONArray();
    private static final String url = "https://usosweb.uni.lodz.pl/kontroler.php?_action=katalog2%2Fprogramy%2FwszystkieKierunki&lang=pl";

    private static void getDoc(DataRequester dataRequester, String url) throws IOException {
        response = dataRequester.getData(url, logger);
        parseToDOC();
    }

    private static void parseToDOC(){
        responseDOC = Jsoup.parse(response);
    }

    private static void extractFieldData(){
        dataArr = responseDOC.select("tbody.autostrong").select("tr");
    }

    private static void extractProgramData(){
        dataArr = responseDOC.select("div.usos-ui").select("li").select("a");
    }


    private static void formatData(String filter){
        AtomicInteger index = new AtomicInteger();
        dataArr.forEach(data -> {
            String field = "";
            ArrayList<String> programs = new ArrayList<>();
            Map<String, Object> tmpObj = new HashMap<>();
            index.incrementAndGet();
            try{
                try{
                    field = data.select("td").get(0).select("a").text();
                } catch(Exception e){
                    logger.warning("|   Failed to format name for field: " + index + ". ");
                }
//                try{
//                    Elements specArr = data.select("td").get(1).select("a");
//                    specArr.forEach(spec -> programs.add(spec.text()));
//                    System.out.println(programs);
//                } catch(Exception e){
//                    logger.warning("|   Failed to format programs for field: " + index + ". ");
//                }
                try{
                    String fieldLink =  data.select("td").get(0).select("a").attr("href");
                    getDoc(new HTMLgetter(), fieldLink);
                    extractProgramData();
                    dataArr.forEach(a -> {
                        programs.add(a.text());
                    });
                }catch(Exception e){
                    logger.warning("|   Failed to format programs for field: " + index + ". ");
                }
            } catch(Exception e){
                logger.warning("|   Do not found data in dorm: " + index + ". " + e);
            } finally {
                tmpObj.put("field", field);
                tmpObj.put("programs", programs);
                if(Objects.equals(filter, null)){
                    fieldsOfStudy.put(tmpObj);
                }else if(tmpObj.get("field").toString().contains(filter)){
                    fieldsOfStudy.put(tmpObj);
                }
            }
        });
    }

    private static void createCSV(CSVwriterInterface CSVwriter) throws IOException {
        CSVwriter.write(fieldsOfStudy, directory+csvFilename, logger);
    }

    //    RUN APP FUNCTION
    public static int run(String filter) throws IOException, ParseException {
        Files.createDirectories(Paths.get(directory));
        MyLogger.loggerConfig(logger, directory+logFilename);

        logger.info("| START: Starting application.");

        getDoc(new HTMLgetter(), url);
        extractFieldData();
        formatData(filter);

        createCSV(new CSVwriter());

        logger.info("| END: Closing application.");

        return fieldsOfStudy.length();
    }
}
