package backendd.CSVwriter;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class CSVwriter implements CSVwriterInterface {

    @Override
    public void write(JSONArray responseJSON, String fileName, Logger logger) throws IOException {

        try {
            File file = new File(fileName);
            String csvString = CDL.toString(responseJSON);
            FileUtils.writeStringToFile(file, csvString, "UTF-8");
            logger.info("|->   Written branches to csv file: " + responseJSON.length());
        } catch (IOException e){
            logger.warning("|->    CSV writer failure: " + e);
            System.exit(1);
        }
    }
}
