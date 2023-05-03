package backendd.CSVwriter;

import org.json.JSONArray;

import java.io.IOException;
import java.util.logging.Logger;

public interface CSVwriterInterface {
    void write(JSONArray responseJSON, String fileName, Logger logger) throws IOException;
}
