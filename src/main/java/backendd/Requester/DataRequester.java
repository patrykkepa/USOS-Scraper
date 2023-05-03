package backendd.Requester;

import java.io.IOException;
import java.util.logging.Logger;

public interface DataRequester {
    String getData(String url, Logger logger) throws IOException;
}
