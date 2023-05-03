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
import java.util.*;
import java.util.logging.Logger;


public class appDormitory {
    private  final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private  final String directory = "./Files/DormitoryFiles/";
    private  final String logFilename = "dormitories.log";
    private  final String csvFilename = "dormitories.csv";
    private  String response;
    private  Document responseDOC;
    private  Elements dataArr;
    private  final List<String> linksArr = new ArrayList<>();
    private  final JSONArray dormitories = new JSONArray();
    private  final String url = "https://usosweb.uni.lodz.pl/kontroler.php?_action=katalog2/akademiki/index";

    private  void getDoc(DataRequester dataRequester, String url) throws IOException {
        response = dataRequester.getData(url, logger);
        parseToDOC();
    }

    private  void parseToDOC(){
        responseDOC = Jsoup.parse(response);
    }

    private  void extractLinks(){
        Elements dataArr = responseDOC.select("ul.no-bullets").select("a[href]");
        dataArr.forEach( link -> {linksArr.add(link.attr("href"));});
    }

    private  void extractData(){
        dataArr = responseDOC.select("table.grey");
    }

    private  void formatData(String link, String filter){
        String name = "", shortcut = "", zipcode = "", city = "", street = "";
        ArrayList<String> phones = new ArrayList<>();
        Map<String, Object> tmpObj = new HashMap<>();
        try{
            try{
                name = dataArr.select("tr.dorm-name").select("td").get(1).text();
            } catch(Exception e) {
                logger.warning("|   Failed to format name for dorm: " + link + ". ");
            }
            try{
                shortcut = dataArr.select("tr.strong").select("td").get(1).text();
            } catch(Exception e) {
                logger.warning("|   Failed to format shortcut for dorm: " + link + ". ");
            }
            try{
                String[] phonesRaw = dataArr.select("tr.dorm-phones").select("td").get(1).toString().split("<br>");
                for(int i=0; i<phonesRaw.length-1; i++){
                    phonesRaw[i] = phonesRaw[i].replace("<td>","").replace("<b>","").replace("</b>","").replace("</td>","").strip();
                    phones.add(phonesRaw[i]);
                }
            } catch(Exception e) {
                logger.warning("|   Failed to format phones for dorm: " + link + ". ");
            }
            try{
                String[] address = dataArr.select("tr.strong.dorm-address").select("td").get(1).text().split(", ");
                zipcode = address[0].split(" ")[0];
                city = address[0].split(" ")[1];
                street = address[1];
            } catch(Exception e) {
                logger.warning("|   Failed to format address for dorm: " + link + ". ");
            }
        } catch(Exception e){
            logger.warning("|   Do not found data in dorm: " + link + ". " + e);
        } finally {
            tmpObj.put("name", name);
            tmpObj.put("shortcut", shortcut);
            tmpObj.put("zipcode", zipcode);
            tmpObj.put("city", city);
            tmpObj.put("street", street);
            tmpObj.put("phones", phones);
            tmpObj.put("url", link);
            if(Objects.equals(filter, "all")){
                dormitories.put(tmpObj);
            } else if(tmpObj.get("name").toString().contains(filter)){
                dormitories.put(tmpObj);
            }
        }
    }

    private  void createCSV(CSVwriterInterface CSVwriter) throws IOException {
        CSVwriter.write(dormitories, directory+csvFilename, logger);
    }

    //    RUN APP FUNCTION
    public  int run(String filter) throws IOException, ParseException {
        if(filter == null) filter = "all";
        String finalFilter = filter;

        Files.createDirectories(Paths.get(directory));
        MyLogger.loggerConfig(logger, directory+logFilename);

        logger.info("| START: Starting application.");

        getDoc(new HTMLgetter(), url);
        extractLinks();
        linksArr.forEach(link -> {
            try {
                getDoc(new HTMLgetter(), link);
                extractData();
                formatData(link, finalFilter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        createCSV(new CSVwriter());


        logger.info("| END: Closing application.");

        return dormitories.length();
    }

}
