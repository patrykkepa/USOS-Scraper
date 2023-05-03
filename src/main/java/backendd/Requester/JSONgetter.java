package backendd.Requester;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class JSONgetter implements DataRequester {

    @Override
    public String getData(String link, Logger logger) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try(
            Scanner scanner = new Scanner(connection.getInputStream());
        ){
            return scanner.useDelimiter("\\A").next();
        } catch(IOException e) {
            logger.warning("|->    GET request status code: " + connection.getResponseCode());
            logger.warning("|->    GET request failure: \n" + e);
            logger.warning("| END: Closing program.");
//            System.exit(1);
        }
        return "";
    };

//    public Document parseSoup(String repsonse){
//      return Jsoup.parse(repsonse);
//    };
}
