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
import java.util.logging.Logger;


public class appPrograms {
    private  final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private  final String directory = "./Files/ProgramsFiles/";
    private  final String logFilename = "programs.log";
    private  final String csvFilename = "programs.csv";
    private  String response;
    private  Document responseDOC;
    private  Elements dataArr;
    private  ArrayList<String> fieldsLinks = new ArrayList();
    private  ArrayList<String> programsLinks = new ArrayList();
    private  final JSONArray programs = new JSONArray();
    private  final String url = "https://usosweb.uni.lodz.pl/kontroler.php?_action=katalog2%2Fprogramy%2FwszystkieKierunki&lang=pl";

    private  void getDoc(DataRequester dataRequester, String url) throws IOException {
        response = dataRequester.getData(url, logger);
        parseToDOC();
    }

    private  void parseToDOC(){
        responseDOC = Jsoup.parse(response);
    }

    private  void extractFieldLinks(){
        Elements fieldsArr = responseDOC.select("tbody.autostrong").select("tr");
        fieldsArr.forEach(field -> {
            fieldsLinks.add(field.select("td").get(0).select("a").attr("href"));
        });
    }

    private  void extractProgramsLinks(){
        Elements programsArr = responseDOC.select("div.usos-ui").select("li").select("a");
        programsArr.forEach(program -> {
            programsLinks.add(program.attr("href"));
        });
    }

    private  void extractData(){
        dataArr = responseDOC.select("tbody.autostrong").select("tr");
    }

    private  void formatData(String link, String filterPhrase, String filterStat, String filterNone, String filterType){
        String code = "", name = "", mode = "", type = "", duration = "", typeOfStudy = "", units="";
        ArrayList<String> specializations = new ArrayList<>();
        ArrayList<String> phones = new ArrayList<>();
        Map<String, Object> tmpObj = new HashMap<>();
        try{
            try{
                code = dataArr.get(0).select("td").get(1).text();
            } catch(Exception e){
                logger.warning("|   Failed to format code for: " + link + ". ");
            }
            try{
                name = dataArr.get(1).select("td").get(1).text();
            } catch(Exception e){
                logger.warning("|   Failed to format name for: " + link + ". ");
            }
            try{
                mode = dataArr.get(2).select("td").get(1).text();
            } catch(Exception e){
                logger.warning("|   Failed to format mode for: " + link + ". ");
            }
            try{
                type = dataArr.get(3).select("td").get(1).text();
            } catch(Exception e){
                logger.warning("|   Failed to format type for: " + link + ". ");
            }
            try{
                duration = dataArr.get(4).select("td").get(1).text();
            } catch(Exception e){
                logger.warning("|   Failed to format duration for: " + link + ". ");
            }
            try{
                typeOfStudy = dataArr.get(5).select("td").get(1).text();
            } catch(Exception e){
                logger.warning("|   Failed to format specialities for: " + link + ". ");
            }
            try{
                Elements data = dataArr.get(6).select("td").get(1).select("a");
                for(int i = 1; i<data.size(); i+=2){
                    specializations.add(data.get(i).text());
                }
            } catch(Exception e){
                logger.warning("|   Failed to format specializations for: " + link + ". ");
            }
            try{
                units = dataArr.get(7).select("td").get(1).text().replace("[ inne programy w tej jednostce ]", "");
            } catch(Exception e){
                logger.warning("|   Failed to format units for: " + link + ". ");
            }
        } catch(Exception e){
            logger.warning("|   Do not found data in dorm: " + link + ". " + e);
        } finally {
            tmpObj.put("code", code);
            tmpObj.put("typeOfStudy", typeOfStudy);
            tmpObj.put("specializations", specializations);
            tmpObj.put("program", name);
            tmpObj.put("mode", mode);
            tmpObj.put("type", type);
            tmpObj.put("duration", duration);
            tmpObj.put("units", units);
            tmpObj.put("url", link);

            if((filterStat != "" && filterNone != "" || filterStat == "" && filterNone == "")&&
                tmpObj.get("typeOfStudy").toString().contains(filterPhrase) &&
                tmpObj.get("type").toString().contains(filterType)
            ){
                programs.put(tmpObj);
            } else if (filterStat == "" && filterNone != "" &&
                tmpObj.get("mode").toString().startsWith(filterNone) &&
                tmpObj.get("typeOfStudy").toString().contains(filterPhrase) &&
                tmpObj.get("type").toString().contains(filterType)
            ){
                programs.put(tmpObj);
            }else if (filterStat != "" && filterNone == "" &&
                tmpObj.get("mode").toString().startsWith(filterStat) &&
                tmpObj.get("typeOfStudy").toString().contains(filterPhrase) &&
                tmpObj.get("type").toString().contains(filterType)
            ) {
                programs.put(tmpObj);
            }
        }
    }

    private  void createCSV(CSVwriterInterface CSVwriter) throws IOException {
        CSVwriter.write(programs, directory+csvFilename, logger);
    }

    //    RUN APP FUNCTION
    public  int run(String filterPhrase, Boolean filterStat, Boolean filterNone, String filterType) throws IOException, ParseException {

//      filters setup
        if(filterPhrase == null) filterPhrase = "";
        String finalFilterPhrase = filterPhrase;

        if(filterType == null || filterType == "all") filterType = "";
        if(filterType == "undergraduate") filterType = "pierwszego";
        if(filterType == "master's degree") filterType = "drugiego";
        String finalFilterType = filterType;

        String finalFilterStat = "";
        String finalFilterNone = "";
        if(filterStat) finalFilterStat = "stacjonarne";
        if(filterNone) finalFilterNone = "niestacjonarne";


        Files.createDirectories(Paths.get(directory));
        MyLogger.loggerConfig(logger, directory+logFilename);

        logger.info("| START: Starting application.");

        getDoc(new HTMLgetter(), url);
        extractFieldLinks();
        for (String field : fieldsLinks) {
            getDoc(new HTMLgetter(), field);
            extractProgramsLinks();
        }
        for (String program : programsLinks) {
            getDoc(new HTMLgetter(), program);
            extractData();
            formatData(program, finalFilterPhrase, finalFilterStat, finalFilterNone, finalFilterType);
        }

        createCSV(new CSVwriter());

        logger.info("| END: Closing application.");

        return programs.length();
    }
}
