package backendd.Logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
    public static void loggerConfig(Logger logger, String fileName){
        try{
            var fh = new FileHandler(fileName,true);
            System.setProperty("java.util.logging.SimpleFormatter.format","%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
            var formatter = new SimpleFormatter();
            logger.addHandler(fh);
            fh.setFormatter(formatter);
            logger.setLevel(Level.CONFIG);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
